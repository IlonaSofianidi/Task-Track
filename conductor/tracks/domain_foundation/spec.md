# Specification: Domain & Data Clean Architecture Foundation

## 1. Overview
This track establishes an exemplary Clean Architecture foundation in the `shared` module. It introduces rich task lifecycle models with flexible status stages (To Do, In Progress, Blocked, Done) and reactive state management.

## 2. Goals & Objectives
- **Clean Architecture:** Separate domain business entities and use cases from data sources and presentation logic in the `shared` module.
- **Rich Task Domain:** Model tasks with comprehensive attributes: title, description, status lifecycle (To Do, In Progress, Blocked, Done), priority, and subtasks.
- **Reactive Repository & Use Cases:** Provide reactive `Flow`-based repository interfaces and dedicated use cases for querying, filtering, and mutating tasks.

## 3. Architecture & Design

### 3.1 Domain Layer (`shared/src/commonMain/kotlin/.../domain`)
- **Entities:**
  - `Task`: `val id: String`, `val title: String`, `val description: String`, `val status: TaskStatus`, `val priority: TaskPriority`, `val subtasks: List<Subtask>`
  - `Subtask`: `val id: String`, `val title: String`, `val isCompleted: Boolean`
  - `TaskStatus`: Enum (`TODO`, `IN_PROGRESS`, `BLOCKED`, `DONE`)
  - `TaskPriority`: Enum (`LOW`, `MEDIUM`, `HIGH`)
- **Repository Interface:**
  - `TaskRepository`: CRUD operations using reactive flows.
- **Use Cases:**
  - `GetTasksUseCase`: Returns Flow of tasks, optional status filter.
  - `UpdateTaskStatusUseCase`: Modifies task status and emits update.
  - `SaveTaskUseCase`: Creates or updates a task entity.

### 3.2 Data Layer (`shared/src/commonMain/kotlin/.../data`)
- **Implementation:**
  - `LocalTaskDataSource`: Thread-safe, reactive in-memory data source.
  - `DefaultTaskRepository`: Delegates to `LocalTaskDataSource`.
- **Dependency Injection:**
  - Update Koin modules (`CoreModule`, `RepositoryModule`, `UseCaseModule`).

## 4. Testing & Acceptance Criteria
- Unit tests written before implementation for all domain use cases and repository operations.
- Test coverage for new domain and data classes exceeds >80%.
