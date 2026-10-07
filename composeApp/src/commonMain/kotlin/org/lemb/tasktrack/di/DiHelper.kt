package org.lemb.tasktrack.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}): KoinApplication {

    return startKoin {
        appDeclaration()
        modules(
            coreModule() + listOf(viewModelModule)
        )
    }
}

// For iOS
fun initKoin() = initKoin {}