package org.lemb.tasktrack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.lemb.tasktrack.data.TaskSubmissionUiState
import org.lemb.tasktrack.domain.model.Task
import org.lemb.tasktrack.domain.usecase.GetTasksUseCase

class TaskSubmissionViewModel(
    private val getTasksUseCase: GetTasksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskSubmissionUiState(pickupOptions = pickupOptions()))
    val uiState: StateFlow<TaskSubmissionUiState> = _uiState.asStateFlow()

    private var currentTasks: List<Task> = emptyList()

    init {
        viewModelScope.launch {
            getTasksUseCase().collect { tasks ->
                currentTasks = tasks
                _uiState.update { currentState ->
                    currentState.copy(
                        availableTasksOptions = tasks.map { it.title },
                        subtasksOptions = tasks.flatMap { it.subtasks.map { subtask -> subtask.title } }
                    )
                }
            }
        }
    }

    fun setAvailableTask(availableTask: String) {
        val selectedTask = currentTasks.find { it.title == availableTask }
        val subtaskOptions = selectedTask?.subtasks?.map { it.title } ?: emptyList()
        _uiState.update { currentState ->
            currentState.copy(
                task = availableTask,
                subtasksOptions = if (subtaskOptions.isNotEmpty()) subtaskOptions else currentState.subtasksOptions
            )
        }
    }

    fun setSubtask(desiredSubtask: String) {
        _uiState.update { currentState ->
            currentState.copy(subtask = desiredSubtask)
        }
    }

    fun setDate(pickupDate: String) {
        _uiState.update { currentState ->
            currentState.copy(
                date = pickupDate,
            )
        }
    }

    fun resetTaskSubmission() {
        _uiState.update { currentState ->
            currentState.copy(
                pickupOptions = emptyList()
            )
        }
    }

    private fun pickupOptions(): List<String> {
        val dateOptions = mutableListOf<String>()
        val now = Clock.System.now()
        val timeZone = TimeZone.currentSystemDefault()
        // add current date and the following 3 dates.
        repeat(4) {
            val day = now.plus(it, DateTimeUnit.DAY, timeZone)
            dateOptions.add(day.toLocalDateTime(timeZone).date.toString())
        }
        return dateOptions
    }
}
