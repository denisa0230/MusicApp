package app.pages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * This page class extends the {@code Page} class and provides details about the user's liked songs
 * and followed playlists.
 */
@Getter
public class LikedContent extends Page {
    /**
     * The user associated with the liked content page.
     */
    @Setter
    private User user;

    /**
     * The list of liked songs for the liked content page.
     */
    private List<Song> likedSongs = new ArrayList<>();

    /**
     * The list of followed playlists for the liked content page.
     */
    private List<Playlist> followedPlaylists = new ArrayList<>();

    public LikedContent(final User user) {
        this.user = user;
        setType("likedContent");
    }

    /**
     * Sets the followed playlists for the liked content page.
     * @param followedPlaylists the list of followed playlists
     */
    public void setFollowedPlaylists(final ArrayList<Playlist> followedPlaylists) {
        this.followedPlaylists = followedPlaylists;
    }

    /**
     * Sets the liked songs for the liked content page.
     * @param likedSongs the list of liked songs
     */
    public void setLikedSongs(final ArrayList<Song> likedSongs) {
        this.likedSongs = likedSongs;
    }

    /**
     * Generates a formatted representation of the liked content page.
     * @return a formatted string representing the liked content page
     */
    @Override
    public String pageFormat() {
        setLikedSongs(user.getLikedSongs());
        setFollowedPlaylists(user.getFollowedPlaylists());
        return "Liked songs:\n\t" + likedSongs.toString()
                + "\n\nFollowed playlists:\n\t" + followedPlaylists.toString();
    }
}
