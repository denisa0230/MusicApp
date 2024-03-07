package app.commands.userCommands.normalUser;

import app.Admin;
import app.commands.Command;
import app.subscriptions.Notification;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;

/**
 * This command extends the {@code Command} class and
 * retrieves a user's notifications.
 */
public class GetNotifications extends Command {
    private final String username;
    public GetNotifications(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        username = commandInput.getUsername();
    }

    /**
     * Executes the BuyMerch command and sets the output with the generated result.
     */
    public void execute() {
        ArrayList<Notification> notifications = getNotifications();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("notifications", objectMapper.valueToTree(notifications));

        this.setOutput(objectNode);
    }

    /**
     * Retrieve all user notifications.
     * @return message
     */
    public ArrayList<Notification> getNotifications() {
        User user = Admin.getInstance().getNormalUser(username);
        if (user == null) {
            return new ArrayList<>();
        }
        ArrayList<Notification> notifications = new ArrayList<>(user.getNotifications());
        user.getNotifications().clear();
        return notifications;
    }
}
