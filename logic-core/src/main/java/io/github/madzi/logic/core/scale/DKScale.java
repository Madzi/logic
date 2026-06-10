package io.github.madzi.logic.core.scale;

import io.github.madzi.logic.core.Logic;

public sealed interface DKScale<D extends DKScale<?>> permits DKScale1, DKScale2, DKScale3, DKScale4 {
    Logic[] scale();
    int size();
    D not();
    D and(D other);
    D or(D other);
}
