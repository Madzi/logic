package io.github.madzi.logic.engine.internal;

import java.util.Objects;

public record Axis(String x, String y) {
    public String other(final String name) {
        if (Objects.equals(x, name)) { return y; } else if (Objects.equals(y, name)) { return x; }
        throw new IllegalArgumentException("Unknown axis: " + name);
    }
}
