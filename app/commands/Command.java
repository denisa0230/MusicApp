package app.commands;

import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import lombok.Getter;
import lombok.Setter;

/**
 * Generic command.
 */
@Getter
public abstract class Command {
    @Setter
    public CommandInput commandInput;
    @Setter
    private ObjectNode output;

    /**
     * Executes a command.
     */
    public abstract void execute();
}
