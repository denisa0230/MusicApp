package app.searchBar.searchStrategies;

import app.Admin;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.searchBar.Filters;
import app.searchBar.SearchStrategy;
import app.searchBar.Searchable;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the {@code SearchStrategy} interface and provides a method to search for songs
 * based on specified filters such as name, tags, album, lyrics, genre, release year, and artist.
 */
public class SearchSong implements SearchStrategy {

    /**
     * Searches for songs based on the provided filters.
     * @param filters the filters to apply to the search
     * @return a list of searchable items matching the search criteria
     */
    @Override
    public List<Searchable> search(final Filters filters) {
        List<LibraryEntry> entries = new ArrayList<>();
        for (Song song : Admin.getInstance().getSongs()) {
            if (song.matchesName(filters.getName())
                    && song.matchesTags(filters.getTags())
                    && song.matchesAlbum(filters.getAlbum())
                    && song.matchesLyrics(filters.getLyrics())
                    && song.matchesGenre(filters.getGenre())
                    && song.matchesReleaseYear(filters.getReleaseYear())
                    && song.matchesArtist(filters.getArtist())) {
                entries.add(song);
            }
        }

        return new ArrayList<>(entries);
    }
}
