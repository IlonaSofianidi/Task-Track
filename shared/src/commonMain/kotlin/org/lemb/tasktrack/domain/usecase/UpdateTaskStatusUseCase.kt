package org.lemb.tasktrack.domain.usecase

import org.lemb.tasktrack.domain.model.TaskStatus
import org.lemb.tasktrack.domain.repository.TaskRepository

/**
 * Updates the [TaskStatus] of a specific task.
 *
 * @param repository The domain [TaskRepository] to update the task status in.
 */
class UpdateTaskStatusUseCase(
    private val repository: TaskRepository
) {
    /**
     * Updates the status of the task identified by [id] to [status].
     *
     * @throws IllegalArgumentException if no task with [id] exists.
     */
    suspend operator fun invoke(id: String, status: TaskStatus) {
        repository.updateTaskStatus(id = id, status = status)
    }
}
