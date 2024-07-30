package org.lemb.tasktrack.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.lemb.tasktrack.data.TaskSubmissionUiState
import tasktrack.composeapp.generated.resources.Res
import tasktrack.composeapp.generated.resources.cancel
import tasktrack.composeapp.generated.resources.new_submission
import tasktrack.composeapp.generated.resources.pickup_date
import tasktrack.composeapp.generated.resources.submission_details
import tasktrack.composeapp.generated.resources.submit
import tasktrack.composeapp.generated.resources.subtask
import tasktrack.composeapp.generated.resources.task


@Composable
fun TaskSubmissionSummaryScreen(
        taskSubmissionUiState: TaskSubmissionUiState,
        onCancelButtonClicked: () -> Unit,
        onSendButtonClicked: (String, String) -> Unit,
        modifier: Modifier = Modifier
) {
    val submissionSummary = stringResource(
        Res.string.submission_details,
        taskSubmissionUiState.task,
        taskSubmissionUiState.subtask,
        taskSubmissionUiState.date
    )
    val newSubmission = stringResource(Res.string.new_submission)
    val items = listOf(
        Pair(stringResource(Res.string.task), taskSubmissionUiState.task),
        Pair(stringResource(Res.string.subtask), taskSubmissionUiState.subtask),
        Pair(stringResource(Res.string.pickup_date), taskSubmissionUiState.date)
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                Text(item.first.uppercase())
                Text(text = item.second, fontWeight = FontWeight.Bold)
                HorizontalDivider(thickness = 1.dp)
            }
        }
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onSendButtonClicked(newSubmission, submissionSummary) }
                ) {
                    Text(stringResource(Res.string.submit))
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCancelButtonClicked
                ) {
                    Text(stringResource(Res.string.cancel))
                }
            }
        }
    }
}