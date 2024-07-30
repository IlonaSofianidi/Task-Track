package org.lemb.tasktrack.di

import org.lemb.tasktrack.repository.TasksRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::TasksRepository)
}
