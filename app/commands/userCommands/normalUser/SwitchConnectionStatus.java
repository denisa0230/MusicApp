/**
 * The package containing classes related to user commands.
 */
package app.commands.userCommands.normalUser;

import app.Admin;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * The {@code SwitchConnectionStatus} extends the {@code Command} class and
 * changes the online status of a normal user.
 */
public class SwitchConnectionStatus extends Command {
    private final String username;

    public SwitchConnectionStatus(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        username = commandInput.getUsername();
    }

    /**
     * Executes the switch connection status command. It toggles the online status of the user,
     * and the result is stored in the output for further processing.
     */
    public void execute() {
        String message = switchConnectionStatus();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Switches the connection status of the specified user.
     * @return a message indicating the success or failure of the operation
     */
    public String switchConnectionStatus() {
        User user = Admin.getInstance().getNormalUser(username);
        if (user == null) {
            if (Admin.getInstance().getHost(username) == null
                    && Admin.getInstance().getArtist(username) == null) {
                return "The username " + username + " doesn't exist.";
            }
            return username + " is not a normal user.";
        }
        user.setOnline(!user.isOnline());
        return username + " has changed status successfully.";
    }
}
