package io.github.madzi.logic.core.internal;

import io.github.madzi.logic.core.Engine;
import io.github.madzi.logic.core.Logic;
import io.github.madzi.logic.core.Relation;
import java.util.Optional;

public record LogicEngine(ConceptState state, KnowledgeBase kb, RelationRegistry registry) implements Engine {

    public LogicEngine() {
        this(new ConceptState(), new KnowledgeBase(), new RelationRegistry());
    }

    @Override
    public Engine defineRelation(final String name, final Relation relation) {
        registry.register(name, relation);
        return this;
    }

    @Override
    public Engine fact(final String relName, final String subject, final String predicate) {
        registry.getRelation(relName).ifPresentOrElse(relation -> {
            kb.addFact(relation, subject, predicate);
        }, () -> {
            throw new IllegalStateException("Unknown relation: " + relName);
        });
        return this;
    }

    @Override
    public Engine status(final String conecpt, final Logic logic) {
        state.setStatus(conecpt, logic);
        return this;
    }

    @Override
    public Logic checkStatus(final String concept) {
        Logic logic = state.getStatus(concept);
        if (logic == Logic.UNKNOWN) {
            for (String knownConcept : state.getKnownConcepts()) {
                if (state.getStatus(knownConcept) == Logic.TRUE) {
                    Relation rel = kb.getRelation(knownConcept, concept);
                    if (null != rel && rel == Relation.A) {
                        return Logic.TRUE;
                    }
                }
            }
        }
        return logic;
    }

    @Override
    public Optional<String> inferRelation(String subject, String predicate) {
        Relation relation = kb.getRelation(subject, predicate);
        if (null == relation) {
            return Optional.empty();
        }
        Logic sLogic = state.getStatus(subject);
        Logic pLogic = state.getStatus(predicate);
        if (sLogic != Logic.TRUE || pLogic == Logic.UNKNOWN) {
            return Optional.empty();
        }
        return registry.deduceRelation(relation);
    }
}
