package app.commands.playlistCommands;

import app.Admin;
import app.audio.Collections.Playlist;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@link Command} class and is used to create a new
 * playlist for a given user.
 */
public class CreatePlaylist extends Command {

    /**
     * The user for whom the playlist is created (receiver of the command).
     */
    private final User user;

    /**
     * The name of the playlist to be created.
     */
    private final String name;

    /**
     * The timestamp associated with the playlist creation.
     */
    private final int timestamp;

    public CreatePlaylist(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
        name = commandInput.getPlaylistName();
        timestamp = commandInput.getTimestamp();
    }

    /**
     * Executes the CreatePlaylist command and sets the output with the generated message.
     */
    public void execute() {
        String message = createPlaylist();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Implementation of the CreatePlaylist command.
     * @return A message indicating the result of the playlist creation.
     */
    public String createPlaylist() {
        if (!user.isOnline()) {
            return user.getUsername() + " is offline.";
        }
        if (user.getPlaylists().stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        user.getPlaylists().add(new Playlist(name, user.getUsername(), timestamp));

        return "Playlist created successfully.";
    }
}
