package app.commands.playerCommands;

import app.Admin;
import app.commands.Command;
import app.user.User;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This class extends the {@code Command} class and introduces
 * an adBreak in a user's listening session.
 */
public class AdBreak extends Command {
    private final Integer price;
    private final String username;
    public AdBreak(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        price = commandInput.getPrice();
        username = commandInput.getUsername();
    }

    /**
     * Executes the AdBreak command and sets the output.
     */
    @Override
    public void execute() {
        String message = adBreak();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Prepares the player for an adBreak.
     *
     * @return message
     */
    public String adBreak() {
        User user = Admin.getInstance().getNormalUser(username);
        if (user == null) {
            return username + " doesn't exist.";
        }
        if (user.getPlayer().getSource() == null
            || user.getPlayer().getSource().getType() == Utils.PlayerSourceType.PODCAST) {
            return username + " is not playing any music.";
        }
        if (!user.isPremium()) {
            // Insert ad break for user with free account
            user.getPlayer().setAdIncoming(true);
            user.getPlayer().setAdPrice(price);
        }
        return "Ad inserted successfully.";
    }
}
