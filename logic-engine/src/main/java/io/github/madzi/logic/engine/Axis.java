package io.github.madzi.logic.engine;

import java.util.Objects;

public record Axis(String x, String y) {
    public boolean same(Axis other) {
        return (Objects.equals(x, other.x) && Objects.equals(y, other.y))
                || (Objects.equals(x, other.y) && Objects.equals(y, other.x));
    }
    public Axis swapXY() { return new Axis(y, x); }
    public String other(final String name) {
        if (Objects.equals(x, name)) { return y; } else if (Objects.equals(y, name)) { return x; }
        throw new IllegalArgumentException("Unknown axis: " + name);
    }
}
