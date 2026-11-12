# Ternary Logic Engine

A Java implementation of a logical engine for working with facts and deductions based on syllogism (Brusentsov ternary logic).

## Overview

This project provides a framework to represent logical facts, define relations between subjects and predicates, and derive new conclusions (deductions) using classical syllogistic reasoning. It supports various types of categorical propositions (A, E, I, O) and implements the different figures of syllogism.

## Project Structure

The project is organized into several Maven modules:

*   **`logic-core`**: The core logic components, including relations, scales, and fundamental logical structures.
*   **`logic-engine`**: The main engine implementation (`LogicEngine`) for deriving deductions from a knowledge base of facts.
*   **`logic-console`**: A command-line interface (CLI) to interact with the engine.
*   **`logic-calc`**: Additional calculation and utility modules.

## Key Concepts

### Logical Engine (`LogicEngine`)

The `LogicEngine` maintains a knowledge base of facts and uses them to derive new deductions. It supports adding facts and querying for deductions between specific subjects and predicates.

### Facts and Deductions

*   **Fact**: Represents a logical assertion (e.g., "All humans are mortal"). A fact consists of an axis (subject and predicate) and a relation type between them.
*   **Deduction**: The result of the reasoning process, representing a conclusion derived from premises.

### Syllogistic Reasoning

The engine implements classical syllogism rules to determine connections between facts based on their figures:
- **First Figure**: $M \to P, S \to M \implies S \to P$
- **Second Figure**: $P \to M, S \to M \implies S \text{ is not } P$ (depending on relations)
- ... and so on for the other figures.

It supports the four standard categorical propositions:
*   **A** (Universal Affirmative): All $S$ are $P$.
*   **E** (Universal Negative): No $S$ are $P$.
*   **I** (Particular Affirmative): Some $S$ are $P$.
*   **O** (Particular Negative): Some $S$ are not $P$.

## Getting Started

### Prerequisites

*   Java 25 (or compatible JDK)
*   Maven 3.x+

### Building the Project

To build and install all modules, run:

```bash
./mvnw clean install
```

### Running Tests

To run the tests for a specific module (e.g., `logic-engine`):

```bash
./mvnw test -pl logic-engine
```

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for details.
