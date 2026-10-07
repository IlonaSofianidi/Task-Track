# Technology Stack

## 1. Programming Languages & Runtimes
- **Kotlin (v2.0.0):** Primary programming language across shared multiplatform business logic, domain models, and UI. Target JVM: 17.
- **Swift:** Native host application wrapper for iOS (`iosApp`).

## 2. Multiplatform UI & Presentation
- **Compose Multiplatform (v1.6.10):** Declarative shared UI layer rendered across Android and iOS.
- **Material 3:** Modern design system components, typography, colors, and shapes.
- **Navigation Compose (`androidx.navigation:navigation-compose`):** Declarative navigation graphs and route management.
- **Lifecycle & ViewModel Compose (`androidx.lifecycle:lifecycle-viewmodel-compose`):** State preservation and lifecycle-aware ViewModel scoping.

## 3. Architecture & Dependency Injection
- **Architecture Pattern:** Clean Architecture with Unidirectional Data Flow (UDF) and Model-View-ViewModel (MVVM).
  - `shared`: Domain models, use cases, repository contracts, and data sources.
  - `composeApp`: UI presentation, modular screen composables, ViewModels, and navigation.
- **Dependency Injection:** Koin (`koin-core` v3.6.0, `koin-compose` v1.2.0, `koin-compose-viewmodel` v1.2.0, `koin-android` v3.6.0) for modular dependency provision.

## 4. Asynchronous & Concurrency
- **Kotlin Coroutines (`kotlinx-coroutines-core` v1.8.1):** Structured concurrency for background tasks and asynchronous operations.
- **Kotlin Flow & StateFlow:** Reactive data streaming between repositories, use cases, and UI state.
- **DateTime:** `kotlinx-datetime` (v0.6.0) for multiplatform date and time operations.

## 5. Storage & Persistence
- **Current State:** In-memory mock data source (`LocalTaskDataSource`).
- **Target Storage:** Local persistence layer to support offline-first task caching and CRUD operations.

## 6. Testing & Quality Assurance
- **Unit Testing:** `kotlin-test`, `junit` (v4.13.2).
- **Coroutines Testing:** `kotlinx-coroutines-test` (v1.8.1).
- **Flow Testing:** Turbine (`app.cash.turbine` v0.12.1) for testing asynchronous Kotlin Flows.

## 7. Build System & Tooling
- **Build Tool:** Gradle with Kotlin DSL (`build.gradle.kts`, `settings.gradle.kts`).
- **Dependency Management:** Gradle Version Catalog (`gradle/libs.versions.toml`).
- **Android Gradle Plugin (AGP):** v8.0.2 (compileSdk: 34, minSdk: 24, targetSdk: 34).
- **iOS Tooling:** Xcode & Framework embedding.
