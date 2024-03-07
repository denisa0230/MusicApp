package app.commands.userCommands;

import app.Admin;
import app.commands.Command;
import app.user.UserAbstract;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@code Command} class and deletes
 * a user from the system.
 */
public class DeleteUser extends Command {
    /**
     * The username of the user to be deleted.
     */
    private String username;

    public DeleteUser(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        username = commandInput.getUsername();
    }

    /**
     * Executes the command to delete a user and generates the output message.
     */
    @Override
    public void execute() {
        String message = deleteUser();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Deletes the specified user if it exists and can be deleted.
     * @return A message indicating the success or failure of the operation.
     */
    public String deleteUser() {
        if (!Admin.getInstance().userExists(username)) {
            return "The username " + username + " doesn't exist.";
        }

        UserAbstract user = Admin.getInstance().getUser(username);
        if (user.canBeDeleted()) {
            user.removeUser();
            return username + " was successfully deleted.";
        }

        return username + " can't be deleted.";
    }
}
