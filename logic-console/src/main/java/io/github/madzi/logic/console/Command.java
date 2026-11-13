package io.github.madzi.logic.console;

import java.nio.file.Path;

/**
 * Console command.
 */
public sealed interface Command {

    record AddFact(String relation, String subject, String predicate) implements Command {}

    record Query(String subject, String predicate) implements Command {}

    record Save(Path file) implements Command {}

    record Load(Path file) implements Command {}

    record Help() implements Command {}

    record Exit() implements Command {}
}
