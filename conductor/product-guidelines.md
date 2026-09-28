# Product Guidelines

## 1. Design & UX Principles
- **Clarity & Simplicity:** Focus on task readability and frictionless interactions. Minimize cognitive load with clear visual hierarchies and straightforward task flows.
- **Consistency Across Platforms:** Provide an intuitive and cohesive user experience on both Android and iOS while respecting platform-specific navigation idioms.
- **Responsive & Adaptive:** Layouts must gracefully adapt to varying mobile screen sizes, orientations, and display densities using Compose Multiplatform constraints and responsive padding.
- **Immediate Visual Feedback:** Every user interaction (taps, status transitions, inputs) must offer immediate visual or animated state transitions.

## 2. Visual Design & Theming
- **Design System:** Built on Material Design 3 (M3) guidelines via Compose Multiplatform's `material3` library.
- **Color Palette:**
  - Semantic color roles (`primary`, `secondary`, `surface`, `background`, `error`) defined in `Theme.kt` with strong contrast ratios.
  - Distinct status badges for task lifecycles (To Do, In Progress, Blocked, Done).
  - Seamless support for Light and Dark themes.
- **Typography:** Clear, legible type hierarchy using Material 3 `Typography` scales (`titleLarge`, `bodyMedium`, `labelSmall`).
- **Touch Targets:** Minimum 48dp x 48dp touch targets for all interactive buttons, icons, and checkboxes.

## 3. Component Architecture & Reusability
- **Atomic Decomposition:** UI elements must be modularized into self-contained, reusable Composables (e.g., `TaskCard`, `StatusBadge`, `TaskInputField`, `EmptyStateView`).
- **Stateless by Default:** Composables should be stateless, accepting data parameters and emitting event callbacks (`onClick`, `onStatusChange`), leaving business logic in ViewModels.
- **Isolated Testing & Preview:** Modular components enable straightforward previewing and isolated UI testing.

## 4. State & Error Handling
- **Comprehensive UI States:** Every screen must gracefully handle all UI states:
  - **Loading:** Subtle loading indicators or shimmer placeholders without jarring layout shifts.
  - **Empty:** Informative, friendly empty states with clear calls-to-action (e.g., "No tasks in this list").
  - **Content/Success:** Responsive content with fluid state transitions.
  - **Error:** Action-oriented, friendly messaging with retry mechanisms.
- **Unidirectional Data Flow (UDF):** UI observes immutable state and emits user intents upward.

## 5. Prose & Tone of Voice
- **Tone:** Professional, encouraging, clear, and concise.
- **Error Messages:** Helpful and actionable without technical jargon.
- **Labels & CTAs:** Short, imperative action phrases (e.g., "Add Task", "Save Changes", "Complete").
