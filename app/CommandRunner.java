package app;

import app.commands.Command;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
/**
 * The type Command runner (The invoker).
 */
public final class CommandRunner {
    @Setter
    @Getter
    private Command command; // reference to the command

    public CommandRunner() {
    }

    /**
     * Run command.
     *
     */
    public ObjectNode run() {
        command.execute();
        return command.getOutput();
    }
}
