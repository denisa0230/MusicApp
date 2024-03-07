package app.commands.userCommands.artist;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AlbumOutput;
import app.commands.Command;
import app.user.Artist;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;

/**
 * This command extends the {@code Command} class and represents
 * a command for retrieving and displaying the albums associated with an artist user.
 */
public class ShowAlbums extends Command {
    private final Artist artist;

    public ShowAlbums(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        artist = Admin.getInstance().getArtist(commandInput.getUsername());
    }

    /**
     * Executes the command and generates the output message.
     */
    public void execute() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(showAlbums()));

        this.setOutput(objectNode);
    }

    /**
     * Creates AlbumOutput objects for the artist's albums.
     * @return the artist's albums.
     */
    public ArrayList<AlbumOutput> showAlbums() {
        ArrayList<Album> albums = artist.getAlbums();
        ArrayList<AlbumOutput> results = new ArrayList<>();
        for (Album album : albums) {
            results.add(new AlbumOutput(album));
        }
        return results;
    }
}
