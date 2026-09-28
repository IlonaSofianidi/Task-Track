package org.lemb.tasktrack.domain.usecase

import org.lemb.tasktrack.domain.model.Task
import org.lemb.tasktrack.domain.repository.TaskRepository

/**
 * Creates a new task or updates an existing one (upsert semantics).
 *
 * @param repository The domain [TaskRepository] to save the task to.
 */
class SaveTaskUseCase(
    private val repository: TaskRepository
) {
    /**
     * Persists the given [task]. If a task with the same [Task.id] already exists,
     * it will be replaced entirely.
     */
    suspend operator fun invoke(task: Task) {
        repository.upsertTask(task)
    }
}
