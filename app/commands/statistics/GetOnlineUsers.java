package app.commands.statistics;

import app.Admin;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.List;

/**
 * This command extends the {@link Command} class and is used to
 * print the usernames of the online users,
 */
public final class GetOnlineUsers extends Command {

    public GetOnlineUsers(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
    }

    /**
     * Executes the GetOnlineUsers command and sets the output with the generated result.
     */
    public void execute() {
        List<String> results = getOnlineUsers(); // calls method from receiver

        ObjectMapper objectMapper = new ObjectMapper(); // creates output

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(results));

        this.setOutput(objectNode);
    }

    /**
     * Retrieves a list of online users.
     * @return A list of usernames for the online users.
     */
    public static List<String> getOnlineUsers() {
        ArrayList<String> onlineUsers = new ArrayList<>();
        for (User user : Admin.getInstance().getNormalUsers()) {
            if (user.isOnline()) {
                onlineUsers.add(user.getUsername());
            }
        }
        return onlineUsers;
    }
}
