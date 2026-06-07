package io.github.madzi.logic.core.relation;

import io.github.madzi.logic.core.Logic;
import java.util.Arrays;

public record Relation2(Logic[] dk4) implements Relation<Relation2> {

    private static final int  SUPPORTED_LEN = 2 * 2;

    private static final int XY   = 0;
    private static final int XY_  = 1;
    private static final int X_Y  = 2;
    private static final int X_Y_ = 3;

    public Relation2 {
        if (dk4.length != SUPPORTED_LEN) {
            throw new IllegalArgumentException("Supported only " + SUPPORTED_LEN + " length");
        }
        dk4 = Arrays.copyOf(dk4, SUPPORTED_LEN);
    }

    @Override
    public Relation2 intersect(final Relation2 other) {
        return new Relation2(new Logic[] {
            dk4[XY].and(other.dk4[XY]),
            dk4[XY_].and(other.dk4[XY_]),
            dk4[X_Y].and(other.dk4[X_Y]),
            dk4[X_Y_].and(other.dk4[X_Y_])
        });
    }

    @Override
    public Logic[] dkscale() {
        return Arrays.copyOf(dk4, SUPPORTED_LEN);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Relation2 rel && Arrays.equals(dk4, rel.dk4);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(dk4);
    }
}
