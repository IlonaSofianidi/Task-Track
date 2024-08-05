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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lemb.tasktrack.data.TaskSubmissionUiState
import org.lemb.tasktrack.usecase.GetSubtasksUseCase
import org.lemb.tasktrack.usecase.GetTasksUseCase


//TODO() ADD multiplatform view model koin injection
class TaskSubmissionViewModel : ViewModel(), KoinComponent {

    private val getTasksUseCase: GetTasksUseCase by inject()
    private val getSubtasksUseCase: GetSubtasksUseCase by inject()

    private val _uiState = MutableStateFlow(TaskSubmissionUiState(pickupOptions = pickupOptions()))
    val uiState: StateFlow<TaskSubmissionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getTasksUseCase.invoke().collect { tasks ->
                _uiState.update { currentState ->
                    currentState.copy(
                        availableTasksOptions = tasks
                    )
                }
            }

            getSubtasksUseCase.invoke().collect { subtasks ->
                _uiState.update { currentState ->
                    currentState.copy(
                        subtasksOptions = subtasks
                    )
                }
            }
        }
    }

    fun setAvailableTask(availableTask: String) {
        _uiState.update { currentState ->
            currentState.copy(
                task = availableTask
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
