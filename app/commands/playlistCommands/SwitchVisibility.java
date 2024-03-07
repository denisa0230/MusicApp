package app.commands.playlistCommands;

import app.Admin;
import app.audio.Collections.Playlist;
import app.commands.Command;
import app.user.User;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@link Command} class and is used to
 * switch the visibility of a specified playlist for a given user.
 */
public final class SwitchVisibility extends Command {

    /**
     * The user for whom the playlist visibility is switched (receiver of the command).
     */
    private User user;

    public SwitchVisibility(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
    }

    /**
     * Executes the SwitchVisibility command and sets the output with the generated message.
     */
    public void execute() {
        String message = switchPlaylistVisibility(commandInput.getPlaylistId());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Change the visibility of the specified playlist for the user.
     * @param playlistId The ID of the playlist
     * @return A message indicating the result of the operation.
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (!user.isOnline()) {
            return user.getUsername() + " is offline.";
        }
        if (playlistId > user.getPlaylists().size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = user.getPlaylists().get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Utils.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }
}
