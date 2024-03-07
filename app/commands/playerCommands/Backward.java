package app.commands.playerCommands;

import app.Admin;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@link Command} class and is used to
 * move the player 90 seconds backwards.
 */
public class Backward extends Command {
    private final User user; // receiver
    public Backward(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
    }

    /**
     * Executes a Backward command and sets the output.
     */
    public void execute() {
        String message = backward();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Implementation of the Backward command.
     * @return message
     */
    public String backward() {
        if (!user.isOnline()) {
            return user.getUsername() + " is offline.";
        }

        if (user.getPlayer().getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!user.getPlayer().getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        user.getPlayer().skipPrev();

        return "Rewound successfully.";
    }
}
