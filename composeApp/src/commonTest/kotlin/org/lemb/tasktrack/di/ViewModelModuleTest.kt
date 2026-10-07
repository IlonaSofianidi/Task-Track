package org.lemb.tasktrack.di

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.lemb.tasktrack.ui.viewmodel.TaskSubmissionViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelModuleTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        startKoin {
            modules(
                coreModule() + listOf(viewModelModule)
            )
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun taskSubmissionViewModel_shouldBeResolvableFromKoin() = runTest {
        val koin = GlobalContext.get()
        val viewModel = koin.getOrNull<TaskSubmissionViewModel>()
        assertNotNull(viewModel, "TaskSubmissionViewModel should be resolvable from Koin graph")
    }

    @Test
    fun taskSubmissionViewModel_shouldProvideNewInstancesAsFactory() = runTest {
        val koin = GlobalContext.get()
        val vm1 = koin.get<TaskSubmissionViewModel>()
        val vm2 = koin.get<TaskSubmissionViewModel>()
        assertNotEquals(vm1, vm2, "TaskSubmissionViewModel should be provided as factory/new instance")
    }
}
