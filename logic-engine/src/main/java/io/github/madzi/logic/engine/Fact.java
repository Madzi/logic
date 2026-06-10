package io.github.madzi.logic.engine;

import io.github.madzi.logic.core.relation.Relation2;

public record Fact(String subject, String predicate, Relation2 relation) {
    public Fact inverse() { return new Fact(predicate, subject, relation.inverse()); }
}
