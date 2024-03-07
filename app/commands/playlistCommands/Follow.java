package app.commands.playlistCommands;

import app.Admin;
import app.audio.Collections.Playlist;
import app.audio.LibraryEntry;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@link Command} class and is used to execute the following
 * or unfollowing action on a playlist for a given user.
 */
public final class Follow extends Command {

    /**
     * The user who wants to follow/unfollow a playlist.
     */
    private final User user;

    public Follow(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
    }

    /**
     * Executes the Follow command and sets the output with the generated message.
     */
    public void execute() {
        String message = follow();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Implementation of the Follow command.
     * @return A message indicating the result of the follow or unfollow operation on the playlist.
     */
    public String follow() {
        // Check if the user is online
        if (!user.isOnline()) {
            return user.getUsername() + " is offline.";
        }

        // Get the last selected item from the user's search bar
        LibraryEntry selection = (LibraryEntry) user.getSearchBar().getLastSelected();
        String type = user.getSearchBar().getLastSearchType();

        // Check if a source is selected and if it's a playlist
        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        // Cast the selection to a Playlist
        Playlist playlist = (Playlist) selection;

        // Check if the user is trying to follow/unfollow their own playlist
        if (playlist.getOwner().equals(user.getUsername())) {
            return "You cannot follow or unfollow your own playlist.";
        }

        // Perform the follow/unfollow action and update followers accordingly
        if (user.getFollowedPlaylists().contains(playlist)) {
            user.getFollowedPlaylists().remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        user.getFollowedPlaylists().add(playlist);
        playlist.increaseFollowers();

        return "Playlist followed successfully.";
    }
}
