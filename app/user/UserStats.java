package app.user;

import lombok.Getter;
import lombok.Setter;
import java.util.*;

import static app.utils.Utils.LIMIT;


/**
 * This class encapsulates all possible types of statistics for a user.
 * It is used primarily for the {@code Wrapped} command.
 */
@Getter
@Setter
public final class UserStats {
     HashMap<String, Integer> topArtists = new HashMap<>();
     HashMap<String, Integer> topGenres = new HashMap<>();
     HashMap<String, Integer> topSongs = new HashMap<>();
     HashMap<String, Integer> topAlbums = new HashMap<>();
     HashMap<String, Integer> topEpisodes = new HashMap<>();
     ArrayList<String> topFans = new ArrayList<>();
     HashMap<String, Integer> listenersList = new HashMap<>();
     Integer listeners = 0;

    /**
     * Adds an element into one of the stats lists or, if it already exists,
     * increases the number of listens for it.
     * @param hashMap the stats mapping
     * @param nameToAdd the name of the object to be added in the list
     */
    public void addToMapping(final HashMap<String, Integer> hashMap,
                             final String nameToAdd) {
         if (hashMap.containsKey(nameToAdd)) {
            hashMap.put(nameToAdd, hashMap.get(nameToAdd) + 1);
            return;
         }
         hashMap.put(nameToAdd, 1);
    }

    /**
     * Get top 5 fans from listeners' list.
     *
     * @return the list of fans
     */
    public ArrayList<String> getTopFans() {
        if (topFans == null) {
            return null;
        }
        HashMap<String, Integer> top = sortTop(listenersList);
        return new ArrayList<>(top.keySet());
    }

    public HashMap<String, Integer> getTopArtists() {
        return sortTop(topArtists);
    }
    public HashMap<String, Integer> getTopAlbums() {
        return sortTop(topAlbums);
    }
    public HashMap<String, Integer> getTopEpisodes() {
        return sortTop(topEpisodes);
    }
    public HashMap<String, Integer> getTopSongs() {
        return sortTop(topSongs);
    }
    public HashMap<String, Integer> getTopGenres() {
        return sortTop(topGenres);
    }

    /**
     * Gets listeners count.
     * @return listeners count
     */
    public Integer getListeners() {
        return listenersList.size();
    }

    /**
     * Sorts a given hashmap, first by value (in descending order),
     * then by key (in lexicographical order) and returns top 5 results.
     * @param top hashmap to sort
     * @return sorted hashmap
     */
    public HashMap<String, Integer> sortTop(final HashMap<String, Integer> top) {
        if (top == null) {
            return null;
        }
        // Sort list
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(top.entrySet());
        list.sort((o1, o2) -> {
            if (o2.getValue().compareTo(o1.getValue()) != 0) {
                return o2.getValue() - o1.getValue();
            }
            return o1.getKey().compareTo(o2.getKey());
        });

        // Extract top 5
        LinkedHashMap<String, Integer> result = new LinkedHashMap<>();
        if (list.size() < LIMIT) {
            list.forEach(entry -> result.put(entry.getKey(), entry.getValue()));
            return result;
        } else {
            list.subList(0, LIMIT).forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    /**
     * Checks if a user has no stats to display.
     * @return true if the user has no stats, false otherwise
     */
    public boolean noStats() {
        return topArtists.isEmpty()
                && topGenres.isEmpty()
                && topSongs.isEmpty()
                && topAlbums.isEmpty()
                && topEpisodes.isEmpty()
                && listenersList.isEmpty();
    }
}
