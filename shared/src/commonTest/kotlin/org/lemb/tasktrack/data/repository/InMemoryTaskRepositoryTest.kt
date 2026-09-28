package org.lemb.tasktrack.data.repository

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.lemb.tasktrack.domain.model.Subtask
import org.lemb.tasktrack.domain.model.Task
import org.lemb.tasktrack.domain.model.TaskPriority
import org.lemb.tasktrack.domain.model.TaskStatus
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InMemoryTaskRepositoryTest {

    private lateinit var repository: InMemoryTaskRepository

    @BeforeTest
    fun setUp() {
        repository = InMemoryTaskRepository()
    }

    @Test
    fun getTasks_shouldReturnNonEmptySampleData() = runTest {
        repository.getTasks().test {
            val tasks = awaitItem()
            assertTrue(tasks.isNotEmpty(), "Initial sample tasks should not be empty")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getTasks_withStatusFilter_shouldReturnOnlyMatchingStatus() = runTest {
        repository.getTasks(statusFilter = TaskStatus.TODO).test {
            val tasks = awaitItem()
            assertTrue(tasks.all { it.status == TaskStatus.TODO }, "All tasks must be TODO")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getTaskById_existingId_shouldReturnTask() = runTest {
        val allTasks = mutableListOf<Task>()
        repository.getTasks().test {
            allTasks.addAll(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        val existingId = allTasks.first().id

        repository.getTaskById(existingId).test {
            val task = awaitItem()
            assertNotNull(task)
            assertEquals(existingId, task.id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getTaskById_nonExistingId_shouldReturnNull() = runTest {
        repository.getTaskById("non_existent_id").test {
            val task = awaitItem()
            assertNull(task)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun upsertTask_newTask_shouldAppearInGetTasks() = runTest {
        val newTask = Task(
            id = "test_new_task",
            title = "New Task",
            description = "Description",
            status = TaskStatus.TODO,
            priority = TaskPriority.HIGH,
            subtasks = emptyList()
        )

        repository.upsertTask(newTask)

        repository.getTasks().test {
            val tasks = awaitItem()
            assertTrue(tasks.any { it.id == newTask.id }, "New task should appear in task list")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun upsertTask_existingTask_shouldUpdateInPlace() = runTest {
        val allTasks = mutableListOf<Task>()
        repository.getTasks().test {
            allTasks.addAll(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        val original = allTasks.first()
        val updated = original.copy(title = "Updated Title")

        repository.upsertTask(updated)

        repository.getTaskById(original.id).test {
            val task = awaitItem()
            assertNotNull(task)
            assertEquals("Updated Title", task.title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateTaskStatus_shouldChangeStatusCorrectly() = runTest {
        val allTasks = mutableListOf<Task>()
        repository.getTasks().test {
            allTasks.addAll(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        val taskId = allTasks.first { it.status == TaskStatus.TODO }.id

        repository.updateTaskStatus(taskId, TaskStatus.IN_PROGRESS)

        repository.getTaskById(taskId).test {
            val task = awaitItem()
            assertNotNull(task)
            assertEquals(TaskStatus.IN_PROGRESS, task.status)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun deleteTask_shouldRemoveFromRepository() = runTest {
        val allTasks = mutableListOf<Task>()
        repository.getTasks().test {
            allTasks.addAll(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        val taskId = allTasks.first().id

        repository.deleteTask(taskId)

        repository.getTaskById(taskId).test {
            val task = awaitItem()
            assertNull(task, "Deleted task should no longer be found")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun getTasks_afterUpdate_shouldReEmitUpdatedList() = runTest {
        val newTask = Task(
            id = "reactive_test",
            title = "Reactive Task",
            description = "Test reactivity",
            status = TaskStatus.BLOCKED,
            priority = TaskPriority.LOW,
            subtasks = listOf(
                Subtask(id = "rs1", title = "Sub 1", isCompleted = true)
            )
        )

        repository.getTasks().test {
            val initial = awaitItem()
            val initialSize = initial.size

            repository.upsertTask(newTask)

            val updated = awaitItem()
            assertEquals(initialSize + 1, updated.size)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
