package io.github.madzi.logic.engine.internal;

import io.github.madzi.logic.engine.Deduction;
import io.github.madzi.logic.engine.Engine;
import io.github.madzi.logic.engine.Fact;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class LogicEngine implements Engine {
    private final Map<String, List<Fact>> index = new ConcurrentHashMap<>();
    private final Map<Axis, Deduction> deductions = new ConcurrentHashMap<>();
    @Override public void clear() {
        index.clear();
        deductions.clear();
    }
    @Override public Engine fact(final Fact fact) {
        final var axis = factAxis(fact);
        if (deductions.containsKey(axis)) {
            System.out.println("Already known fact"); // TODO change to notifier
            final var deprecated = deductions.get(axis);
            if (Objects.equals(fact.relation(), deprecated.conclusion().relation())) {
                System.out.println("Fact verified");
            } else {
                System.out.println("Relation changed !!!");
                if (null != deprecated.major()) {
                    System.out.println("Some CONFLICT into facts");
                    cleanup(deprecated);
                }
            }
        } else {
            addToIndex(fact.subject(), fact);
            addToIndex(fact.predicate(), fact);
        }
        final var deduction = new Deduction(null, null, fact);
        deductions.put(axis, deduction);
        return this;
    }
    @Override public Optional<Deduction> find(final String subject, final String predicate) {
        final var axis = new Axis(subject, predicate);
        return deductions.containsKey(axis) ? Optional.of(deductions.get(axis)) : Optional.empty();
    }
    private void addKnownFact(final Axis axis, final Fact fact) {
    }
    private void addUnknownFact(final Axis axis, final Fact fact) {
    }
    private void addToIndex(final String name, final Fact fact) {
        final var axis = new Axis(fact.subject(), fact.predicate());
        final var list = index.computeIfAbsent(name, key -> new ArrayList<>());
        if (!list.isEmpty()) {
            final String second = axis.other(name);
            for (Fact item : list) {
                final var found = axis.other(name);
                if (!Objects.equals(second, found)) {
                    var hash = new Axis(name, found);
                    if (!deductions.containsKey(hash)) {
                        deductions.put(hash, calc(fact, item));
                    }
                    hash = new Axis(found, name);
                    if (!deductions.containsKey(hash)) {
                        deductions.put(hash, calc(item, fact));
                    }
                }
            }
        }
        list.add(fact);
    }
    private Deduction calc(final Fact major, final Fact minor) {
        if (Objects.equals(major.predicate(), minor.subject()) || Objects.equals(major.predicate(), minor.predicate())) {
            if (Objects.equals(major.predicate(), minor.subject())) {
                return new Deduction(major, minor, new Fact(major.subject(), minor.predicate(), major.relation().conclusion(minor.relation())));
            } else {
                return new Deduction(major, minor, new Fact(major.subject(), minor.subject(), major.relation().conclusion(minor.relation().inverse())));
            }
        } else {
            if (Objects.equals(major.subject(), minor.subject())) {
                return new Deduction(major, minor, new Fact(major.predicate(), minor.predicate(), major.inverse().relation().conclusion(minor.relation())));
            } else {
                return new Deduction(major, minor, new Fact(major.predicate(), minor.subject(), major.inverse().relation().conclusion(minor.inverse().relation())));
            }
        }
    }
    private void cleanup(final Deduction deduction) {
        final Set<Axis> keys = new HashSet<>();
        for (Map.Entry<Axis, Deduction> entry : deductions.entrySet()) {
            var item = entry.getValue();
            if (Objects.equals(deduction.conclusion(), item.major()) || Objects.equals(deduction.conclusion(), item.minor())) {
                keys.add(entry.getKey());
            }
        }
        if (!keys.isEmpty()) {
            keys.forEach(deductions::remove);
        }
    }
    private Axis factAxis(final Fact fact) { return new Axis(fact.subject(), fact.predicate()); }
}
