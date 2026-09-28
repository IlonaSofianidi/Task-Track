package org.lemb.tasktrack.domain.model

/**
 * Represents the lifecycle stage of a task.
 */
enum class TaskStatus {
    TODO,
    IN_PROGRESS,
    BLOCKED,
    DONE;

    /** Returns true if this status represents a completed task. */
    val isDone: Boolean get() = this == DONE
}
