package io.github.madzi.logic.core.internal;

import io.github.madzi.logic.core.Atom;
import io.github.madzi.logic.core.relation.Relation2;
import io.github.madzi.logic.core.relation.Transform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record KnowledgeBase(Map<Atom<?>, List<Edge>> graph, Transform transform) {

    public KnowledgeBase() {
        this(new ConcurrentHashMap<>(), new Transform());
    }

    public void addFact(final Relation2 relation, final Atom<?> subject, final Atom<?> predicate) {
        graph.computeIfAbsent(subject, key -> new ArrayList<>()).add(new Edge(predicate, relation));
    }

    public Relation2 getRelation(final Atom<?> subject, final Atom<?> predicate) {
        return findPath(subject, predicate, new HashMap<>());
    }

    private Relation2 findPath(final Atom<?> current, final Atom<?> target, final Map<Atom<?>, Boolean> visited) {
        if (current.equals(target)) {
            return RelationRegistry.A;
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
            Relation2 subRelation = findPath(edge.target, target, visited);
            if (null != subRelation) {
                return transform.conclude(edge.relation, subRelation);
            }
        }
        return null;
    }

    private record Edge(Atom<?> target, Relation2 relation) {

    }
}
