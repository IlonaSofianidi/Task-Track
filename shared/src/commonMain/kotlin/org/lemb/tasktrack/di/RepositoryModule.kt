package org.lemb.tasktrack.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.lemb.tasktrack.data.repository.DefaultTaskRepository
import org.lemb.tasktrack.domain.repository.TaskRepository

/**
 * Koin module providing the [TaskRepository] implementation.
 *
 * [DefaultTaskRepository] is bound to the [TaskRepository] interface so that
 * the domain and presentation layers depend only on the abstraction.
 */
val repositoryModule = module {
    singleOf(::DefaultTaskRepository) bind TaskRepository::class
}
