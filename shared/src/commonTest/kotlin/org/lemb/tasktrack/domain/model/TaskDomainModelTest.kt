package org.lemb.tasktrack.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TaskDomainModelTest {

    // --- TaskStatus Tests ---

    @Test
    fun taskStatus_shouldHaveAllFourStages() {
        val statuses = TaskStatus.entries
        assertEquals(4, statuses.size, "TaskStatus must have exactly 4 stages")
        assertTrue(statuses.contains(TaskStatus.TODO))
        assertTrue(statuses.contains(TaskStatus.IN_PROGRESS))
        assertTrue(statuses.contains(TaskStatus.BLOCKED))
        assertTrue(statuses.contains(TaskStatus.DONE))
    }

    @Test
    fun taskStatus_isDone_shouldReturnTrueForDoneOnly() {
        assertTrue(TaskStatus.DONE.isDone)
        assertFalse(TaskStatus.TODO.isDone)
        assertFalse(TaskStatus.IN_PROGRESS.isDone)
        assertFalse(TaskStatus.BLOCKED.isDone)
    }

    // --- TaskPriority Tests ---

    @Test
    fun taskPriority_shouldHaveThreeLevels() {
        val priorities = TaskPriority.entries
        assertEquals(3, priorities.size, "TaskPriority must have exactly 3 levels")
        assertTrue(priorities.contains(TaskPriority.LOW))
        assertTrue(priorities.contains(TaskPriority.MEDIUM))
        assertTrue(priorities.contains(TaskPriority.HIGH))
    }

    // --- Subtask Tests ---

    @Test
    fun subtask_shouldBeCreatedWithCorrectFields() {
        val subtask = Subtask(id = "s1", title = "Research data", isCompleted = false)
        assertEquals("s1", subtask.id)
        assertEquals("Research data", subtask.title)
        assertFalse(subtask.isCompleted)
    }

    @Test
    fun subtask_copy_shouldAllowTogglingCompletion() {
        val subtask = Subtask(id = "s2", title = "Write tests", isCompleted = false)
        val completedSubtask = subtask.copy(isCompleted = true)
        assertTrue(completedSubtask.isCompleted)
        assertEquals(subtask.id, completedSubtask.id)
    }

    // --- Task Tests ---

    @Test
    fun task_shouldBeCreatedWithRequiredFields() {
        val task = Task(
            id = "t1",
            title = "Build feature",
            description = "A detailed description",
            status = TaskStatus.TODO,
            priority = TaskPriority.MEDIUM,
            subtasks = emptyList()
        )
        assertEquals("t1", task.id)
        assertEquals("Build feature", task.title)
        assertEquals("A detailed description", task.description)
        assertEquals(TaskStatus.TODO, task.status)
        assertEquals(TaskPriority.MEDIUM, task.priority)
        assertTrue(task.subtasks.isEmpty())
    }

    @Test
    fun task_completedSubtasksCount_shouldCountOnlyCompleted() {
        val subtasks = listOf(
            Subtask(id = "s1", title = "Sub 1", isCompleted = true),
            Subtask(id = "s2", title = "Sub 2", isCompleted = false),
            Subtask(id = "s3", title = "Sub 3", isCompleted = true),
        )
        val task = Task(
            id = "t2",
            title = "Task with subtasks",
            description = "",
            status = TaskStatus.IN_PROGRESS,
            priority = TaskPriority.HIGH,
            subtasks = subtasks
        )
        assertEquals(2, task.completedSubtasksCount)
    }

    @Test
    fun task_isCompleted_shouldBeTrueWhenStatusIsDone() {
        val task = Task(
            id = "t3",
            title = "Done task",
            description = "",
            status = TaskStatus.DONE,
            priority = TaskPriority.LOW,
            subtasks = emptyList()
        )
        assertTrue(task.isCompleted)
    }

    @Test
    fun task_isCompleted_shouldBeFalseWhenStatusIsNotDone() {
        val task = Task(
            id = "t4",
            title = "Not done task",
            description = "",
            status = TaskStatus.IN_PROGRESS,
            priority = TaskPriority.LOW,
            subtasks = emptyList()
        )
        assertFalse(task.isCompleted)
    }

    @Test
    fun task_copy_shouldAllowStatusTransition() {
        val task = Task(
            id = "t5",
            title = "Transition test",
            description = "",
            status = TaskStatus.TODO,
            priority = TaskPriority.MEDIUM,
            subtasks = emptyList()
        )
        val updated = task.copy(status = TaskStatus.IN_PROGRESS)
        assertEquals(TaskStatus.IN_PROGRESS, updated.status)
        assertEquals(task.id, updated.id)
    }
}
