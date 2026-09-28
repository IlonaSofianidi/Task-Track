package org.lemb.tasktrack.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.lemb.tasktrack.domain.usecase.GetTasksUseCase
import org.lemb.tasktrack.domain.usecase.SaveTaskUseCase
import org.lemb.tasktrack.domain.usecase.UpdateTaskStatusUseCase

/**
 * Koin module providing all domain use case factories.
 *
 * Use cases are scoped as factories so each requesting component gets a fresh instance,
 * keeping them stateless and independently testable.
 */
val useCaseModule = module {
    factoryOf(::GetTasksUseCase)
    factoryOf(::UpdateTaskStatusUseCase)
    factoryOf(::SaveTaskUseCase)
}
