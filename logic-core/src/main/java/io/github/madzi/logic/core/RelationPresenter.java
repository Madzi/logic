package io.github.madzi.logic.core;

import java.util.ArrayList;
import java.util.List;

public record RelationPresenter(LogicPresenter presenter) {

    public String write(final Relation relation) {
        final var builder = new StringBuilder();
        for (Logic logic : relation.dkscale()) {
            builder.append(presenter.write(logic));
        }
        return builder.toString();
    }

    public Relation parse(final String text) {
        if (null == text || text.isEmpty()) {
            throw new IllegalArgumentException("Unable to parse empty string");
        }
        final List<Logic> scales = new ArrayList<>();
        int cursor = 0;
        while (cursor < text.length()) {
            if (text.startsWith(presenter.pos(), cursor)) {
                scales.add(Logic.TRUE);
                cursor += presenter.pos().length();
            } else if (text.startsWith(presenter.neg(), cursor)) {
                scales.add(Logic.FALSE);
                cursor += presenter.neg().length();
            } else if (text.startsWith(presenter.unk(), cursor)) {
                scales.add(Logic.UNKNOWN);
                cursor += presenter.unk().length();
            } else {
                String remaining = text.substring(cursor);
                throw new IllegalArgumentException("Unable to parse logic element at position: " + cursor + "'" + remaining + "'");
            }
        }
        return new Relation(scales.toArray(Logic[]::new));
    }
}
