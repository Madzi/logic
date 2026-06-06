package io.github.madzi.logic.core.internal;

import io.github.madzi.logic.core.Relation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record KnowledgeBase(Map<String, List<Edge>> graph) {

    public KnowledgeBase() {
        this(new ConcurrentHashMap<>());
    }

    public void addFact(final Relation relation, final String subject, final String predicate) {
        graph.computeIfAbsent(subject, key -> new ArrayList<>()).add(new Edge(predicate, relation));
    }

    public Relation getRelation(final String subject, final String predicate) {
        return findPath(subject, predicate, new HashMap<>());
    }

    private Relation findPath(final String current, final String target, final Map<String, Boolean> visited) {
        if (current.equals(target)) {
            return Relation.A;
        }
        if (visited.getOrDefault(current, false)) {
            return null;
        }
        visited.put(current, true);
        List<Edge> edges = graph.get(current);
        if (null == edges) {
            return null;
        }
        for (Edge edge : edges) {
            if (edge.target.equals(target)) {
                return edge.relation;
            }
            Relation subRelation = findPath(edge.target, target, visited);
            if (null != subRelation) {
                return edge.relation.intersect(subRelation);
            }
        }
        return null;
    }

    private record Edge(String target, Relation relation) {
    }
}
