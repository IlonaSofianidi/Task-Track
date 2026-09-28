package org.lemb.tasktrack.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.lemb.tasktrack.domain.model.Task
import org.lemb.tasktrack.domain.model.TaskStatus
import org.lemb.tasktrack.domain.repository.TaskRepository

/**
 * Retrieves a reactive list of tasks, with optional status filtering.
 *
 * @param repository The domain [TaskRepository] to source tasks from.
 */
class GetTasksUseCase(
    private val repository: TaskRepository
) {
    /**
     * Returns a [Flow] of tasks, optionally filtered by [statusFilter].
     *
     * @param statusFilter When non-null, only tasks with this [TaskStatus] are emitted.
     */
    operator fun invoke(statusFilter: TaskStatus? = null): Flow<List<Task>> {
        return repository.getTasks(statusFilter = statusFilter)
    }
}
