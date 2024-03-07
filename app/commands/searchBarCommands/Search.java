package app.commands.searchBarCommands;

import app.Admin;
import app.commands.Command;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.searchBar.searchStrategies.SearchPlaylist;
import app.searchBar.searchStrategies.SearchAlbum;
import app.searchBar.searchStrategies.SearchArtist;
import app.searchBar.searchStrategies.SearchHost;
import app.searchBar.searchStrategies.SearchPodcast;
import app.searchBar.searchStrategies.SearchSong;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.List;

/**
 * This command extends the {@link Command} class and is used to execute a search
 * for various types such as songs, playlists, podcasts, albums, artists, and hosts.
 */
public final class Search extends Command {

    /**
     * The user for whom the search is performed (receiver of the command).
     */
    private User user;

    /**
     * The filters to be applied during the search.
     */
    private Filters filters;

    /**
     * The type of the search (e.g., song, playlist, podcast).
     */
    private String type;

    public Search(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
        filters = new Filters(commandInput.getFilters());
        type = commandInput.getType();
    }

    /**
     * Executes the Search command and sets the output with the generated message and results.
     */
    public void execute() {
        ObjectMapper objectMapper = new ObjectMapper(); // creates output
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (!user.isOnline()) {
            objectNode.put("message", user.getUsername() + " is offline.");
            objectNode.put("results", objectMapper.valueToTree(new ArrayList<>()));
        } else {
            List<String> results = search();
            objectNode.put("message", "Search returned " + results.size() + " results");
            objectNode.put("results", objectMapper.valueToTree(results));
        }

        this.setOutput(objectNode);
    }

    /**
     * Performs the search based on the specified type.
     * @return A list of search results.
     */
    public List<String> search() {
        user.resetOnSearch(type);
        SearchBar searchBar = user.getSearchBar();

        // identify concrete search command, pass it to the context and extract only the results
        switch (type) {
            case "song":
                searchBar.setSearchStrategy(new SearchSong());
                break;
            case "playlist":
                searchBar.setSearchStrategy(new SearchPlaylist(user.getUsername()));
                break;
            case "podcast":
                searchBar.setSearchStrategy(new SearchPodcast());
                break;
            case "album":
                searchBar.setSearchStrategy(new SearchAlbum());
                break;
            case "artist":
                searchBar.setSearchStrategy(new SearchArtist());
                break;
            case "host":
                searchBar.setSearchStrategy(new SearchHost());
                break;
            default:
                break;
        }
        return searchBar.search(filters);
    }
}
