package app.searchBar.searchStrategies;

import app.Admin;
import app.audio.Collections.Album;
import app.searchBar.Filters;
import app.searchBar.SearchStrategy;
import app.searchBar.Searchable;
import app.user.Artist;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the {@code SearchStrategy} interface and provides a method to search for albums
 * based on specified filters such as description, owner, and name.
 */
public class SearchAlbum implements SearchStrategy {

    /**
     * Searches for albums based on the provided filters.
     * @param filters the filters to apply to the search
     * @return a list of searchable items matching the search criteria
     */
    @Override
    public List<Searchable> search(final Filters filters) {
        List<Album> entries = new ArrayList<>();

        for (Album entry : Admin.getInstance().getAlbums()) {
            if (entry.matchesDescription(filters.getDescription())
                    && entry.matchesOwner(filters.getOwner())
                    && entry.matchesName(filters.getName())) {
                entries.add(entry);
            }
        }

        /*
            Sort the albums that fit the search tags according
            to the order of the artists added on the platform
         */
        entries.sort((o1, o2) -> {
            int idx1 = Admin.getInstance().getArtists().stream()
                    .map(Artist::getUsername)
                    .toList()
                    .indexOf(o1.getOwner());
            int idx2 = Admin.getInstance().getArtists().stream()
                    .map(Artist::getUsername)
                    .toList()
                    .indexOf(o2.getOwner());
            return idx1 - idx2;
        });

        return new ArrayList<>(entries);
    }
}
