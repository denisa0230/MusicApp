package app.commands.userCommands;

import app.Admin;
import app.commands.Command;
import app.user.UserAbstract;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@link Command} class and is used to generate the
 * wrapped statistics for a certain user.
 */
public class Wrapped extends Command {
    private final UserAbstract user;

    public Wrapped(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getUser(commandInput.getUsername());
    }

    /**
     * Executes the Wrapped command and sets the output with the generated results
     * or the corresponding message.
     */
    @Override
    public void execute() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            // The user doesn't exist
            objectNode.put("message", "No data to show for user "
                    + commandInput.getUsername() + ".");
        } else {
            if (user.getStats().noStats()) {
                // The user has no activity to show in statistics
                objectNode.put("message", "No data to show for "
                        + user.getType() + " " + commandInput.getUsername() + ".");
            } else {
                objectNode.put("result", user.getUserStats());
            }
        }

        this.setOutput(objectNode);
    }
}
