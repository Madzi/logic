package io.github.madzi.logic.core.internal;

import io.github.madzi.logic.core.Atom;
import io.github.madzi.logic.core.Engine;
import io.github.madzi.logic.core.Logic;
import io.github.madzi.logic.core.relation.Relation2;
import java.util.Optional;

public record LogicEngine(ConceptState state, KnowledgeBase kb, RelationRegistry registry) implements Engine {

    public LogicEngine() {
        this(new ConceptState(), new KnowledgeBase(), new RelationRegistry());
    }

    @Override
    public Engine defineRelation(final String name, final Relation2 relation) {
        registry.register(name, relation);
        return this;
    }

    @Override
    public Engine fact(final String relName, final Atom<?> subject, final Atom<?> predicate) {
        registry.getRelation(relName).ifPresentOrElse(
            relation -> kb.addFact(relation, subject, predicate),
            () -> { throw new IllegalStateException("Unknown relation: " + relName); }
        );
        return this;
    }

    @Override
    public Engine status(final Atom<?> concept, final Logic logic) {
        state.setStatus(concept, logic);
        return this;
    }

    @Override
    public Logic checkStatus(final Atom<?> concept) {
        Logic logic = state.getStatus(concept);
        if (logic == Logic.UNKNOWN) {
            for (Atom<?> knownConcept : state.getKnownConcepts()) {
                if (state.getStatus(knownConcept) == Logic.TRUE) {
                    Relation2 rel = kb.getRelation(knownConcept, concept);
                    if (null != rel && isAllXAreY(rel)) {
                        return Logic.TRUE;
                    }
                }
            }
        }
        return logic;
    }

    private boolean isAllXAreY(Relation2 rel) {
        // В ДК4 за зону xy' (X есть, Y нет) отвечает строго индекс 1
        return rel.dkscale()[1] == Logic.FALSE;
    }

    @Override
    public Optional<String> inferRelation(final Atom<?> subject, final Atom<?> predicate) {
        Relation2 relation = kb.getRelation(subject, predicate);
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
