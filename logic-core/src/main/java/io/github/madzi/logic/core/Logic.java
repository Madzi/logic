package io.github.madzi.logic.core;

/**
 * Base logic elements.
 */
public enum Logic {
    FALSE(-1),
    UNKNOWN(0),
    TRUE(1);

    private final int value;

    Logic(final int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    public Logic not() {
        return Logic.valueOf(-value);
    }

    public Logic and(final Logic logic) {
        return Logic.valueOf(Math.min(value, logic.value));
    }

    public Logic or(final Logic logic) {
        return Logic.valueOf(Math.max(value, logic.value));
    }

    public static Logic valueOf(final int obj) {
        return obj < 0 ? FALSE : obj > 0 ? TRUE : UNKNOWN;
    }

    public static Logic valueOf(final Boolean bool) {
        return bool == null ? UNKNOWN : bool ? TRUE : FALSE;
    }
}
