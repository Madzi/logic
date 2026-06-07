package io.github.madzi.logic.core.relation;

import io.github.madzi.logic.core.Logic;

public interface Relation<R extends Relation> {

    Logic[] dkscale();

    R intersect(R relation);
}
