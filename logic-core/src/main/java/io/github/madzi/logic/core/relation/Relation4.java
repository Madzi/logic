package io.github.madzi.logic.core.relation;

import io.github.madzi.logic.core.scale.DKScale4;

public record Relation4(DKScale4 scale) implements Relation<Relation4> {
    @Override public DKScale4 dkscale() { return scale; }
    @Override public Relation4 inverse() { return Relation.create(scale.not()); }
    @Override public Relation4 intersect(final Relation4 other) { return Relation.create(scale.and(other.scale)); }
    @Override public Relation4 union(final Relation4 other) { return Relation.create(scale.or(other.scale)); }
}
