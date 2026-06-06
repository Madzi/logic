package io.github.madzi.logic.core;

import java.util.Arrays;

public record Relation(Logic[] dkscale) {

    public static final Relation A = new Relation(new Logic[] { Logic.TRUE, Logic.TRUE, Logic.FALSE, Logic.FALSE, Logic.UNKNOWN, Logic.UNKNOWN, Logic.TRUE, Logic.TRUE });
    public static final Relation E = new Relation(new Logic[] { Logic.TRUE, Logic.FALSE, Logic.UNKNOWN, Logic.TRUE, Logic.TRUE, Logic.FALSE, Logic.UNKNOWN, Logic.TRUE });
    public static final Relation I = new Relation(new Logic[] { Logic.UNKNOWN, Logic.TRUE, Logic.TRUE, Logic.UNKNOWN, Logic.FALSE, Logic.UNKNOWN, Logic.FALSE, Logic.TRUE });
    public static final Relation O = new Relation(new Logic[] { Logic.UNKNOWN, Logic.FALSE, Logic.UNKNOWN, Logic.TRUE, Logic.TRUE, Logic.TRUE, Logic.FALSE, Logic.UNKNOWN });

    private static final int SUPPORTED_LEN = 8;

    public Relation {
        if (dkscale.length != SUPPORTED_LEN) {
            throw new IllegalArgumentException("Supported only " + SUPPORTED_LEN + " length");
        }
    }

    public Relation intersect(Relation other) {
        final var resScale = new Logic[SUPPORTED_LEN];
        for (int i = 0; i < SUPPORTED_LEN; ++i) {
            resScale[i] = this.dkscale[i].and(other.dkscale[i]);
        }
        return new Relation(resScale);
    }

    public int matchScore(final Relation other) {
        int score = 0;
        for (int i = 0; i < SUPPORTED_LEN; ++i) {
            Logic exp = dkscale[i];
            Logic act = other.dkscale[i];
            if (exp == act) {
                score += 2;
            } else if (exp == Logic.UNKNOWN || act == Logic.UNKNOWN) {
                score += 1;
            } else {
                score -= 3;
            }
        }
        return score;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Relation rel && Arrays.equals(dkscale, rel.dkscale);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(dkscale);
    }
}
