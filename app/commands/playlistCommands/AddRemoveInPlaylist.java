package app.commands.playlistCommands;

import app.Admin;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@link Command} class and is used to execute and update the contents of
 * a specified playlist for a given user by adding or removing a song.
 */
public class AddRemoveInPlaylist extends Command {

    /**
     * The user for whom the song is added or removed from the playlist (receiver of the command).
     */
    private User user;

    /**
     * The ID of the playlist to which the song is added or removed.
     */
    private int id;

    public AddRemoveInPlaylist(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
        id = commandInput.getPlaylistId();
    }

    /**
     * Executes the AddRemoveInPlaylist command and sets the output.
     */
    public void execute() {
        String message = addRemoveInPlaylist();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Implementation of the AddRemoveInPlaylist command.
     * @return A message indicating the result of the add/remove operation in the playlist.
     */
    public String addRemoveInPlaylist() {
        if (!user.isOnline()) {
            return user.getUsername() + " is offline.";
        }
        if (user.getPlayer().getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (user.getPlayer().getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > user.getPlaylists().size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = user.getPlaylists().get(id - 1);
        Song currentSong = (Song) user.getPlayer().getCurrentAudioFile();
        if (playlist.containsSong(currentSong)) {
            playlist.removeSong(currentSong);
            return "Successfully removed from playlist.";
        }

        playlist.addSong(currentSong);
        return "Successfully added to playlist.";
    }
}
