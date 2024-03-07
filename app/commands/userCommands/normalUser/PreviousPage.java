package app.commands.userCommands.normalUser;

import app.Admin;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@code Command} class and goes to
 * the previous page in a user's page history.
 */
public class PreviousPage extends Command {
    private final User user;

    public PreviousPage(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
    }

    /**
     * Executes the PreviousPage command and generates the output message.
     */
    @Override
    public void execute() {
        String message = previousPage();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Go to the previous page in the page history.
     *
     * @return message
     */
    public String previousPage() {
        if (user == null) {
            return commandInput.getUsername() + " doesn't exist.";
        }
        if (user.getCurrentPageIndex() == 0) {
            return "There are no pages left to go back.";
        }
        user.setCurrentPageIndex(user.getCurrentPageIndex() - 1);
        user.setCurrentPage(user.getPageHistory().get(user.getCurrentPageIndex()));
        return "The user " + user.getUsername()
                + " has navigated successfully to the previous page.";
    }
}
