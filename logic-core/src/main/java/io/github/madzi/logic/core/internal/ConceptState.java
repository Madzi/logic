package io.github.madzi.logic.core.internal;

import io.github.madzi.logic.core.Logic;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public record ConceptState(Map<String, Logic> statusMap) {

    public ConceptState() {
        this(new ConcurrentHashMap<>());
    }

    public void setStatus(final String conecpt, final Logic status) {
        statusMap.put(conecpt, status);
    }

    public Logic getStatus(final String concept) {
        return statusMap.getOrDefault(concept, Logic.UNKNOWN);
    }

    public Set<String> getKnownConcepts() {
        return statusMap.keySet();
    }
}
