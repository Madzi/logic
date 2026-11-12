package io.github.madzi.logic.engine.internal;

import io.github.madzi.logic.core.Logic;
import io.github.madzi.logic.core.relation.Relation2;
import io.github.madzi.logic.engine.Axis;
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

/**
 * Логический движок для работы с фактами и дедукциями.
 */
public final class LogicEngine implements Engine {
    private final Map<String, List<Fact>> index = new ConcurrentHashMap<>();
    private final Map<Axis, Deduction> deductions = new ConcurrentHashMap<>();

    @Override
    public void clear() {
        index.clear();
        deductions.clear();
    }

    @Override
    public Engine fact(final Fact fact) {
        putFact(fact);
        // Explicitly add inverse as a known fact to ensure it's not replaced by deductions
        final var inverse = fact.inverse();
        mergeDeduction(inverse.axis(), factFromUser(inverse));
        return this;
    }

    @Override
    public Optional<Deduction> find(final String subject, final String predicate) {
        final var axis = new Axis(subject, predicate);
        return deductions.containsKey(axis) ? Optional.of(deductions.get(axis)) : Optional.empty();
    }

    private void putFact(final Fact fact) {
        mergeDeduction(fact.axis(), factFromUser(fact));
    }

    private void toIndex(final String name, final Fact fact) {
        var facts = index.computeIfAbsent(name, key -> new ArrayList<>());
        
        for (Fact existing : facts) {
            if (existing.axis().equals(fact.axis()) && getCertainty(existing.relation()) >= getCertainty(fact.relation())) {
                return;
            }
        }
        
        var second = fact.axis().other(name);
        for (Fact item : new ArrayList<>(facts)) {
            if (!Objects.equals(second, item.axis().other(name))) {
                combine(fact, item);
                combine(item, fact);
            }
        }
        facts.add(fact);
    }

    private void combine(final Fact fact, final Fact item) {
        Deduction ded = null;
        if (Objects.equals(fact.axis().y(), item.axis().x())) {
            ded = figure1(fact, item);
        } else if (Objects.equals(fact.axis().y(), item.axis().y())) {
            ded = figure2(fact, item);
        } else if (Objects.equals(fact.axis().x(), item.axis().x())) {
            ded = figure3(fact, item);
        } else if (Objects.equals(fact.axis().x(), item.axis().y())) {
            ded = figure4(fact, item);
        }

        if (ded != null) {
            mergeDeduction(ded.conclusion().axis(), ded);
            
            // Also derive inverse for every new deduction to keep knowledge symmetrical
            final var inverseFact = ded.conclusion().inverse();
            mergeDeduction(inverseFact.axis(), new Deduction(ded.minor(), ded.major(), inverseFact));
        }
    }

    private void mergeDeduction(Axis axis, Deduction newDed) {
        final var existing = deductions.get(axis);
        if (existing == null) {
            deductions.put(axis, newDed);
            toIndex(axis.x(), newDed.conclusion());
            toIndex(axis.y(), newDed.conclusion());
        } else {
            // Priority 1: User Facts (major == null) over Deductions
            boolean existingIsFact = existing.major() == null;
            boolean newIsFact = newDed.major() == null;
            
            int existingCertainty = getCertainty(existing.conclusion().relation());
            int newCertainty = getCertainty(newDed.conclusion().relation());
            
            boolean shouldUpdate = false;
            if (newIsFact && !existingIsFact) {
                shouldUpdate = true; // Fact overrides deduction
            } else if (newIsFact == existingIsFact) {
                // If both are facts or both are deductions, choose more certain one
                if (newCertainty > existingCertainty) {
                    shouldUpdate = true;
                }
            }
            
            if (shouldUpdate) {
                deductions.put(axis, newDed);
                toIndex(axis.x(), newDed.conclusion());
                toIndex(axis.y(), newDed.conclusion());
            }
        }
    }

    private int getCertainty(Relation2 relation) {
        int knownCount = 0;
        for (Logic l : relation.dkscale().scale()) {
            if (l != Logic.UNKNOWN) knownCount++;
        }
        return knownCount;
    }

    private Deduction figure1(final Fact fact, final Fact item) {
        final var result = new Fact(new Axis(fact.axis().x(), item.axis().y()), fact.relation().conclusion(item.relation()));
        return new Deduction(fact, item, result);
    }

    private Deduction figure2(final Fact fact, final Fact item) {
        final var result = new Fact(new Axis(fact.axis().x(), item.axis().x()), fact.relation().conclusion(item.relation().inverse()));
        return new Deduction(fact, item, result);
    }

    private Deduction figure3(final Fact fact, final Fact item) {
        final var result = new Fact(new Axis(fact.axis().y(), item.axis().y()), fact.relation().inverse().conclusion(item.relation()));
        return new Deduction(fact, item, result);
    }

    private Deduction figure4(final Fact fact, final Fact item) {
        final var result = new Fact(new Axis(fact.axis().y(), item.axis().x()), fact.relation().inverse().conclusion(item.relation().inverse()));
        return new Deduction(fact, item, result);
    }

    private Deduction factFromUser(final Fact fact) { return new Deduction(null, null, fact); }
}
