package org.lemb.tasktrack.ui.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.lemb.tasktrack.domain.model.Subtask
import org.lemb.tasktrack.domain.model.Task
import org.lemb.tasktrack.domain.model.TaskPriority
import org.lemb.tasktrack.domain.model.TaskStatus
import org.lemb.tasktrack.domain.repository.TaskRepository
import org.lemb.tasktrack.domain.usecase.GetTasksUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private class FakeTaskRepositoryForViewModel : TaskRepository {
    private val tasksFlow = MutableStateFlow<List<Task>>(emptyList())

    fun emitTasks(tasks: List<Task>) {
        tasksFlow.value = tasks
    }

    override fun getTasks(statusFilter: TaskStatus?): Flow<List<Task>> =
        tasksFlow.map { list ->
            if (statusFilter != null) list.filter { it.status == statusFilter } else list
        }

    override fun getTaskById(id: String): Flow<Task?> =
        tasksFlow.map { list -> list.find { it.id == id } }

    override suspend fun upsertTask(task: Task) {}
    override suspend fun updateTaskStatus(id: String, status: TaskStatus) {}
    override suspend fun deleteTask(id: String) {}
}

@OptIn(ExperimentalCoroutinesApi::class)
class TaskSubmissionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeTaskRepositoryForViewModel
    private lateinit var getTasksUseCase: GetTasksUseCase
    private lateinit var viewModel: TaskSubmissionViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeTaskRepositoryForViewModel()
        getTasksUseCase = GetTasksUseCase(fakeRepository)
        fakeRepository.emitTasks(
            listOf(
                Task(
                    id = "1",
                    title = "Prepare quarterly review",
                    description = "Collect metrics",
                    status = TaskStatus.TODO,
                    priority = TaskPriority.HIGH,
                    subtasks = listOf(
                        Subtask(id = "s1", title = "Gather analytics", isCompleted = false),
                        Subtask(id = "s2", title = "Create slides", isCompleted = false)
                    )
                ),
                Task(
                    id = "2",
                    title = "Design system sync",
                    description = "Review tokens",
                    status = TaskStatus.IN_PROGRESS,
                    priority = TaskPriority.MEDIUM,
                    subtasks = listOf(
                        Subtask(id = "s3", title = "Color palette check", isCompleted = true)
                    )
                )
            )
        )
        viewModel = TaskSubmissionViewModel(getTasksUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun uiState_shouldPopulateAvailableTasksFromUseCase() = runTest {
        testScheduler.advanceUntilIdle()
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.availableTasksOptions.contains("Prepare quarterly review"))
            assertTrue(state.availableTasksOptions.contains("Design system sync"))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun setAvailableTask_shouldUpdateTaskInUiState() = runTest {
        viewModel.setAvailableTask("Prepare quarterly review")
        assertEquals("Prepare quarterly review", viewModel.uiState.value.task)
    }

    @Test
    fun setSubtask_shouldUpdateSubtaskInUiState() = runTest {
        viewModel.setSubtask("Gather analytics")
        assertEquals("Gather analytics", viewModel.uiState.value.subtask)
    }

    @Test
    fun setDate_shouldUpdateDateInUiState() = runTest {
        viewModel.setDate("2026-10-15")
        assertEquals("2026-10-15", viewModel.uiState.value.date)
    }

    @Test
    fun resetTaskSubmission_shouldClearPickupOptions() = runTest {
        viewModel.resetTaskSubmission()
        assertTrue(viewModel.uiState.value.pickupOptions.isEmpty())
    }
}
