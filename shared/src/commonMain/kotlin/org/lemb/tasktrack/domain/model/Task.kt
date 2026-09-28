package org.lemb.tasktrack.domain.model

/**
 * Core domain entity representing a task in the TaskTrack system.
 *
 * @property id Unique identifier for this task.
 * @property title Short title describing the task.
 * @property description Full description of the task.
 * @property status Current lifecycle status of the task.
 * @property priority Priority level for the task.
 * @property subtasks List of subtasks belonging to this task.
 */
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val priority: TaskPriority,
    val subtasks: List<Subtask>
) {
    /** Number of subtasks that have been marked as completed. */
    val completedSubtasksCount: Int get() = subtasks.count { it.isCompleted }

    /** True if this task's status is [TaskStatus.DONE]. */
    val isCompleted: Boolean get() = status.isDone
}
