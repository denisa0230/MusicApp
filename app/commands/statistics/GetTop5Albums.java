package app.commands.statistics;

import app.Admin;
import app.audio.Collections.Album;
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
 * the top 5 liked albums and provide the result in the output.
 */
public final class GetTop5Albums extends Command {

    /**
     * The list of albums used for top albums calculation.
     */
    private static List<Album> albums;

    public GetTop5Albums(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        albums = Admin.getInstance().getAlbums();
    }

    /**
     * Executes the GetTop5Albums command and sets the output with the generated result.
     */
    @Override
    public void execute() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(getTop5Albums()));

        this.setOutput(objectNode);
    }

    /**
     * Retrieves the top 5 liked albums.
     * @return A list of names of the top 5 liked albums.
     */
    public List<String> getTop5Albums() {
        albums.forEach(album -> album.setLikes());
        List<Album> sortedAlbums = new ArrayList<>(albums);
        sortedAlbums.sort(Comparator.comparingInt(Album::getLikes)
                .reversed()
                .thenComparing(Album::getName, Comparator.naturalOrder()));
        List<String> topAlbums = new ArrayList<>();
        int count = 0;
        for (Album album : sortedAlbums) {
            if (count >= LIMIT) {
                break;
            }
            topAlbums.add(album.getName());
            count++;
        }
        return topAlbums;
    }
}
