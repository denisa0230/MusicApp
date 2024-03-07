package app.commands.userCommands.normalUser;

import app.Admin;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@code Command} class and
 * switches a user's account from free to premium.
 */
public class BuyPremium extends Command {
    private final String username;
    public BuyPremium(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        username = commandInput.getUsername();
    }

    /**
     * Executes the BuyPremium command and sets the output with the generated result.
     */
    public void execute() {
        String message = buyPremium();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Buys premium subscription.
     * @return message
     */
    public String buyPremium() {
        User user = Admin.getInstance().getNormalUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (user.isPremium()) {
            return username + " is already a premium user.";
        }

        user.setPremium(true);

        return username + " bought the subscription successfully.";
    }
}
