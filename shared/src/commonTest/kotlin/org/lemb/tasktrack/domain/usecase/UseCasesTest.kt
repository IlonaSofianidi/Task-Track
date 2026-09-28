package org.lemb.tasktrack.domain.usecase

import app.cash.turbine.test
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.lemb.tasktrack.domain.model.Subtask
import org.lemb.tasktrack.domain.model.Task
import org.lemb.tasktrack.domain.model.TaskPriority
import org.lemb.tasktrack.domain.model.TaskStatus
import org.lemb.tasktrack.domain.repository.TaskRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Fake in-memory implementation of [TaskRepository] for use case testing.
 */
class FakeTaskRepository : TaskRepository {
    private val store = mutableMapOf<String, Task>()
    private val _flow = MutableStateFlow<Map<String, Task>>(emptyMap())

    fun seed(vararg tasks: Task) {
        tasks.forEach { store[it.id] = it }
        _flow.value = store.toMap()
    }

    override fun getTasks(statusFilter: TaskStatus?): Flow<List<Task>> =
        _flow.map { it.values.toList().let { all -> if (statusFilter != null) all.filter { t -> t.status == statusFilter } else all } }

    override fun getTaskById(id: String): Flow<Task?> = _flow.map { it[id] }

    override suspend fun upsertTask(task: Task) {
        store[task.id] = task
        _flow.value = store.toMap()
    }

    override suspend fun updateTaskStatus(id: String, status: TaskStatus) {
        val existing = store[id] ?: throw IllegalArgumentException("No task with id: $id")
        store[id] = existing.copy(status = status)
        _flow.value = store.toMap()
    }

    override suspend fun deleteTask(id: String) {
        store.remove(id)
        _flow.value = store.toMap()
    }
}

private fun makeTask(id: String, status: TaskStatus = TaskStatus.TODO, priority: TaskPriority = TaskPriority.MEDIUM) =
    Task(id = id, title = "Task $id", description = "", status = status, priority = priority, subtasks = emptyList())

// --- GetTasksUseCase Tests ---

class GetTasksUseCaseTest {

    private lateinit var repository: FakeTaskRepository
    private lateinit var useCase: GetTasksUseCase

    @BeforeTest
    fun setUp() {
        repository = FakeTaskRepository()
        useCase = GetTasksUseCase(repository)
        repository.seed(
            makeTask("1", TaskStatus.TODO),
            makeTask("2", TaskStatus.IN_PROGRESS),
            makeTask("3", TaskStatus.DONE),
            makeTask("4", TaskStatus.BLOCKED),
        )
    }

    @Test
    fun invoke_noFilter_shouldReturnAllTasks() = runTest {
        useCase().test {
            val tasks = awaitItem()
            assertEquals(4, tasks.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun invoke_withTodoFilter_shouldReturnOnlyTodoTasks() = runTest {
        useCase(statusFilter = TaskStatus.TODO).test {
            val tasks = awaitItem()
            assertEquals(1, tasks.size)
            assertTrue(tasks.all { it.status == TaskStatus.TODO })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun invoke_withDoneFilter_shouldReturnOnlyDoneTasks() = runTest {
        useCase(statusFilter = TaskStatus.DONE).test {
            val tasks = awaitItem()
            assertTrue(tasks.all { it.status == TaskStatus.DONE })
            cancelAndIgnoreRemainingEvents()
        }
    }
}

// --- UpdateTaskStatusUseCase Tests ---

class UpdateTaskStatusUseCaseTest {

    private lateinit var repository: FakeTaskRepository
    private lateinit var useCase: UpdateTaskStatusUseCase

    @BeforeTest
    fun setUp() {
        repository = FakeTaskRepository()
        useCase = UpdateTaskStatusUseCase(repository)
        repository.seed(makeTask("t1", TaskStatus.TODO))
    }

    @Test
    fun invoke_shouldUpdateStatusInRepository() = runTest {
        useCase(id = "t1", status = TaskStatus.IN_PROGRESS)

        repository.getTaskById("t1").test {
            val task = awaitItem()
            assertNotNull(task)
            assertEquals(TaskStatus.IN_PROGRESS, task.status)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun invoke_withDoneStatus_taskShouldBeCompleted() = runTest {
        useCase(id = "t1", status = TaskStatus.DONE)

        repository.getTaskById("t1").test {
            val task = awaitItem()
            assertNotNull(task)
            assertTrue(task.isCompleted)
            cancelAndIgnoreRemainingEvents()
        }
    }
}

// --- SaveTaskUseCase Tests ---

class SaveTaskUseCaseTest {

    private lateinit var repository: FakeTaskRepository
    private lateinit var useCase: SaveTaskUseCase

    @BeforeTest
    fun setUp() {
        repository = FakeTaskRepository()
        useCase = SaveTaskUseCase(repository)
    }

    @Test
    fun invoke_newTask_shouldBeRetrievable() = runTest {
        val newTask = makeTask("new1", TaskStatus.TODO, TaskPriority.HIGH)
        useCase(newTask)

        repository.getTaskById("new1").test {
            val task = awaitItem()
            assertNotNull(task)
            assertEquals("new1", task.id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun invoke_existingTask_shouldOverwritePrevious() = runTest {
        val original = makeTask("existing", TaskStatus.TODO)
        repository.seed(original)

        val updated = original.copy(title = "Updated Title", status = TaskStatus.IN_PROGRESS)
        useCase(updated)

        repository.getTaskById("existing").test {
            val task = awaitItem()
            assertNotNull(task)
            assertEquals("Updated Title", task.title)
            assertEquals(TaskStatus.IN_PROGRESS, task.status)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun invoke_taskWithSubtasks_shouldPreserveSubtasks() = runTest {
        val task = Task(
            id = "with_subtasks",
            title = "Task with subtasks",
            description = "Has subtasks",
            status = TaskStatus.IN_PROGRESS,
            priority = TaskPriority.HIGH,
            subtasks = listOf(
                Subtask(id = "s1", title = "Step 1", isCompleted = true),
                Subtask(id = "s2", title = "Step 2", isCompleted = false),
            )
        )
        useCase(task)

        repository.getTaskById("with_subtasks").test {
            val retrieved = awaitItem()
            assertNotNull(retrieved)
            assertEquals(2, retrieved.subtasks.size)
            assertEquals(1, retrieved.completedSubtasksCount)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
