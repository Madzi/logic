package io.github.madzi.logic.core;

import io.github.madzi.logic.core.internal.LogicEngine;
import io.github.madzi.logic.core.relation.Relation2;
import java.util.Optional;

public interface Engine {

    Engine defineRelation(String name, Relation2 relation);

    Engine fact(String relName, Atom<?> subject, Atom<?> predicate);

    Engine status(Atom<?> concept, Logic logic);

    Logic checkStatus(Atom<?> concept);

    Optional<String> inferRelation(Atom<?> subject, Atom<?> predicate);

    static Engine create() {
        return new LogicEngine();
    }
}
