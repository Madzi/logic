package io.github.madzi.logic.calc;

import io.github.madzi.logic.core.relation.Relation;
import java.util.Set;

public record FactItem(Set<String> axises, Relation<?> relation) {

    public boolean hasAxis(final String name) {
        return axises.contains(name);
    }

    public int indexOf(final String axis) {
        return 0; // TODO
    }
}
