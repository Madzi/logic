package io.github.madzi.logic.core.relation;

import io.github.madzi.logic.core.Logic;
import io.github.madzi.logic.core.relation.internal.RelationFactory;
import io.github.madzi.logic.core.scale.DKScale;
import io.github.madzi.logic.core.scale.DKScale1;
import io.github.madzi.logic.core.scale.DKScale2;
import io.github.madzi.logic.core.scale.DKScale3;
import io.github.madzi.logic.core.scale.DKScale4;

public sealed interface Relation<R extends Relation<?>> permits Relation1, Relation2, Relation3, Relation4 {
    DKScale<?> dkscale();
    R inverse();
    R intersect(R other);
    R union(R other);
    static Relation2 A() { return RelationFactory.INSTANCE.createRelation(new DKScale2(Logic.TRUE, Logic.FALSE, Logic.UNKNOWN, Logic.TRUE)); }
    static Relation2 E() { return RelationFactory.INSTANCE.createRelation(new DKScale2(Logic.FALSE, Logic.TRUE, Logic.TRUE, Logic.UNKNOWN)); }
    static Relation2 I() { return RelationFactory.INSTANCE.createRelation(new DKScale2(Logic.TRUE, Logic.UNKNOWN, Logic.UNKNOWN, Logic.TRUE)); }
    static Relation2 O() { return RelationFactory.INSTANCE.createRelation(new DKScale2(Logic.UNKNOWN, Logic.TRUE, Logic.TRUE, Logic.UNKNOWN)); }
    static Relation1 create(final DKScale1 scale) { return RelationFactory.INSTANCE.createRelation(scale); }
    static Relation2 create(final DKScale2 scale) { return RelationFactory.INSTANCE.createRelation(scale); }
    static Relation3 create(final DKScale3 scale) { return RelationFactory.INSTANCE.createRelation(scale); }
    static Relation4 create(final DKScale4 scale) { return RelationFactory.INSTANCE.createRelation(scale); }
}
