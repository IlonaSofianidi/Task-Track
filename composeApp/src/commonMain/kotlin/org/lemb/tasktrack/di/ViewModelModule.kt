package org.lemb.tasktrack.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.lemb.tasktrack.ui.viewmodel.TaskSubmissionViewModel

val viewModelModule = module {
    factoryOf(::TaskSubmissionViewModel)
}
