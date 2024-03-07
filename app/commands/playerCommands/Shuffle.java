package app.commands.playerCommands;

import app.Admin;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@link Command} class and is used to
 * shuffle a playlist or album.
 */
public class Shuffle extends Command {
    private final User user;
    private final Integer seed;
    public Shuffle(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
        seed = commandInput.getSeed();
    }

    /**
     * Executes a Shuffle command and sets the output.
     */
    public void execute() {
        String message = shuffle();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Implementation of the Shuffle command.
     * @return message
     */
    public String shuffle() {
        if (!user.isOnline()) {
            return user.getUsername() + " is offline.";
        }

        if (user.getPlayer().getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!user.getPlayer().getType().equals("playlist")
                && !user.getPlayer().getType().equals("album")) {
            return "The loaded source is not a playlist or an album."; // or album
        }

        user.getPlayer().shuffle(seed);

        if (user.getPlayer().getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }
}
