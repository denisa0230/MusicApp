package app.commands.userCommands.artist;

import app.Admin;
import app.commands.Command;
import app.user.Artist;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import app.audio.Files.Song;

import java.util.Iterator;

/**
 * This command extends the {@code Command} class and represents
 * a command for removing an album owned by an artist.
 */
public class RemoveAlbum extends Command {
    private final String username;
    private final String albumName;

    public RemoveAlbum(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        username = commandInput.getUsername();
        albumName = commandInput.getName();
    }

    /**
     * Executes the command to remove an album and generates the output message.
     */
    @Override
    public void execute() {
        String message = removeAlbum();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Removes the specified album associated with the artist user.
     * @return A message indicating the success or failure of the operation.
     */
    public String removeAlbum() {
        if (!Admin.getInstance().userExists(username)) {
            return "The username " + username + " doesn't exist.";
        }
        if (Admin.getInstance().getArtist(username) == null) {
            return username + " is not an artist.";
        }
        Artist artist = Admin.getInstance().getArtist(username);
        if (!artist.getAlbumNames().contains(albumName)) {
            return artist.getUsername() + " doesn't have an album with the given name.";
        }
        if (Admin.getInstance().albumIsPlayed(albumName)) {
            return artist.getUsername() + " can't delete this album.";
        }

        Iterator<Song> iterator = Admin.getInstance().getSongs().iterator();
        while (iterator.hasNext()) {
            Song song = iterator.next();
            if (song.getAlbum().equals(albumName)) {
                iterator.remove();
                Admin.getInstance().removeSongFromLibrary(song);
            }
        }
        artist.getAlbums().removeIf(album -> album.getName().equals(albumName));
        Admin.getInstance().getAlbums().removeIf(album -> album.getName().equals(albumName));
        return artist.getUsername() + " deleted the album successfully.";
    }
}
