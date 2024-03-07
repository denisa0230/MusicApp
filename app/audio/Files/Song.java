package app.audio.Files;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Song.
 */
@Getter
public final class Song extends AudioFile {
    private final String album;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final Integer releaseYear;
    private final String artist;
    private Integer likes;
    @Setter
    private Double revenue;


    /**
     * Instantiates a new Song.
     *
     * @param name        the name
     * @param duration    the duration
     * @param album       the album
     * @param tags        the tags
     * @param lyrics      the lyrics
     * @param genre       the genre
     * @param releaseYear the release year
     * @param artist      the artist
     */
    public Song(final String name, final Integer duration, final String album,
                final ArrayList<String> tags, final String lyrics, final String genre,
                final Integer releaseYear, final String artist) {
        super(name, duration);
        this.album = album;
        this.tags = tags;
        this.lyrics = lyrics;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.artist = artist;
        this.likes = 0;
        this.revenue = 0.0;
    }
    public Song(final Song song) {
        this(song.getName(), song.getDuration(), song.album, song.tags,
                song.lyrics, song.genre, song.releaseYear, song.artist);
    }

    @Override
    public boolean matchesAlbum(final String albumName) {
        return albumName == null || this.getAlbum().equalsIgnoreCase(albumName);
    }

    @Override
    public boolean matchesTags(final ArrayList<String> tagsList) {
        if (tagsList == null) {
            return true;
        }
        List<String> songTags = new ArrayList<>();
        for (String tag : this.getTags()) {
            songTags.add(tag.toLowerCase());
        }

        for (String tag : tagsList) {
            if (!songTags.contains(tag.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
    @Override
    public boolean matchesLyrics(final String lyricFilter) {
        return lyricFilter == null
                || this.getLyrics().toLowerCase().contains(lyricFilter.toLowerCase());
    }

    @Override
    public boolean matchesGenre(final String genreFilter) {
        return genreFilter == null || this.getGenre().equalsIgnoreCase(genreFilter);
    }

    @Override
    public boolean matchesArtist(final String artistFilter) {
        return artistFilter == null || this.getArtist().equalsIgnoreCase(artistFilter);
    }

    @Override
    public boolean matchesReleaseYear(final String releaseYearFilter) {
        return releaseYearFilter == null || filterByYear(this.getReleaseYear(), releaseYearFilter);
    }

    private static boolean filterByYear(final int year, final String query) {
        if (query.startsWith("<")) {
            return year < Integer.parseInt(query.substring(1));
        } else if (query.startsWith(">")) {
            return year > Integer.parseInt(query.substring(1));
        } else {
            return year == Integer.parseInt(query);
        }
    }

    /**
     * Like.
     */
    public void like() {
        likes++;
    }

    /**
     * Dislike.
     */
    public void dislike() {
        likes--;
    }

    @Override
    public String toString() {
        return  getName() + " - " + artist;
    }

    /**
     * Increase song revenue
     * @param sum the new profit made by the song
     */
    public void increaseRevenue(final Double sum) {
        revenue += sum;
    }
}
