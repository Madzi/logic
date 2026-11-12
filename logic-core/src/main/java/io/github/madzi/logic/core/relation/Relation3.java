package io.github.madzi.logic.core.relation;

import io.github.madzi.logic.core.scale.DKScale3;

public record Relation3(DKScale3 scale) implements Relation<Relation3> {
  @Override
  public Relation3 inverse() {
    return Relation.create(scale.not());
  }

  @Override
  public Relation3 intersect(final Relation3 other) {
    return Relation.create(scale.and(other.scale));
  }

  @Override
  public Relation3 union(final Relation3 other) {
    return Relation.create(scale.or(other.scale));
  }

  @Override
  public DKScale3 dkscale() {
    return scale;
  }
}
