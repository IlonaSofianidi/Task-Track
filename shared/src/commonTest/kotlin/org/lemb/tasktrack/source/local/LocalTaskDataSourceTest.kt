package org.lemb.tasktrack.source.local

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalTaskDataSourceTest {

    private lateinit var localTaskDataSource: LocalTaskDataSource

    @BeforeTest
    fun setUp() {
        localTaskDataSource = LocalTaskDataSource()
    }

    /**
     * Given tasks in local task data source, When getAllTasks is called, Then not empty list is returned
     */
    @Test
    fun getAllTasksShouldNotBeEmpty() = runTest {
        val expected = listOf(
            "Complete project report",
            "Prepare for client meeting",
            "Follow up with team on progress",
        )

        localTaskDataSource.getAllTasks().test {
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }
}