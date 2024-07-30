package org.lemb.tasktrack.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.lemb.tasktrack.repository.TasksRepository

class GetSubtasksUseCase(
        private val repository: TasksRepository
) {
    suspend operator fun invoke(): Flow<List<String>> {
        return withContext(Dispatchers.Default) {
            repository.observeAllSubtasks()
        }
    }
}
