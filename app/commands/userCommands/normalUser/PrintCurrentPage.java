package app.commands.userCommands.normalUser;

import app.Admin;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * The {@code PrintCurrentPage} class extends the {@code Command} class and
 * prints the current page of the user.
 */
public class PrintCurrentPage extends Command {
    private final User user;

    public PrintCurrentPage(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
    }

    /**
     * Executes the command to print the current page of the user and generates the output message.
     */
    @Override
    public void execute() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user.isOnline()) {
            objectNode.put("message", user.getCurrentPage().pageFormat());
        } else {
            objectNode.put("message", user.getUsername() + " is offline.");
        }
        this.setOutput(objectNode);
    }
}
