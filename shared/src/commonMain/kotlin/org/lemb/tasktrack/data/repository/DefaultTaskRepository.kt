package org.lemb.tasktrack.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.lemb.tasktrack.data.source.local.LocalTaskDataSource
import org.lemb.tasktrack.domain.model.Task
import org.lemb.tasktrack.domain.model.TaskStatus
import org.lemb.tasktrack.domain.repository.TaskRepository

class DefaultTaskRepository(
    private val localTaskDataSource: LocalTaskDataSource
) : TaskRepository {

    override fun getTasks(statusFilter: TaskStatus?): Flow<List<Task>> {
        return localTaskDataSource.observeTasks().map { tasks ->
            if (statusFilter != null) {
                tasks.filter { it.status == statusFilter }
            } else {
                tasks
            }
        }
    }

    override fun getTaskById(id: String): Flow<Task?> {
        return localTaskDataSource.observeTaskById(id)
    }

    override suspend fun upsertTask(task: Task) {
        localTaskDataSource.upsertTask(task)
    }

    override suspend fun updateTaskStatus(id: String, status: TaskStatus) {
        localTaskDataSource.updateTaskStatus(id, status)
    }

    override suspend fun deleteTask(id: String) {
        localTaskDataSource.deleteTask(id)
    }
}
