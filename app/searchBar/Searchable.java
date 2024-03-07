package app.searchBar;

/**
 * Provides a common interface for objects upon which the
 * search operation can be performed.
 */
public interface Searchable {
    /**
     * The method is used for outputting the results
     * of a search operation.
     * @return the name of a searchable item
     */
    String getItemName();
}
