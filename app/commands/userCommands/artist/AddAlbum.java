package app.commands.userCommands.artist;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.commands.Command;
import app.user.Artist;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import fileio.input.SongInput;
import java.util.ArrayList;


/**
 * This command extends the {@link Command} class and is used to add a new album
 * to an artist's list of albums.
 */
public class AddAlbum extends Command {
    private String username;
    private ArrayList<Song> songs;
    private final String name;
    private final int releaseYear;
    private final String description;

    public AddAlbum(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        username = commandInput.getUsername();
        setSongs(commandInput.getSongs());
        name = commandInput.getName();
        releaseYear = commandInput.getReleaseYear();
        description = commandInput.getDescription();
    }

    /**
     * Sets the list of songs for the new album based on the provided song input list.
     * @param songInputList The list of song inputs for the new album.
     */
    public void setSongs(final ArrayList<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    /**
     * Executes the AddAlbum command and sets the output with the generated result.
     */
    public void execute() {
        String message = addAlbum();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Adds a new album to the artist's collection based on the provided input.
     * @return A message indicating the success or failure of the addition.
     */
    public String addAlbum() {
        if (!Admin.getInstance().userExists(username)) {
            return "The username " + username + " doesn't exist.";
        }
        if (Admin.getInstance().getArtist(username) == null) {
            return username + " is not an artist.";
        }
        Artist artist = Admin.getInstance().getArtist(username);
        if (artist.getAlbums().stream().map(LibraryEntry::getName).toList().contains(name)) {
            return artist.getUsername() + " has another album with the same name.";
        }

        // Check if the list of songs contains duplicates.
        ArrayList<String> uniqueSongs = new ArrayList<>();
        for (Song song : songs) {
            if (uniqueSongs.contains(song.getName())) {
                return artist.getUsername() + " has the same song at least twice in this album.";
            }
            uniqueSongs.add(song.getName());
        }
        Album newAlbum = new Album(name, artist.getUsername(), releaseYear, description, songs);
        artist.getAlbums().add(newAlbum);
        Admin.getInstance().getAlbums().add(newAlbum);
        Admin.getInstance().getSongs().addAll(songs);

        // notify subscribers
        artist.notifySubscribers("New Album");

        return artist.getUsername() + " has added new album successfully.";
    }
}
