package org.lemb.tasktrack.domain.repository

import kotlinx.coroutines.flow.Flow
import org.lemb.tasktrack.domain.model.Task
import org.lemb.tasktrack.domain.model.TaskStatus

/**
 * Domain contract for accessing and modifying tasks.
 *
 * All implementations must be reactive (Flow-based) and support offline-first access.
 */
interface TaskRepository {

    /**
     * Returns a [Flow] that emits the current list of all tasks, and re-emits on any change.
     *
     * @param statusFilter If non-null, only tasks with the specified [TaskStatus] will be emitted.
     */
    fun getTasks(statusFilter: TaskStatus? = null): Flow<List<Task>>

    /**
     * Returns a [Flow] that emits the task with the given [id], or null if it does not exist.
     */
    fun getTaskById(id: String): Flow<Task?>

    /**
     * Creates a new task or updates an existing one (upsert by [Task.id]).
     */
    suspend fun upsertTask(task: Task)

    /**
     * Updates the [TaskStatus] of the task with the given [id].
     *
     * @throws IllegalArgumentException if no task with [id] exists.
     */
    suspend fun updateTaskStatus(id: String, status: TaskStatus)

    /**
     * Permanently deletes the task with the given [id].
     */
    suspend fun deleteTask(id: String)
}
