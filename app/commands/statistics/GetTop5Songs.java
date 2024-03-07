package app.commands.statistics;

import app.Admin;
import app.audio.Files.Song;
import app.commands.Command;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static app.utils.Utils.LIMIT;

/**
 * This command extends the {@link Command} class and is used to extract
 * of the top 5 liked songs and provide the result in the output.
 */
public class GetTop5Songs extends Command {
    /**
     * The list of songs used for top songs calculation.
     */
    private List<Song> songs;

    public GetTop5Songs(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        this.songs = Admin.getInstance().getSongs();
    }

    /**
     * Executes the GetTop5Songs command and sets the output with the generated result.
     */
    public void execute() {
        List<String> topSongs = getTop5Songs();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(topSongs));

        this.setOutput(objectNode);
    }
    /**
     * Gets top 5 songs.
     * @return the top 5 songs
     */
    public List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }
}
