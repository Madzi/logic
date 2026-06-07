package io.github.madzi.logic.core.relation;

import io.github.madzi.logic.core.Logic;
import java.util.Arrays;

public record Relation3(Logic[] dk8) implements Relation<Relation3> {

    private static final int SUPPORTED_LEN = 2 * 2 * 2;

    private static final int XYZ    = 0;
    private static final int XYZ_   = 1;
    private static final int XY_Z   = 2;
    private static final int XY_Z_  = 3;
    private static final int X_YZ   = 4;
    private static final int X_YZ_  = 5;
    private static final int X_Y_Z  = 6;
    private static final int X_Y_Z_ = 7;

    public Relation3 {
        if (dk8.length != SUPPORTED_LEN) {
            throw new IllegalArgumentException("Supported only " + SUPPORTED_LEN + " length");
        }
        dk8 = Arrays.copyOf(dk8, SUPPORTED_LEN);
    }

    @Override
    public Relation3 intersect(final Relation3 other) {
        return new Relation3(new Logic[] {
            dk8[XYZ].and(other.dk8[XYZ]),
            dk8[XYZ_].and(other.dk8[XYZ_]),
            dk8[XY_Z].and(other.dk8[XY_Z]),
            dk8[XY_Z_].and(other.dk8[XY_Z_]),
            dk8[X_YZ].and(other.dk8[X_YZ]),
            dk8[X_YZ_].and(other.dk8[X_YZ_]),
            dk8[X_Y_Z].and(other.dk8[X_Y_Z]),
            dk8[X_Y_Z_].and(other.dk8[X_Y_Z_])
        });
    }

    @Override
    public Logic[] dkscale() {
        return Arrays.copyOf(dk8, SUPPORTED_LEN);
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Relation3 rel && Arrays.equals(dk8, rel.dk8);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(dk8);
    }
}
