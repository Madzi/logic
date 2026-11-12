# Ternary Logic Engine

A Java implementation of a logical engine for working with facts and deductions based on syllogism (Brusentsov ternary logic).

## Project Structure

The project is organized into several Maven modules:

*   **`logic-core`**: The core logic components, including relations, scales, and fundamental logical structures.
*   **`logic-engine`**: The main engine implementation (`LogicEngine`) for deriving deductions from a knowledge base of facts.
*   **`logic-console`**: A command-line interface (CLI) to interact with the engine.
*   **`logic-calc`**: Additional calculation and utility modules.

Other important artefacts:

* `.agents/` - provide skills for agents
* `pom.xml` - Configuration for Maven
* `makefile` - Build script for project

## Code Standards

- Use Java 25 modern features like **record**, **switch expression**, **virtual threads**
- Use Java Modular System (`module-info.java` files)
- Place interfaces in exposed packages and implementation of interfaces into sub-package `internal` without exposing
