package io.github.madzi.logic.core.relation;

import io.github.madzi.logic.core.Logic;
import io.github.madzi.logic.core.relation.internal.RelationFactory;
import io.github.madzi.logic.core.scale.DKScale;
import io.github.madzi.logic.core.scale.DKScale1;
import io.github.madzi.logic.core.scale.DKScale2;
import io.github.madzi.logic.core.scale.DKScale3;
import io.github.madzi.logic.core.scale.DKScale4;

public sealed interface Relation<R extends Relation<?>>
    permits Relation1, Relation2, Relation3, Relation4 {
  /** Returns the DKScale associated with this relation. */
  DKScale<?> dkscale();

  /** Returns the inverse relation. */
  R inverse();

  /** Returns the intersection of this relation with another. */
  R intersect(final R other);

  /** Returns the union of this relation with another. */
  R union(final R other);

  /** Creates Aristotelian relation A (All S are P). */
  static Relation2 A() {
    return RelationFactory.INSTANCE.createRelation(
        new DKScale2(Logic.TRUE, Logic.FALSE, Logic.UNKNOWN, Logic.TRUE));
  }

  /** Creates Aristotelian relation E (No S are P). */
  static Relation2 E() {
    return RelationFactory.INSTANCE.createRelation(
        new DKScale2(Logic.FALSE, Logic.TRUE, Logic.TRUE, Logic.UNKNOWN));
  }

  /** Creates Aristotelian relation I (Some S are P). */
  static Relation2 I() {
    return RelationFactory.INSTANCE.createRelation(
        new DKScale2(Logic.TRUE, Logic.UNKNOWN, Logic.UNKNOWN, Logic.TRUE));
  }

  /** Creates Aristotelian relation O (Some S are not P). */
  static Relation2 O() {
    return RelationFactory.INSTANCE.createRelation(
        new DKScale2(Logic.UNKNOWN, Logic.TRUE, Logic.TRUE, Logic.UNKNOWN));
  }

  /** Creates a Relation1 from a DKScale1. */
  static Relation1 create(final DKScale1 scale) {
    return RelationFactory.INSTANCE.createRelation(scale);
  }

  /** Creates a Relation2 from a DKScale2. */
  static Relation2 create(final DKScale2 scale) {
    return RelationFactory.INSTANCE.createRelation(scale);
  }

  /** Creates a Relation3 from a DKScale3. */
  static Relation3 create(final DKScale3 scale) {
    return RelationFactory.INSTANCE.createRelation(scale);
  }

  /** Creates a Relation4 from a DKScale4. */
  static Relation4 create(final DKScale4 scale) {
    return RelationFactory.INSTANCE.createRelation(scale);
  }
}
