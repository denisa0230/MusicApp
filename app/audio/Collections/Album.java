package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import lombok.Getter;

import java.util.ArrayList;

public final class Album  extends AudioCollection {
    private int releaseYear;
    private String description;

    @Getter
    private ArrayList<Song> songs;

    @Getter
    private int likes;

    public Album(final String name, final String owner, final int releaseYear,
                 final String description, final ArrayList<Song> songs) {
        super(name, owner);
        this.releaseYear = releaseYear;
        this.description = description;
        this.songs = songs;
    }

    /**
     * Calculates the likes of an album as a sum
     * of the likes received by the songs it contains.
     */
    public void setLikes() {
        int sum = 0;
        for (Song song : songs) {
            sum += song.getLikes();
        }
        likes = sum;
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    @Override
    public boolean matchesDescription(final String descriptionToMatch) {
        return descriptionToMatch == null || this.description.equals(descriptionToMatch);
    }
}
