package io.github.madzi.logic.core.internal;

import io.github.madzi.logic.core.Logic;
import io.github.madzi.logic.core.Relation;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public record RelationRegistry(Map<String, Relation> registry) {

    public RelationRegistry() {
        this(new ConcurrentHashMap<>());
        register("A", Relation.A);
        register("E", Relation.E);
        register("I", Relation.I);
        register("O", Relation.O);
    }

    public void register(final String name, final Relation relation) {
        registry.put(name, relation);
    }

    public Optional<Relation> getRelation(final String name) {
        return Optional.ofNullable(registry.get(name));
    }

    public Optional<String> deduceRelation(final Relation actual) {
        String bestTarget = null;
        int maxCertainty = -1;

        for (Map.Entry<String, Relation> entry : registry.entrySet()) {
            Relation expected = entry.getValue();
            
            if (isDeductivelyValid(expected, actual)) {
                int certainty = calculateCertainty(expected, actual);
                if (certainty > maxCertainty) {
                    maxCertainty = certainty;
                    bestTarget = entry.getKey();
                }
            }
        }
        return Optional.ofNullable(bestTarget);
    }

    private boolean isDeductivelyValid(Relation expected, Relation actual) {
        for (int i = 0; i < 8; i++) {
            Logic exp = expected.dkscale()[i];
            Logic act = actual.dkscale()[i];
            if ((exp == Logic.TRUE && act == Logic.FALSE) || (exp == Logic.FALSE && act == Logic.TRUE)) {
                return false; 
            }
        }
        return true;
    }

    private int calculateCertainty(Relation expected, Relation actual) {
        int score = 0;
        for (int i = 0; i < 8; i++) {
            if (expected.dkscale()[i] == actual.dkscale()[i] && expected.dkscale()[i] != Logic.UNKNOWN) {
                score++;
            }
        }
        return score;
    }
}
