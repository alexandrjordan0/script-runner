# Kotlin Script Runner

This is a Kotlin Multiplatform project targeting Desktop (JVM). It provides a lightweight, modern execution environment for running, debugging, and managing Kotlin scripts (`.kts`) with a custom graphical interface.

## Features

* **Dual-Pane Interface**: A resizable split-screen layout featuring a code editor and a live output terminal side-by-side.
* **Standard Execution**: seamless integration with the system's `kotlinc` compiler to execute scripts in a real environment.
* **Live Output Streaming**: Captures `stdout` and `stderr` in real-time, allowing for immediate feedback on long-running processes.
* **Process Management**:
  * Visual status indicators for running processes.
  * One-click termination for hanging or long-running scripts.
  * Clear reporting of process exit codes upon completion.
* **Syntax Highlighting**: Custom highlighting for Kotlin keywords to improve code readability.
* **Smart Error Navigation**: Automatically parses execution errors and generates clickable links. Clicking a link instantly moves the editor cursor to the exact line and column of the issue.
* **Modern UI**: A dark-themed, frameless window design for a unified aesthetic.

## Screenshots

**Live Progress**

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/dcdd06ec-b4cc-4041-9ab8-44b40ae2a112" />


**Error Navigation**

<img width="800" height="600" alt="image" src="https://github.com/user-attachments/assets/25eef21d-4d5e-4756-8e66-59fb2155e903" />


**Process Control**

<img width="800" height="558" alt="image" src="https://github.com/user-attachments/assets/4be0424f-8a15-458f-aad9-ee0f379252a8" />

## Prerequisites

* **JDK 11 or higher**: Required to run the JVM application.
* **Kotlin Compiler (`kotlinc`)**: Must be installed and available in the system PATH to enable script execution.

## Build and Run Desktop (JVM) Application

To build and run the development version of the desktop app, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:run
- on Windows
  .\gradlew.bat :composeApp:run
