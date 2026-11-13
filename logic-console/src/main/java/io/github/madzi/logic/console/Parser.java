package io.github.madzi.logic.console;

import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Console Parser.
 */
public class Parser {

    private static final int SAVE_LOAD_LEN = 5;
    private static final String TERM = "[\\p{L}\\p{N}_]+";
    private static final Pattern FACT = Pattern.compile("^([^\\s:]+)\\s*:\\s*(" + TERM + ")\\s*->\\s*(" + TERM + ")$");
    private static final Pattern QUERY = Pattern.compile("^\\?\\s*:\\s*(" + TERM + ")\\s*->\\s*(" + TERM + ")$");

    public Optional<Command> parse(final String raw) {
        String line = raw.strip();
        if (line.isEmpty() || line.startsWith("%")) {
            return Optional.empty();
        }
        if ("exit".equals(line)) {
            return Optional.of(new Command.Exit());
        }
        if ("help".equals(line)) {
            return Optional.of(new Command.Help());
        }
        if (line.startsWith("save ")) {
            return Optional.of(new Command.Save(Path.of(line.substring(SAVE_LOAD_LEN).strip())));
        }
        if (line.startsWith("load ")) {
            return Optional.of(new Command.Load(Path.of(line.substring(SAVE_LOAD_LEN).strip())));
        }
        var query = QUERY.matcher(line);
        if (query.matches()) {
            return Optional.of(new Command.Query(query.group(1), query.group(2)));
        }
        var fact = FACT.matcher(line);
        if (fact.matches()) {
            return Optional.of(new Command.AddFact(fact.group(1), fact.group(2), fact.group(3)));
        }
        throw new IllegalArgumentException("Unknown command format");
    }
}
