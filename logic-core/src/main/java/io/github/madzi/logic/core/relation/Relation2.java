package io.github.madzi.logic.core.relation;

import io.github.madzi.logic.core.scale.DKScale2;

public record Relation2(DKScale2 scale) implements Relation<Relation2> {
    @Override public Relation2 inverse() { return Relation.create(scale.swapXY()); }
    @Override public Relation2 intersect(final Relation2 other) { return Relation.create(scale.and(other.scale)); }
    @Override public Relation2 union(final Relation2 other) { return Relation.create(scale.or(other.scale)); }
    public Relation2 conclusion(final Relation2 other) { return Relation.create(this.scale.liftAsXY().and(other.scale.liftAsYZ()).eliminateY()); }
    @Override public DKScale2 dkscale() { return scale; }
}
