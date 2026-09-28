# Specification: Establish Clean Architecture foundation and modular Compose Multiplatform task components

## 1. Overview
This track establishes an exemplary Clean Architecture foundation in the `shared` module and decomposes the presentation layer in `composeApp` into modular, reusable Compose Multiplatform UI components. It introduces rich task lifecycle models with flexible status stages (To Do, In Progress, Blocked, Done) and reactive state management.

## 2. Goals & Objectives
- **Clean Architecture:** Separate domain business entities and use cases from data sources and presentation logic in the `shared` module.
- **Rich Task Domain:** Model tasks with comprehensive attributes: title, description, status lifecycle (To Do, In Progress, Blocked, Done), priority, and subtasks.
- **Reactive Repository & Use Cases:** Provide reactive `Flow`-based repository interfaces and dedicated use cases for querying, filtering, and mutating tasks.
- **Modular Compose UI:** Build standalone, reusable Compose Multiplatform components (`StatusBadge`, `PriorityBadge`, `TaskCard`, `TaskStatusFilterBar`, `EmptyStateView`).
- **Dashboard Integration:** Deliver an interactive, filterable Task Dashboard screen adhering to Unidirectional Data Flow (UDF) via `StateFlow` and Material 3 design tokens.

## 3. Architecture & Design

### 3.1 Domain Layer (`shared/src/commonMain/kotlin/.../domain`)
- **Entities:**
  - `Task`: `val id: String`, `val title: String`, `val description: String`, `val status: TaskStatus`, `val priority: TaskPriority`, `val subtasks: List<Subtask>`
  - `Subtask`: `val id: String`, `val title: String`, `val isCompleted: Boolean`
  - `TaskStatus`: Enum (`TODO`, `IN_PROGRESS`, `BLOCKED`, `DONE`)
  - `TaskPriority`: Enum (`LOW`, `MEDIUM`, `HIGH`)
- **Repository Interface:**
  - `TaskRepository`:
    - `getTasks(): Flow<List<Task>>`
    - `getTaskById(id: String): Flow<Task?>`
    - `upsertTask(task: Task): suspend () -> Unit`
    - `updateTaskStatus(id: String, status: TaskStatus): suspend () -> Unit`
    - `deleteTask(id: String): suspend () -> Unit`
- **Use Cases:**
  - `GetTasksUseCase`: Returns Flow of tasks, optional status filter.
  - `UpdateTaskStatusUseCase`: Modifies task status and emits update.
  - `SaveTaskUseCase`: Creates or updates a task entity.

### 3.2 Data Layer (`shared/src/commonMain/kotlin/.../data`)
- **Implementation:**
  - `InMemoryTaskRepository`: Thread-safe, reactive in-memory implementation of `TaskRepository` initialized with rich sample tasks.
- **Dependency Injection:**
  - Update Koin modules (`CoreModule`, `RepositoryModule`, `UseCaseModule`) to bind and inject new components.

### 3.3 Presentation Layer (`composeApp/src/commonMain/kotlin/.../ui`)
- **Components:**
  - `StatusBadge`: Material 3 AssistChip/Surface displaying status label with semantic color styling.
  - `PriorityBadge`: Visual indicator for task priority.
  - `TaskCard`: Card displaying title, description summary, badges, and progress bar for completed subtasks.
  - `TaskStatusFilterBar`: Horizontally scrollable row of status filter chips (All, To Do, In Progress, Blocked, Done).
  - `EmptyStateView`: Illustration/icon, title, and subtitle when no tasks match current filter.
- **Screen & ViewModel:**
  - `TaskListViewModel`: Manages filter state and observes `GetTasksUseCase`. Exposes `TaskListUiState(val tasks: List<Task>, val selectedStatus: TaskStatus?, val isLoading: Boolean)`.
  - `TaskListScreen`: Scaffold screen integrating the modular components with pull/filter interactions.

## 4. Testing & Acceptance Criteria
- Unit tests written before implementation for all domain use cases, repository operations, and ViewModels.
- Test coverage for new domain and data classes exceeds >80%.
- Both Android and iOS targets build without warnings or unresolved dependencies.
- Interactive status filter correctly filters displayed task cards.
- Status transition updates the task reactively.
