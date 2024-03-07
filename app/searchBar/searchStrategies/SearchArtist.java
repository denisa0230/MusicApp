package app.searchBar.searchStrategies;

import app.Admin;
import app.searchBar.Filters;
import app.searchBar.SearchStrategy;
import app.searchBar.Searchable;
import app.user.Artist;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the {@code SearchStrategy} interface and provides a method to search for artists
 * based on specified filters such as name.
 */
public class SearchArtist implements SearchStrategy {

    /**
     * Searches for artists based on the provided filters.
     * @param filters the filters to apply to the search
     * @return a list of searchable items matching the search criteria
     */
    @Override
    public List<Searchable> search(final Filters filters) {
        List<Artist> entries = new ArrayList<>();
        for (Artist artist : Admin.getInstance().getArtists()) {
            if (artist.matchesName(filters.getName())) {
                entries.add(artist);
            }
        }
        return new ArrayList<>(entries);
    }
}
