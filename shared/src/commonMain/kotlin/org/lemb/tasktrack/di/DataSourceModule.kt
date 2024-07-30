package org.lemb.tasktrack.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.lemb.tasktrack.source.local.LocalTaskDataSource

val dataSourceModule = module {
    singleOf(::LocalTaskDataSource)
}