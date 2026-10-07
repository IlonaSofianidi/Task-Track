# Specification: Multiplatform ViewModel Dependency Injection with Koin

## 1. Overview
Refactor Compose Multiplatform presentation architecture to use constructor injection with Koin for ViewModels, eliminating the service locator anti-pattern (`KoinComponent` / `by inject()`) and integrating Koin Compose for lifecycle-aware ViewModel retrieval.

## 2. Functional Requirements
- **Constructor Injection:** Refactor `TaskSubmissionViewModel` to accept `GetTasksUseCase` via constructor parameter and remove `KoinComponent` interface implementation and `by inject()` delegation.
- **Remove Broken / Deprecated References:** Remove references to obsolete `GetSubtasksUseCase` and adapt to domain `GetTasksUseCase` returning `Flow<List<Task>>`.
- **ViewModel Module:** Define `viewModelModule` in `composeApp` (`org.lemb.tasktrack.di.ViewModelModule`) declaring factory / viewModel bindings.
- **Koin Initialization:** Register `viewModelModule` in `initKoin` in `composeApp/src/commonMain/kotlin/org/lemb/tasktrack/di/DiHelper.kt`.
- **Compose Integration:** Use Koin Compose (`koinViewModel()`) in `TaskTrackApp` to obtain lifecycle-aware ViewModels in Compose Multiplatform.

## 3. Non-Functional Requirements & Architecture
- **Dependency Management:** Ensure `composeApp` includes necessary Koin Compose dependencies.
- **Testability:** Enable unit testing of ViewModels without requiring global Koin container startup by passing mock or fake use cases directly into constructors.

## 4. Acceptance Criteria
- `composeApp` compiles cleanly without unresolved symbol errors.
- `TaskSubmissionViewModel` uses constructor injection with `GetTasksUseCase`.
- `initKoin()` successfully loads both `coreModule()` from `shared` and `viewModelModule` from `composeApp`.
- Unit tests verify `TaskSubmissionViewModel` behavior with fake/mock use case in isolation.
- Unit tests verify Koin DI graph resolves `TaskSubmissionViewModel`.

## 5. Out of Scope
- Implementation of task list, cards, and filter UI components (tracked in `compose_ui_components`).
