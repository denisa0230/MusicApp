package app.searchBar.searchStrategies;

import app.Admin;
import app.audio.Collections.Playlist;
import app.audio.LibraryEntry;
import app.searchBar.Filters;
import app.searchBar.SearchStrategy;
import app.searchBar.Searchable;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the {@code SearchStrategy} interface and provides a method to search for playlists
 * based on specified filters such as name, owner, followers, and user visibility.
 */
public class SearchPlaylist implements SearchStrategy {

    /**
     * The username of the user initiating the search.
     */
    private String user;

    public SearchPlaylist(final String user) {
        this.user = user;
    }

    /**
     * Searches for playlists based on the provided filters.
     * @param filters the filters to apply to the search
     * @return a list of searchable items matching the search criteria
     */
    @Override
    public List<Searchable> search(final Filters filters) {
        List<LibraryEntry> entries = new ArrayList<>();
        for (Playlist playlist : Admin.getInstance().getPlaylists()) {
            if (playlist.isVisibleToUser(user)
                    && playlist.matchesName(filters.getName())
                    && playlist.matchesOwner(filters.getOwner())
                    && playlist.matchesFollowers(filters.getFollowers())) {
                entries.add(playlist);
            }
        }
        return new ArrayList<>(entries);
    }
}
