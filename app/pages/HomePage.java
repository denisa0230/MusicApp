package app.pages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static app.utils.Utils.LIMIT;

/**
 * This page class extends the {@code Page} class and provides specific details
 * about a user's top 5 liked songs and followed playlists.
 */
public class HomePage extends Page {

    /**
     * The user associated with the home page.
     */
    private User user;

    /**
     * The list of liked songs for the home page.
     */
    @Getter
    private List<String> likedSongs = new ArrayList<>();

    /**
     * The list of followed playlists for the home page.
     */
    @Getter
    private List<String> followedPlaylists = new ArrayList<>();

    @Getter
    private List<String> songRecommandations = new ArrayList<>();

    @Getter
    private List<String> playlistRecommandations = new ArrayList<>();

    /**
     * Constructs a {@code HomePage} object with the given user.
     *
     * @param user the user for the home page
     */
    public HomePage(final User user) {
        this.user = user;
        setType("home");
    }

    /**
     * Generates a formatted representation of the home page.
     *
     * @return a formatted string representing the home page
     */
    @Override
    public String pageFormat() {
        setLikedSongs(user.getLikedSongs());
        setFollowedPlaylists(user.getFollowedPlaylists());
        return "Liked songs:\n\t" + likedSongs
                +  "\n\nFollowed playlists:\n\t" + followedPlaylists
                + "\n\nSong recommendations:\n\t" + songRecommandations
                + "\n\nPlaylists recommendations:\n\t" + playlistRecommandations;
    }

    /**
     * Sets the followed playlists for the home page, considering a limited number of top playlists.
     *
     * @param followedPlaylists the list of followed playlists
     */
    private void setFollowedPlaylists(final ArrayList<Playlist> followedPlaylists) {
        List<Playlist> sortedPlaylists = new ArrayList<>(followedPlaylists);
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        this.followedPlaylists = topPlaylists;
    }

    /**
     * Sets the liked songs for the home page, considering a limited number of top songs.
     *
     * @param likedSongs the list of liked songs
     */
    private void setLikedSongs(final ArrayList<Song> likedSongs) {
        List<Song> sortedSongs = new ArrayList<>(likedSongs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        this.likedSongs = topSongs;
    }
}
