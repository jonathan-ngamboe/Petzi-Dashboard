# Common Module

## Description
The `Common` module is the shared library across all modules of the Petzi project. It includes commonly used classes and configurations needed by `PetziHook`, `RTBI`, and potentially other modules.

## Key Features
- Shared utility classes for data handling, security configuration, etc.
- Service components for common operations like validation, conversion, and data processing.
- Centralized configuration support for seamless integration with other modules, including Kafka connection details, database configuration, and more.

## Configuration
- **Shared Components:** Ensure the classes and configurations from this module are correctly integrated into the modules that depend on them.
- **Maven:** Add the `Common` module as a dependency in the `pom.xml` file of each module that uses it.

## Startup
The `Common` module is not a standalone application and does not require an independent launch. It is imported as a dependency by other modules.

## Usage
Simply include the `Common` module in `PetziHook`, `RTBI`, or any other module that requires access to shared components. Be sure to update any references when modifications are made to the `Common` module’s classes or configurations.

---

For more information about the overall project architecture and the other modules, refer to the [main Petzi project README](https://github.com/Jonathanngamboe/petzi).

---