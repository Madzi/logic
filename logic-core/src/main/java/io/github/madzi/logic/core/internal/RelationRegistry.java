package io.github.madzi.logic.core.internal;

import io.github.madzi.logic.core.Logic;
import io.github.madzi.logic.core.relation.Relation2;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public record RelationRegistry(Map<String, Relation2> registry) {

    public static final Relation2 A = new Relation2(new Logic[] { Logic.TRUE, Logic.FALSE, Logic.UNKNOWN, Logic.TRUE });
    public static final Relation2 E = new Relation2(new Logic[] { Logic.FALSE, Logic.TRUE, Logic.TRUE, Logic.UNKNOWN });
    public static final Relation2 I = new Relation2(new Logic[] { Logic.TRUE, Logic.UNKNOWN, Logic.UNKNOWN, Logic.TRUE });
    public static final Relation2 O = new Relation2(new Logic[] { Logic.UNKNOWN, Logic.TRUE, Logic.TRUE, Logic.UNKNOWN });

    public RelationRegistry() {
        this(new ConcurrentHashMap<>());
        register("A", A);
        register("E", E);
        register("I", I);
        register("O", O);
    }

    public void register(final String name, final Relation2 relation) {
        registry.put(name, relation);
    }

    public Optional<Relation2> getRelation(final String name) {
        return Optional.ofNullable(registry.get(name));
    }

    public Optional<String> deduceRelation(final Relation2 actual) {
        String bestTarget = null;
        int maxScore = Integer.MIN_VALUE;
        for (Map.Entry<String, Relation2> entry : registry.entrySet()) {
            Relation2 expected = entry.getValue();
            int score = matchScore(expected, actual);
            if (score > maxScore) {
                maxScore = score;
                bestTarget = entry.getKey();
            }
        }
        return maxScore > 0 ? Optional.of(bestTarget) : Optional.empty();
    }

    private int matchScore(Relation2 expected, Relation2 actual) {
        int score = 0;
        Logic[] exp = expected.dkscale();
        Logic[] act = actual.dkscale();
        for (int i = 0; i < 4; ++i) {
            if (exp[i] == act[i]) {
                score += 2;
            } else if (exp[i] == Logic.UNKNOWN || act[i] == Logic.UNKNOWN) {
                score += 1;
            } else {
                score -= 3;
            }
        }
        return score;
    }
}
