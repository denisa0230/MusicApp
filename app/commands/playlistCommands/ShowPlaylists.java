package app.commands.playlistCommands;

import app.Admin;
import app.audio.Collections.Playlist;
import app.audio.Collections.PlaylistOutput;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;

/**
 * This command extends the {@link Command} class and is used to execute and retrieve the
 * playlists associated with a specific user.
 */
public final class ShowPlaylists extends Command {

    /**
     * The user for whom the playlists are displayed (receiver of the command).
     */
    private User user;

    public ShowPlaylists(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
    }

    /**
     * Executes the ShowPlaylists command and sets the output.
     */
    public void execute() {
        ArrayList<PlaylistOutput> playlists = showPlaylists();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        this.setOutput(objectNode);
    }

    /**
     * Retrieves and returns the user's playlists.
     * @return The list of PlaylistOutput objects representing the user's playlists.
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : user.getPlaylists()) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }
        return playlistOutputs;
    }
}
