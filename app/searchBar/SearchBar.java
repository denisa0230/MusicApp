package app.searchBar;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Search bar.
 * the context for the search strategy
 */
public final class SearchBar {
    @Getter
    @Setter
    private List<Searchable> results;
    public static final Integer MAX_RESULTS = 5;

    @Setter
    @Getter
    private String lastSearchType;

    @Getter
    private Searchable lastSelected;

    @Setter
    @Getter
    private SearchStrategy searchStrategy;


    public SearchBar(final String user) {
        this.results = new ArrayList<>();
    }

    /**
     * Clear selection.
     */
    public void clearSelection() {
        lastSelected = null;
        lastSearchType = null;
    }


    /**
     * Search list.
     * @param filters the filters
     * @return the list
     */
    public List<String> search(final Filters filters) {
        List<Searchable> entries = searchStrategy.search(filters);

        if (entries.size() > MAX_RESULTS) {
            entries = entries.subList(0, MAX_RESULTS); // truncate results
        }
        this.results = entries;

        // Return names only
        return entries.stream().map(Searchable::getItemName).collect(Collectors.toList());
    }

    /**
     * Select item.
     * @param itemNumber the item number
     * @return the selected item from the search list
     */
    public Searchable select(final Integer itemNumber) {
        if (this.results.size() < itemNumber) {
            results.clear();
            return null;
        }
        lastSelected =  this.results.get(itemNumber - 1);
        results.clear();
        return lastSelected;
    }
}
