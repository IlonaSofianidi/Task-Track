# Specification: Modular Compose Multiplatform Task Components

## 1. Overview
This track decomposes the presentation layer in `composeApp` into modular, reusable Compose Multiplatform UI components for task tracking.

## 2. Goals & Objectives
- **Modular Compose UI:** Build standalone, reusable Compose Multiplatform components (`StatusBadge`, `PriorityBadge`, `TaskCard`, `TaskStatusFilterBar`, `EmptyStateView`).

## 3. Architecture & Design

### 3.1 Presentation Layer (`composeApp/src/commonMain/kotlin/.../ui`)
- **Components:**
  - `StatusBadge`: Material 3 AssistChip/Surface displaying status label with semantic color styling.
  - `PriorityBadge`: Visual indicator for task priority.
  - `TaskCard`: Card displaying title, description summary, badges, and progress bar for completed subtasks.
  - `TaskStatusFilterBar`: Horizontally scrollable row of status filter chips (All, To Do, In Progress, Blocked, Done).
  - `EmptyStateView`: Illustration/icon, title, and subtitle when no tasks match current filter.

## 4. Testing & Acceptance Criteria
- Unit tests written before implementation for components.
- Interactive status filter UI matches design spec.
