package app.commands.playerCommands;

import app.Admin;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@link Command} class and is used to
 * skip to the next track.
 */
public class Next extends Command {
    private final User user; // receiver
    public Next(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
    }

    /**
     * Executes a Next command and sets the output.
     */
    public void execute() {
        String message = next();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Implementation of the Next command.
     * @return message
     */
    public String next() {
        if (!user.isOnline()) {
            return user.getUsername() + " is offline.";
        }

        if (user.getPlayer().getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        user.getPlayer().next();

        if (user.getPlayer().getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(user.getPlayer().getCurrentAudioFile().getName());
    }
}
