package app.commands.playerCommands;

import app.Admin;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@link Command} class and is used to
 * play or pause the player of a user.
 */
public class PlayPause extends Command {
    private final User user;
    public PlayPause(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
    }

    /**
     * Executes a PlayPause command and sets the output.
     */
    public void execute() {
        String message = playPause();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Implementation of the PlayPause command.
     * @return message
     */
    public String playPause() {
        if (!user.isOnline()) {
            return user.getUsername() + " is offline.";
        }

        if (user.getPlayer().getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        user.getPlayer().pause();

        if (user.getPlayer().getPaused()) {
            return "Playback paused successfully.";
        }

        return "Playback resumed successfully.";
    }
}
