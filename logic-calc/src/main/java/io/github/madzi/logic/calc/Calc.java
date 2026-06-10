package io.github.madzi.logic.calc;

import io.github.madzi.logic.core.relation.Relation;

public interface Calc {

    void clear();

    void define(String name, Relation<?> relation);

    Relation<?> evaluate(String text);
}
