package app.commands.playerCommands;

import app.Admin;
import app.audio.Files.Song;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;


/**
 * This command extends the {@link Command} class and is used to
 * like a song.
 */
public class Like extends Command {
    private final User user; // receiver
    public Like(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
    }

    /**
     * Executes a Like command and sets the output.
     */
    public void execute() {
        String message = like();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Implementation of the like command.
     * @return message
     */
    public String like() {
        if (!user.isOnline()) {
            return user.getUsername() + " is offline.";
        }

        if (user.getPlayer().getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!user.getPlayer().getType().equals("song")
                && !user.getPlayer().getType().equals("playlist")
                && !user.getPlayer().getType().equals("album")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) user.getPlayer().getCurrentAudioFile();

        if (user.getLikedSongs().contains(song)) {
            user.getLikedSongs().remove(song);
            song.dislike();
            return "Unlike registered successfully.";
        }

        user.getLikedSongs().add(song);
        song.like();
        return "Like registered successfully.";
    }
}
