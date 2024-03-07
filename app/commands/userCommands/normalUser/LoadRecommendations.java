package app.commands.userCommands.normalUser;

import app.Admin;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@link Command} class and is used to
 * load the last recommendations of a user.
 */
public class LoadRecommendations extends Command {
    private final String username;
    public LoadRecommendations(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        username = commandInput.getUsername();
    }

    /**
     * Executes the LoadRecommendations command and sets the output with the generated result.
     */
    public void execute() {
        String message = loadRecommendations();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }


    /**
     * Implementation of the LoadRecommendations command.
     * @return message
     */
    public String loadRecommendations() {
        User user = Admin.getInstance().getNormalUser(username);
        if (user == null) {
            return username + " doesn't exist";
        }
        if (!user.isOnline()) {
            return username + " is offline.";
        }

        if (user.getLastRecommendation() == null
            || user.getLastRecommendation().getType() == null) {
            return "No recommendations available.";
        }

        // load cancels ads
        if (user.getPlayer().isAdIncoming()) {
            user.getPlayer().setAdIncoming(false);
            user.getPlayer().setAdPrice(0);
        }

        // set player source to the last recommendation
        if (user.getLastRecommendation().getType().equals("song")) {
            user.getPlayer().setSource(user.getLastRecommendation().getSong(),
                    user.getLastRecommendation().getType());
        } else {
            user.getPlayer().setSource(user.getLastRecommendation().getPlaylist(),
                    user.getLastRecommendation().getType());
        }

        // register listen
        user.registerListen(user.getPlayer().getSource());

        user.getPlayer().pause();

        return "Playback loaded successfully.";
    }
}
