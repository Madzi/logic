package io.github.madzi.logic.core.internal;

import io.github.madzi.logic.core.Atom;
import io.github.madzi.logic.core.Logic;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public record ConceptState(Map<Atom<?>, Logic> statusMap) {

    public ConceptState() {
        this(new ConcurrentHashMap<>());
    }

    public void setStatus(final Atom<?> conecpt, final Logic status) {
        statusMap.put(conecpt, status);
    }

    public Logic getStatus(final Atom<?> concept) {
        return statusMap.getOrDefault(concept, Logic.UNKNOWN);
    }

    public Set<Atom<?>> getKnownConcepts() {
        return statusMap.keySet();
    }
}
