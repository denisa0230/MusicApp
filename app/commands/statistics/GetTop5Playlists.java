package app.commands.statistics;

import app.Admin;
import app.audio.Collections.Playlist;
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
 * the top 5 followed playlists and provide the result in the output.
 */
public class GetTop5Playlists extends Command {

    /**
     * The list of playlists used for top playlists calculation.
     */
    private static List<Playlist> playlists;

    public GetTop5Playlists(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        playlists = Admin.getInstance().getPlaylists();
    }

    /**
     * Executes the GetTop5Playlists command and sets the output with the generated result.
     */
    public void execute() {
        List<String> topPlaylists = getTop5Playlists();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(topPlaylists));

        this.setOutput(objectNode);
    }

    /**
     * Gets the top 5 followed playlists.
     * @return A list of names of the top 5 followed playlists.
     */
    public List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(playlists);
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }
}
