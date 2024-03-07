package app.commands.userCommands.normalUser;

import app.Admin;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;

/**
 * This command extends the {@code Command} class and
 * retrieves a user's list of bought merch.
 */
public class SeeMerch extends Command {
    private final String username;
    public SeeMerch(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        username = commandInput.getUsername();
    }

    /**
     * Executes the SeeMerch command and sets the output with the generated result.
     */
    public void execute() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        ArrayList<String> result = seeMerch();
        if (result == null) {
            objectNode.put("message", "The username "
                    + commandInput.getUsername() + " doesn't exist.");
        } else {
            objectNode.put("result", objectMapper.valueToTree(result));
        }

        this.setOutput(objectNode);
    }

    /**
     * Get list of merch bought by the user.
     *
     * @return the list of merch
     */
    public ArrayList<String> seeMerch() {
        User user = Admin.getInstance().getNormalUser(username);
        if (user == null) {
            return null;
        }
        return user.getMerch();
    }
}
