package org.lemb.tasktrack.repository.util.mapping

import org.lemb.tasktrack.model.Task
import org.lemb.tasktrack.model.TaskResource


internal fun List<TaskResource>.toModelData(): List<Task> = this.map { it.toModelData() }

internal fun TaskResource.toModelData(): Task = Task(
    id = this.id,
    title = this.title,
    content = this.content
)