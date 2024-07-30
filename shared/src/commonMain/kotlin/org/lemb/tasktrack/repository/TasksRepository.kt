package org.lemb.tasktrack.repository

import kotlinx.coroutines.flow.Flow
import org.lemb.tasktrack.source.local.LocalTaskDataSource

class TasksRepository(
        private val localTaskDataSource: LocalTaskDataSource
) {

    fun observeAllTasks(): Flow<List<String>> {
        return localTaskDataSource.getAllTasks()
    }

    fun observeAllSubtasks(): Flow<List<String>> {
        return localTaskDataSource.getAllSubtasks()
    }
}
