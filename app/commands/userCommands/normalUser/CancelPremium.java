package app.commands.userCommands.normalUser;

import app.Admin;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@code Command} class and
 * switches a user's account from premium to free.
 */
public class CancelPremium extends Command {
    private final String username;
    public CancelPremium(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        username = commandInput.getUsername();
    }

    /**
     * Executes the CancelPremium command and sets the output with the generated result.
     */
    public void execute() {
        String message = cancelPremium();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Cancels premium subscription.
     * @return message
     */
    public String cancelPremium() {
        User user = Admin.getInstance().getNormalUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (!user.isPremium()) {
            return username + " is not a premium user.";
        }
        user.payArtists();
        user.getPremiumContent().clear();
        user.setPremium(false);
        return username + " cancelled the subscription successfully.";
    }
}
