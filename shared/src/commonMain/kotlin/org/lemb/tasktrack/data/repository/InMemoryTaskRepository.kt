package org.lemb.tasktrack.data.repository

import co.touchlab.stately.collections.ConcurrentMutableMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.lemb.tasktrack.domain.model.Subtask
import org.lemb.tasktrack.domain.model.Task
import org.lemb.tasktrack.domain.model.TaskPriority
import org.lemb.tasktrack.domain.model.TaskStatus
import org.lemb.tasktrack.domain.repository.TaskRepository

/**
 * An in-memory, reactive implementation of [TaskRepository].
 *
 * Uses a [MutableStateFlow] backed by a [ConcurrentMutableMap] for thread-safe
 * mutations and reactive emissions. Suitable for offline-first development and
 * testing without a local database.
 */
class InMemoryTaskRepository : TaskRepository {

    /** Thread-safe backing store for all tasks keyed by their ID. */
    private val store = ConcurrentMutableMap<String, Task>()

    /** StateFlow that emits the current snapshot of all tasks on every mutation. */
    private val _tasksFlow = MutableStateFlow<Map<String, Task>>(emptyMap())

    init {
        // Seed with realistic sample data
        val sampleTasks = listOf(
            Task(
                id = "t1",
                title = "Set up Clean Architecture",
                description = "Establish domain, data, and presentation layers with proper separation of concerns.",
                status = TaskStatus.TODO,
                priority = TaskPriority.HIGH,
                subtasks = listOf(
                    Subtask(id = "t1s1", title = "Define domain models", isCompleted = false),
                    Subtask(id = "t1s2", title = "Create repository interfaces", isCompleted = false),
                    Subtask(id = "t1s3", title = "Configure Koin DI modules", isCompleted = false),
                )
            ),
            Task(
                id = "t2",
                title = "Implement Compose UI components",
                description = "Build reusable, stateless Composable components following Material 3 guidelines.",
                status = TaskStatus.IN_PROGRESS,
                priority = TaskPriority.HIGH,
                subtasks = listOf(
                    Subtask(id = "t2s1", title = "Create StatusBadge composable", isCompleted = true),
                    Subtask(id = "t2s2", title = "Create TaskCard composable", isCompleted = false),
                    Subtask(id = "t2s3", title = "Create TaskStatusFilterBar", isCompleted = false),
                )
            ),
            Task(
                id = "t3",
                title = "Write unit tests for use cases",
                description = "Apply TDD: write tests before implementing each use case.",
                status = TaskStatus.BLOCKED,
                priority = TaskPriority.MEDIUM,
                subtasks = listOf(
                    Subtask(id = "t3s1", title = "Tests for GetTasksUseCase", isCompleted = false),
                    Subtask(id = "t3s2", title = "Tests for UpdateTaskStatusUseCase", isCompleted = false),
                )
            ),
            Task(
                id = "t4",
                title = "Integrate navigation graph",
                description = "Wire all screens into the NavHost and configure route transitions.",
                status = TaskStatus.DONE,
                priority = TaskPriority.LOW,
                subtasks = listOf(
                    Subtask(id = "t4s1", title = "Define routes", isCompleted = true),
                    Subtask(id = "t4s2", title = "Connect TaskListScreen", isCompleted = true),
                )
            ),
        )
        sampleTasks.forEach { store[it.id] = it }
        _tasksFlow.value = store.toMap()
    }

    override fun getTasks(statusFilter: TaskStatus?): Flow<List<Task>> {
        return _tasksFlow.map { snapshot ->
            val all = snapshot.values.toList()
            if (statusFilter != null) all.filter { it.status == statusFilter } else all
        }
    }

    override fun getTaskById(id: String): Flow<Task?> {
        return _tasksFlow.map { snapshot -> snapshot[id] }
    }

    override suspend fun upsertTask(task: Task) {
        store[task.id] = task
        _tasksFlow.update { store.toMap() }
    }

    override suspend fun updateTaskStatus(id: String, status: TaskStatus) {
        val existing = store[id]
            ?: throw IllegalArgumentException("No task found with id: $id")
        val updated = existing.copy(status = status)
        store[id] = updated
        _tasksFlow.update { store.toMap() }
    }

    override suspend fun deleteTask(id: String) {
        store.remove(id)
        _tasksFlow.update { store.toMap() }
    }
}
