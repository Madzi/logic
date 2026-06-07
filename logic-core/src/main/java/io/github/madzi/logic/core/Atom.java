package io.github.madzi.logic.core;

import io.github.madzi.logic.core.internal.SimpleAtom;

public interface Atom<T> {

    T value();

    static <V> Atom<V> create(final V value) {
        return new SimpleAtom<>(value);
    }
}
