package io.github.madzi.logic.core.scale;

import static io.github.madzi.logic.core.Logic.FALSE;
import static io.github.madzi.logic.core.Logic.TRUE;
import static io.github.madzi.logic.core.Logic.UNKNOWN;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DKScale1Test {

    private static final DKScale1 FSCALE = new DKScale1(FALSE);
    private static final DKScale1 USCALE = new DKScale1(UNKNOWN);
    private static final DKScale1 TSCALE = new DKScale1(TRUE);

    @Test
    void testCreationFalse() {
        Assertions.assertEquals(FALSE, FSCALE.x());
        Assertions.assertEquals(TRUE, FSCALE.x_());
    }

    @Test
    void testNotFalse() {
        final var scale = FSCALE.not();
        Assertions.assertEquals(TRUE, scale.x());
        Assertions.assertEquals(FALSE, scale.x_());
    }

    @Test
    void testCreationUnknown() {
        Assertions.assertEquals(UNKNOWN, USCALE.x());
        Assertions.assertEquals(UNKNOWN, USCALE.x_());
    }

    @Test
    void testNotUnknown() {
        final var scale = USCALE.not();
        Assertions.assertEquals(UNKNOWN, scale.x());
        Assertions.assertEquals(UNKNOWN, scale.x_());
    }

    @Test
    void testCreationTrue() {
        Assertions.assertEquals(TRUE, TSCALE.x());
        Assertions.assertEquals(FALSE, TSCALE.x_());
    }

    @Test
    void testNotTrue() {
        final var scale = TSCALE.not();
        Assertions.assertEquals(FALSE, scale.x());
        Assertions.assertEquals(TRUE, scale.x_());
    }

    @Test
    void testFF() {
        final var scale = new DKScale1(FALSE, FALSE);
        Assertions.assertEquals(FALSE, scale.x());
        Assertions.assertEquals(FALSE, scale.x_());
    }

    @Test
    void testLiftAndEliminate() {
        final var scale = TSCALE;
        final var scale2 = scale.liftAsX();
        final var scale1 = scale2.eliminateY();
        Assertions.assertEquals(scale, scale1);
    }
}
