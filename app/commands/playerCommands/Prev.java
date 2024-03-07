package app.commands.playerCommands;

import app.Admin;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@link Command} class and is used to
 * go back to the previous track.
 */
public class Prev extends Command {
    private final User user; // receiver
    public Prev(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
    }

    /**
     * Executes a Prev command and sets the output.
     */
    public void execute() {
        String message = prev();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Implementation of the Prev command.
     * @return message
     */
    public String prev() {
        if (!user.isOnline()) {
            return user.getUsername() + " is offline.";
        }

        if (user.getPlayer().getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        user.getPlayer().prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(user.getPlayer().getCurrentAudioFile().getName());
    }
}
