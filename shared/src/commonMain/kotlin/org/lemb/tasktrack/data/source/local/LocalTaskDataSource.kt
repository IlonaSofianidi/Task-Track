package org.lemb.tasktrack.data.source.local

import co.touchlab.stately.collections.ConcurrentMutableMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.lemb.tasktrack.domain.model.Subtask
import org.lemb.tasktrack.domain.model.Task
import org.lemb.tasktrack.domain.model.TaskPriority
import org.lemb.tasktrack.domain.model.TaskStatus

class LocalTaskDataSource {

    private val store = ConcurrentMutableMap<String, Task>()
    private val _tasksFlow = MutableStateFlow<Map<String, Task>>(emptyMap())

    init {
        val sampleTasks = listOf(
            Task(
                id = "t1",
                title = "Set up Clean Architecture",
                description = "Establish domain, data, and presentation layers.",
                status = TaskStatus.TODO,
                priority = TaskPriority.HIGH,
                subtasks = listOf(
                    Subtask(id = "t1s1", title = "Define domain models", isCompleted = false),
                    Subtask(id = "t1s2", title = "Create repository interfaces", isCompleted = false)
                )
            ),
            Task(
                id = "t2",
                title = "Implement Compose UI components",
                description = "Build reusable components.",
                status = TaskStatus.IN_PROGRESS,
                priority = TaskPriority.HIGH,
                subtasks = listOf(
                    Subtask(id = "t2s1", title = "StatusBadge", isCompleted = true),
                )
            ),
            Task(
                id = "t3",
                title = "Integrate navigation",
                description = "Wire screens into NavHost.",
                status = TaskStatus.DONE,
                priority = TaskPriority.LOW,
                subtasks = listOf()
            )
        )
        sampleTasks.forEach { store[it.id] = it }
        _tasksFlow.value = store.toMap()
    }

    fun observeTasks(): Flow<List<Task>> = _tasksFlow.map { it.values.toList() }

    fun observeTaskById(id: String): Flow<Task?> = _tasksFlow.map { it[id] }

    fun upsertTask(task: Task) {
        store[task.id] = task
        _tasksFlow.update { store.toMap() }
    }

    fun updateTaskStatus(id: String, status: TaskStatus) {
        val existing = store[id] ?: throw IllegalArgumentException("No task found with id: $id")
        store[id] = existing.copy(status = status)
        _tasksFlow.update { store.toMap() }
    }

    fun deleteTask(id: String) {
        store.remove(id)
        _tasksFlow.update { store.toMap() }
    }
}
