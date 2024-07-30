package org.lemb.tasktrack

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.lemb.tasktrack.ui.SelectOptionScreen
import org.lemb.tasktrack.ui.StartTaskScreen
import org.lemb.tasktrack.ui.TaskSubmissionSummaryScreen
import org.lemb.tasktrack.ui.viewmodel.TaskSubmissionViewModel
import tasktrack.composeapp.generated.resources.Res
import tasktrack.composeapp.generated.resources.add_task
import tasktrack.composeapp.generated.resources.app_name
import tasktrack.composeapp.generated.resources.back_button
import tasktrack.composeapp.generated.resources.choose_pickup_date
import tasktrack.composeapp.generated.resources.submission_summary


enum class TaskTrackScreen(val title: StringResource) {
    Start(title = Res.string.app_name),
    Subtask(title = Res.string.add_task),
    Pickup(title = Res.string.choose_pickup_date),
    Summary(title = Res.string.submission_summary)
}

@Composable
fun TaskTrackAppBar(
        currentScreen: TaskTrackScreen,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit,
        modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun TaskTrackApp(
        viewModel: TaskSubmissionViewModel = viewModel { TaskSubmissionViewModel() },
        navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = TaskTrackScreen.valueOf(
        backStackEntry?.destination?.route ?: TaskTrackScreen.Start.name
    )

    Scaffold(
        topBar = {
            TaskTrackAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = TaskTrackScreen.Start.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = TaskTrackScreen.Start.name) {
                StartTaskScreen(
                    availableTasksOptions = uiState.availableTasksOptions,
                    onNextButtonClicked = {
                        viewModel.setAvailableTask(it)
                        navController.navigate(TaskTrackScreen.Subtask.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
            composable(route = TaskTrackScreen.Subtask.name) {
                SelectOptionScreen(
                    onNextButtonClicked = { navController.navigate(TaskTrackScreen.Pickup.name) },
                    onCancelButtonClicked = {
                        cancelSubmissionAndNavigateToStart(viewModel, navController)
                    },
                    options = uiState.subtasksOptions,
                    onSelectionChanged = { viewModel.setSubtask(it) },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = TaskTrackScreen.Pickup.name) {
                SelectOptionScreen(
                    onNextButtonClicked = { navController.navigate(TaskTrackScreen.Summary.name) },
                    onCancelButtonClicked = {
                        cancelSubmissionAndNavigateToStart(viewModel, navController)
                    },
                    options = uiState.pickupOptions,
                    onSelectionChanged = { viewModel.setDate(it) },
                    modifier = Modifier.fillMaxHeight()
                )
            }
            composable(route = TaskTrackScreen.Summary.name) {
                TaskSubmissionSummaryScreen(
                    taskSubmissionUiState = uiState,
                    onCancelButtonClicked = {
                        cancelSubmissionAndNavigateToStart(viewModel, navController)
                    },
                    onSendButtonClicked = { subject: String, summary: String ->
                        submit(subject = subject, summary = summary)
                    },
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

private fun cancelSubmissionAndNavigateToStart(
        viewModel: TaskSubmissionViewModel,
        navController: NavHostController
) {
    viewModel.resetTaskSubmission()
    navController.popBackStack(TaskTrackScreen.Start.name, inclusive = false)
}

private fun submit(subject: String, summary: String) {
    // TODO
}
