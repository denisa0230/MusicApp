package app.searchBar.searchStrategies;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.LibraryEntry;
import app.searchBar.Filters;
import app.searchBar.SearchStrategy;
import app.searchBar.Searchable;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the {@code SearchStrategy} interface and provides a method to search for podcasts
 * based on specified filters such as name and owner.
 */
public class SearchPodcast implements SearchStrategy {

    /**
     * Searches for podcasts based on the provided filters.
     * @param filters the filters to apply to the search
     * @return a list of searchable items matching the search criteria
     */
    @Override
    public List<Searchable> search(final Filters filters) {
        List<LibraryEntry> entries = new ArrayList<>();
        for (Podcast podcast : Admin.getInstance().getPodcasts()) {
            if (podcast.matchesName(filters.getName())
                    && podcast.matchesOwner(filters.getOwner())) {
                entries.add(podcast);
            }
        }
        return new ArrayList<>(entries);
    }
}
