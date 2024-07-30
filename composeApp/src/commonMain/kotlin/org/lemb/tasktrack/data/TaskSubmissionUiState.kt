package org.lemb.tasktrack.data


data class TaskSubmissionUiState(
        /** Task name */
        val task: String = "",
        /** Subtask in the task */
        val subtask: String = "",
        /** Selected date for pickup (such as "Jan 1") */
        val date: String = "",
        /** Available pickup dates for the task*/
        val pickupOptions: List<String> = listOf(),
        /** Available tasks*/
        val availableTasksOptions: List<String> = listOf(),
        /** Available subtasks for the task*/
        val subtasksOptions: List<String> = listOf()
)
