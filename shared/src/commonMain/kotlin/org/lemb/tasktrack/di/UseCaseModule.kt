package org.lemb.tasktrack.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.lemb.tasktrack.usecase.GetSubtasksUseCase
import org.lemb.tasktrack.usecase.GetTasksUseCase

val useCaseModule = module {
    factoryOf(::GetTasksUseCase)
    factoryOf(::GetSubtasksUseCase)
}
