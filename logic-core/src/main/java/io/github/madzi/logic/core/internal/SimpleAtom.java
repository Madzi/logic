package io.github.madzi.logic.core.internal;

import io.github.madzi.logic.core.Atom;

public record SimpleAtom<T>(T value) implements Atom<T> {

}
