package org.lemb.tasktrack.source.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class LocalTaskDataSource {

    private val mockedTasks = listOf(
        "Complete project report",
        "Prepare for client meeting",
        "Follow up with team on progress",
    )

    private val mockedSubtasks = listOf(
        "Research relevant data",
        "Review and edit draft",
        "Schedule check-in meeting",
        "Provide feedback"
    )

    fun getAllTasks(): Flow<List<String>> {
        return flowOf(mockedTasks)
    }

    fun getAllSubtasks(): Flow<List<String>> {
        return flowOf(mockedSubtasks)
    }
}