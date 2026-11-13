package io.github.madzi.logic.console;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;

import io.github.madzi.logic.core.relation.Relation;
import io.github.madzi.logic.core.relation.Relation2;

/**
 * Relation Names.
 */
public class RelationNames {

    private final Map<Relation2, String> byRelation = new HashMap<>();
    private final Map<String, Relation2> byName = new LinkedHashMap<>();

    public RelationNames() {
        Map<String, Relation2> base = new LinkedHashMap<>();
        base.put("A", Relation.A());
        base.put("E", Relation.E());
        base.put("I", Relation.I());
        base.put("O", Relation.O());
        register(base, "", "", r -> r);
        register(base, "!", "", RelationNames::negate);
        register(base, "", "'", Relation2::inverse);
        register(base, "!", "'", r -> negate(r).inverse());
    }

    public Optional<Relation2> byName(final String name) {
        return Optional.ofNullable(byName.get(name));
    }

    public Optional<String> nameOf(final Relation2 relation) {
        return Optional.ofNullable(byRelation.get(relation));
    }

    public Set<String> all() {
        return byName.keySet();
    }

    private void register(final Map<String, Relation2> base, final String prefix, final String suffix, UnaryOperator<Relation2> fn) {
        base.forEach((letter, relation) -> {
            String name = prefix + letter + suffix;
            Relation2 value = fn.apply(relation);
            byRelation.putIfAbsent(value, name);
            byName.put(name, value);
        });
    }

    private static Relation2 negate(final Relation2 relation) {
        return Relation.create(relation.dkscale().not());
    }
}
