package io.github.madzi.logic.core.internal;

import io.github.madzi.logic.core.Relation;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public record RelationRegistry(Map<String, Relation> registry) {

    private static final int LIMIT_SCORE = 5;

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

    public Optional<String> findBestMatch(final Relation relation) {
        String bestName = null;
        int maxScore = -1;
        for (Map.Entry<String, Relation> entry : registry.entrySet()) {
            int score = entry.getValue().matchScore(relation);
            if (score > maxScore) {
                maxScore = score;
                bestName = entry.getKey();
            }
        }
        return maxScore < LIMIT_SCORE ? Optional.empty() : Optional.ofNullable(bestName);
    }

    public Optional<String> nameOfRelation(final Relation relation) {
        for (Map.Entry<String, Relation> entry : registry.entrySet()) {
            if (entry.getValue().equals(relation)) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }
}
