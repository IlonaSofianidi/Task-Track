package org.lemb.tasktrack.di

import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.error.NoBeanDefFoundException
import org.lemb.tasktrack.domain.repository.TaskRepository
import org.lemb.tasktrack.domain.usecase.GetTasksUseCase
import org.lemb.tasktrack.domain.usecase.SaveTaskUseCase
import org.lemb.tasktrack.domain.usecase.UpdateTaskStatusUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertFailsWith

class KoinDiModuleTest {

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(coreModule())
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun taskRepository_shouldBeResolvable() = runTest {
        val koin = org.koin.core.context.GlobalContext.get()
        val repository = koin.getOrNull<TaskRepository>()
        assertNotNull(repository, "TaskRepository should be resolvable from the Koin graph")
    }

    @Test
    fun getTasksUseCase_shouldBeResolvable() = runTest {
        val koin = org.koin.core.context.GlobalContext.get()
        val useCase = koin.getOrNull<GetTasksUseCase>()
        assertNotNull(useCase, "GetTasksUseCase should be resolvable from the Koin graph")
    }

    @Test
    fun updateTaskStatusUseCase_shouldBeResolvable() = runTest {
        val koin = org.koin.core.context.GlobalContext.get()
        val useCase = koin.getOrNull<UpdateTaskStatusUseCase>()
        assertNotNull(useCase, "UpdateTaskStatusUseCase should be resolvable from the Koin graph")
    }

    @Test
    fun saveTaskUseCase_shouldBeResolvable() = runTest {
        val koin = org.koin.core.context.GlobalContext.get()
        val useCase = koin.getOrNull<SaveTaskUseCase>()
        assertNotNull(useCase, "SaveTaskUseCase should be resolvable from the Koin graph")
    }
}
