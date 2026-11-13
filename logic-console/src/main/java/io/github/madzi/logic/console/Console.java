package io.github.madzi.logic.console;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jline.builtins.Completers.FileNameCompleter;
import org.jline.reader.Completer;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * Console.
 */
public class Console {

    private static final String RELATION_HELP = """
    Relations:
    A: S -> P   => means: All S are P
    E: S -> P   => means: No S are P
    I: S -> P   => means: Some S are P
    O: S -> P   => means: Some S are not P
    ---
    """;
    private static final String HELP = """
    Commands:
    help              => this screen
    save <file.ext>   => save session
    load <file.ext>   => load session
    exit              => exit
    REL: S -> P       => add fact (REL: A, E, I, O, A', !A, !A', {TFUU}, etc...)
    ?: S -> P         => query deduction (example: ?: greek -> mortals)
    ---
    """;

    private final Parser parser = new Parser();
    private final Terminal terminal;
    private final PrintWriter out;
    private final Session session = new Session();

    public Console(final Terminal terminal) {
        this.terminal = terminal;
        this.out = terminal.writer();
    }

    public void run() {
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .appName("Ternary Logic Console")
                .completer(completer())
                .variable(LineReader.HISTORY_FILE, Path.of(System.getProperty("user.home"), ".logic_history"))
                .build();
        banner();
        var running = true;
        while (running) {
            String line;
            try {
                line = reader.readLine("=> ");
            } catch (final UserInterruptException _) {
                continue;
            } catch (final EndOfFileException _) {
                running = false;
                return;
            }
            try {
                Optional<Command> parsed = parser.parse(line);
                if (parsed.isEmpty()) {
                    continue;
                }
                switch (parsed.get()) {
                    case Command.Exit _ -> {
                        return;
                    }
                    case Command.Help _ ->
                        out.print(HELP);
                    case Command.Save(var file) -> {
                        session.save(file);
                        println("=> Session saved to: " + file);
                    }
                    case Command.Load(var file) -> {
                        session.load(file).forEach(this::println);
                        println("=> Session loaded from: " + file);
                    }
                    default ->
                        println(session.apply(parsed.get(), line.strip()));
                }
                out.flush();
            } catch (final IllegalArgumentException | IOException exception) {
                println("=> ERROR: " + exception.getMessage());
            }
        }
    }

    private Completer completer() {
        List<String> words = new ArrayList<>(session.relationNames());
        words.addAll(List.of("help", "exit"));
        return new AggregateCompleter(new ArgumentCompleter(new StringsCompleter("save", "load"), new FileNameCompleter()), new StringsCompleter(words));
    }

    private void banner() {
        var p = session.presenter();
        out.println("==> Ternary Logic Console <==");
        out.printf("Logic alphabet: FALSE=%s, UNKNOWN=%s, TRUE=%s%n", p.neg(), p.unk(), p.pos());
        out.print(RELATION_HELP);
        out.print(HELP);
        out.flush();
    }

    private void println(final String message) {
        out.println(message);
        out.flush();
    }

    public static void main(final String... args) throws IOException {
        try (final Terminal terminal = TerminalBuilder.builder().system(true).build()) {
            new Console(terminal).run();
        }
    }
}
