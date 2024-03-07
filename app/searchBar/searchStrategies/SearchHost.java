package app.searchBar.searchStrategies;

import app.Admin;
import app.searchBar.Filters;
import app.searchBar.SearchStrategy;
import app.searchBar.Searchable;
import app.user.Host;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the {@code SearchStrategy} interface and provides a method to search for hosts
 * based on specified filters such as name.
 */
public class SearchHost implements SearchStrategy {

    /**
     * Searches for hosts based on the provided filters.
     * @param filters the filters to apply to the search
     * @return a list of searchable items matching the search criteria
     */
    @Override
    public List<Searchable> search(final Filters filters) {
        List<Host> entries = new ArrayList<>();
        for (Host host : Admin.getInstance().getHosts()) {
            if (host.matchesName(filters.getName())) {
                entries.add(host);
            }
        }
        return new ArrayList<>(entries);
    }
}
