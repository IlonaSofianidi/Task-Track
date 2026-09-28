package org.lemb.tasktrack.domain.model

/**
 * Represents a sub-item of a [Task], with its own completion state.
 *
 * @property id Unique identifier for this subtask.
 * @property title Short description of what this subtask entails.
 * @property isCompleted Whether this subtask has been finished.
 */
data class Subtask(
    val id: String,
    val title: String,
    val isCompleted: Boolean
)
