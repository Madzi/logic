package io.github.madzi.logic.core.relation;

import io.github.madzi.logic.core.scale.DKScale1;

public record Relation1(DKScale1 scale) implements Relation<Relation1> {
  @Override
  public DKScale1 dkscale() {
    return scale;
  }

  @Override
  public Relation1 inverse() {
    return Relation.create(scale.not());
  }

  @Override
  public Relation1 intersect(final Relation1 other) {
    return Relation.create(scale.and(other.scale));
  }

  @Override
  public Relation1 union(final Relation1 other) {
    return Relation.create(scale.or(other.scale));
  }
}
