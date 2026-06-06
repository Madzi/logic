package io.github.madzi.logic.core;

import io.github.madzi.logic.core.internal.LogicEngine;
import java.util.Optional;

/**
 * Logic Engine.
 */
public interface Engine {

    Engine defineRelation(String name, Relation relation);

    Engine fact(String relName, String subject, String predicate);

    Engine status(String conecpt, Logic logic);

    Logic checkStatus(String concept);

    Optional<String> inferRelation(String subject, String predicate);

    static Engine create() {
        return new LogicEngine();
    }
}
