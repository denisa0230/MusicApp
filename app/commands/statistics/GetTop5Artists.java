package app.commands.statistics;

import app.Admin;
import app.commands.Command;
import app.user.Artist;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static app.utils.Utils.LIMIT;

/**
 * This command extends the {@link Command} class and is used to extract
 * the top 5 liked artists and provide the result in the output.
 */
public class GetTop5Artists extends Command {

    /**
     * The list of artists used for top artists calculation.
     */
    private List<Artist> artists;

    public GetTop5Artists(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        artists = Admin.getInstance().getArtists();
    }

    /**
     * Executes the GetTop5Artists command and sets the output with the generated result.
     */
    @Override
    public void execute() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(getTop5Artists()));

        this.setOutput(objectNode);
    }

    /**
     * Retrieves the top 5 liked artists.
     * @return A list of usernames of the top 5 liked artists.
     */
    public List<String> getTop5Artists() {
        List<Artist> sortedArtists = new ArrayList<>(artists);
        sortedArtists.sort(Comparator.comparingInt(Artist::getLikes)
                .reversed());
        List<String> topArtists = new ArrayList<>();
        int count = 0;
        for (Artist artist : sortedArtists) {
            if (count >= LIMIT) {
                break;
            }
            topArtists.add(artist.getUsername());
            count++;
        }
        return topArtists;
    }
}
