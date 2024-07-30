package org.lemb.tasktrack.model

data class Task(
        val id: String,
        val title: String,
        val content: String,
)

fun TaskResource.toTask(): Task {
    return Task(
        title = title,
        content = content,
        id = id ?: "-1",
    )
}
