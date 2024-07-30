package org.lemb.tasktrack.di

fun coreModule() = listOf(
    useCaseModule,
    repositoryModule,
    dataSourceModule,
    platformModule()
)
