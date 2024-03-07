package app.searchBar;

import java.util.List;

/**
 * Provides a common interface for the concrete search strategies
 * implemented in the searchStrategies package.
 */
public interface SearchStrategy {
    /**
     * Performs the search operation on a given set of filters.
     * @param filters the search filters
     * @return list of search results
     */
    List<Searchable> search(Filters filters);
}
