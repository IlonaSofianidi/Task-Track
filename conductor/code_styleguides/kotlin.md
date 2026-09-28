# Kotlin & Compose Multiplatform Style Guide

This document outlines the coding standards, patterns, and style conventions for Kotlin, Compose Multiplatform, and Clean Architecture in TaskTrack.

---

## 1. Kotlin Language Standards
- **Official Conventions:** Follow the official [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html).
- **Immutability First:**
  - Always prefer `val` over `var`.
  - Use immutable data classes for domain models and UI state models.
  - Return read-only collections (`List`, `Set`, `Map`) from repositories and use cases.
- **Null Safety:**
  - Avoid force unwrapping (`!!`). Use safe calls (`?.`), Elvis operator (`?:`), or `let`/`run`.
  - Avoid nullable collections; prefer empty collections instead of `null`.
- **Naming Conventions:**
  - **Classes & Interfaces:** PascalCase (`TaskRepository`, `TaskSubmissionUiState`).
  - **Functions & Properties:** camelCase (`getTasks()`, `isCompleted`).
  - **Constants:** UPPER_SNAKE_CASE for compile-time constants (`const val MAX_TITLE_LENGTH = 100`).
  - **Coroutines Flows:** Suffix Flow/StateFlow backing properties with underscore for private mutable state:
    ```kotlin
    private val _uiState = MutableStateFlow(TaskSubmissionUiState())
    val uiState: StateFlow<TaskSubmissionUiState> = _uiState.asStateFlow()
    ```

---

## 2. Compose Multiplatform Conventions
- **Composable Function Naming:**
  - Composable functions that emit UI must be named in PascalCase and be nouns: `TaskCard`, `TaskListScreen`, `StatusBadge`.
  - Composable functions that return a value should follow standard function naming in camelCase.
- **Modifier Ordering & Defaults:**
  - Always provide an optional `modifier: Modifier = Modifier` as the first optional parameter of a Composable.
  - Pass and chain the incoming `modifier` to the root layout element of the Composable.
- **Stateless & State Hoisting:**
  - Keep UI Composables stateless whenever possible.
  - Hoist state up to ViewModels or caller Composables.
  - Pass state down (`data`) and event callbacks up (`onTaskClicked: (String) -> Unit`).
- **Compose Preview:**
  - Include `@Preview` annotated composables with sample/preview data to enable rapid UI feedback.

---

## 3. Clean Architecture Layering
- **Domain Layer (`shared`):**
  - Contains pure Kotlin models, use case interfaces, and repository contracts.
  - Must not depend on UI frameworks (Compose, Android, iOS UIKit).
- **Data Layer (`shared`):**
  - Implements repository interfaces.
  - Encapsulates local and remote data sources.
  - Maps data transfer objects / database entities to domain models.
- **Presentation Layer (`composeApp`):**
  - ViewModels inherit from standard AndroidX / Multiplatform ViewModel.
  - ViewModels expose single immutable UI state via `StateFlow`.
  - ViewModels orchestrate use cases and transform domain results into UI states.

---

## 4. Coroutines & Concurrency
- Never block threads; use `suspend` functions and coroutines.
- Use explicit dispatchers via dependency injection for testability (`Dispatchers.Default`, `Dispatchers.IO`).
- Manage coroutine scopes tied to lifecycle (`viewModelScope`).
