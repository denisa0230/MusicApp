package app.user;

import app.Admin;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.pages.*;
import app.player.Player;
import app.player.PlayerSource;
import app.player.PlayerStats;
import app.searchBar.SearchBar;
import app.subscriptions.Notification;
import app.subscriptions.Subject;
import app.subscriptions.Subscriber;
import app.user.userFeatures.Recommendation;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

import static app.utils.Utils.*;

/**
 * This class represents a user in the application.
 */
@Getter
@Setter
public class User extends UserAbstract implements Subscriber {
    private ArrayList<Playlist> playlists;
    private ArrayList<Song> likedSongs;
    private ArrayList<Playlist> followedPlaylists;
    private Player player;
    private Page currentPage;
    private HomePage homePage;
    private LikedContent likedContentPage;
    private SearchBar searchBar;
    private boolean lastSearched;
    private boolean online;
    private ArrayList<String> merch = new ArrayList<>();
    private boolean premium;
    private HashMap<Song, Integer> interAdsContent = new HashMap<>();
    private HashMap<Song, Integer> premiumContent = new HashMap<>();
    private ArrayList<Notification> notifications = new ArrayList<>();
    private Recommendation lastRecommendation = null;
    private ArrayList<Page> pageHistory = new ArrayList<>();
    private int currentPageIndex;

    public User(final String username, final int age, final String city) {
        super(username, age, city);
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        player.setUser(this);
        searchBar = new SearchBar(username);
        lastSearched = false;
        online = true;
        homePage = new HomePage(this);
        likedContentPage = new LikedContent(this);
        currentPage = homePage;
        premium = false;
        type = "user";
        lastRecommendation = new Recommendation();
        pageHistory.add(homePage);
        currentPageIndex = 0;
    }


    /**
     * Gets player stats.
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * Simulate time.
     * @param time the time
     */
    public void simulateTime(final int time) {
        player.simulatePlayer(time);
    }


    /**
     * Resets the user's search-related attributes on search.
     * @param type the type of the search
     */
    public void resetOnSearch(final String type) {
        player.stop();
        lastSearched = true;
        searchBar.clearSelection();
        searchBar.setLastSearchType(type);
    }

    /**
     * Checks if the user is currently playing content related to the specified artist.
     * @param artist the artist's username
     * @return true if the user is playing the artist's content, false otherwise
     */
    public boolean isPlayingArtist(final String artist) {
        // Check if the user has searched the artist
        if (searchBar.getLastSearchType() != null
                && searchBar.getLastSearchType().equals("artist")
                && searchBar.getResults().contains(Admin.getInstance().getArtist(artist))) {
            return true;
        }

        // Check if the user is on the artist's page.
        if (currentPage.getType().equals("artist")
                && ((ArtistPage) currentPage).getArtist().getUsername().equals(artist)) {
            return true;
        }
        // Check if the user is playing the artist's content.
        PlayerSource source = player.getSource();
        if (source == null || !online) {
            return false;
        }
        if ((source.getType() == Utils.PlayerSourceType.ALBUM
                    || source.getType() == Utils.PlayerSourceType.PLAYLIST)
                && source.getAudioCollection().getOwner().equals(artist)) {
            return true;
        }
        if (source.getType() == Utils.PlayerSourceType.LIBRARY
                && ((Song) source.getAudioFile()).getArtist().equals(artist)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the user is currently playing the content of a specific host.
     * @param host The username of the host to check.
     * @return True if the user is playing the host's content, false otherwise.
     */
    public boolean isPlayingHost(final String host) {
        // Check if the user has searched the host
        if (searchBar.getLastSearchType() != null
                && searchBar.getLastSearchType().equals("host")
                && searchBar.getResults().contains(Admin.getInstance().getHost(host))) {
            return true;
        }

        // Check if the user is on the host's page.
        if (currentPage.getType().equals("host")
                && ((HostPage) currentPage).getHost().getUsername().equals(host)) {
            return true;
        }
        // Check if the user is playing the host's content.
        PlayerSource source = player.getSource();
        if (source == null || !online) {
            return false;
        }
        if ((source.getType() == Utils.PlayerSourceType.PODCAST
                || source.getType() == Utils.PlayerSourceType.PLAYLIST)
            && source.getAudioCollection().getOwner().equals(host)) {
            return true;
        }
        return false;
    }


    /**
     * Checks if the user is currently playing a specific album or one of its songs.
     * @param name The name of the album to check.
     * @return True if the user is playing the album, false otherwise.
     */
    public boolean isPlayingAlbum(final String name) {
        // Check if the user is playing the album or one of its songs.
        PlayerSource source = player.getSource();
        if (source == null || !online) {
            return false;
        }
        if (source.getType() == Utils.PlayerSourceType.ALBUM
                && source.getAudioCollection().getName().equals(name)) {
            return true;
        }
        if (source.getType() == Utils.PlayerSourceType.LIBRARY
                && ((Song) source.getAudioFile()).getAlbum().equals(name)) {
            return true;
        }

        // If the source is a playlist, check if it contains songs from the album to be deleted.
        if (source.getType() == Utils.PlayerSourceType.PLAYLIST) {
            for (Song song : ((Playlist) source.getAudioCollection()).getSongs()) {
                if (song.getAlbum().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the user is currently playing a specific podcast.
     * @param name The name of the podcast to check.
     * @return True if the user is playing the podcast, false otherwise.
     */
    public boolean isPlayingPodcast(final String name) {
        // Check if the user is playing the podcast.
        PlayerSource source = player.getSource();
        if (source == null || !online) {
            return false;
        }
        if (source.getType() == Utils.PlayerSourceType.PODCAST
                && source.getAudioCollection().getName().equals(name)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the user is currently playing the content of a specific user playlist.
     *
     * @param owner The username of the playlist owner.
     * @return True if the user is playing the user's playlist, false otherwise.
     */
    public boolean isPlayingUserPlaylist(final String owner) {
        if (player.getSource() == null || !online) {
            return false;
        }
        if (player.getSource().getType() == Utils.PlayerSourceType.PLAYLIST
                && player.getSource().getAudioCollection().getOwner().equals(owner)) {
            return true;
        }
        return false;
    }

    /**
     * Removes the user from the system and updates related data structures.
     */
    public void removeUser() {
        Admin.getInstance().getUsers().remove(this);
        for (Playlist playlist : followedPlaylists) {
            playlist.decreaseFollowers();
        }
        for (Song song : likedSongs) {
            song.dislike();
        }

        Admin.getInstance().getNormalUsers().remove(this);
        Admin.getInstance().deleteUserPlaylists(username);
    }

    /**
     * Checks if a normal user can be deleted (meaning
     * that no other user is playing one of the playlist
     * they created).
     * @return True if the user can be deleted, false otherwise.
     */
    public boolean canBeDeleted() {
        for (User user : Admin.getInstance().getNormalUsers()) {
            if (user.isPlayingUserPlaylist(this.getUsername())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a loaded element's information into the statistics lists of
     * the user and source's owner.
     *
     * @param source the player source
     */
    public void registerListen(final PlayerSource source) {
        if (source.getType() == Utils.PlayerSourceType.LIBRARY
            || source.getType() == Utils.PlayerSourceType.PLAYLIST
            || source.getType() == Utils.PlayerSourceType.ALBUM) {
            Song song = (Song) source.getAudioFile();

            // Track song for monetization
            this.trackContent(song);

            // Add song to user statistics
            stats.addToMapping(stats.topSongs, song.getName());
            stats.addToMapping(stats.topArtists, song.getArtist());
            stats.addToMapping(stats.topAlbums, song.getAlbum());
            stats.addToMapping(stats.topGenres, song.getGenre());

            Artist artist = Admin.getInstance().getArtist(song.getArtist());
            if (artist != null) {
                // Add song to artist statistics
                artist.stats.addToMapping(artist.stats.topSongs, song.getName());
                artist.stats.addToMapping(artist.stats.topAlbums, song.getAlbum());
                artist.stats.addToMapping(artist.stats.listenersList, username);
                artist.stats.topFans.add(username);
            }
            return;
        }

        Episode episode = (Episode) source.getAudioFile();
        // Add episode to user statistics
        stats.addToMapping(stats.topEpisodes, episode.getName());
        Host host = Admin.getInstance().getHost(source.getAudioCollection().getOwner());
        if (host != null) {
            // Add episode to host statistics
            host.stats.addToMapping(host.stats.topEpisodes, episode.getName());
            host.stats.addToMapping(host.stats.listenersList, username);
        }
    }

    /**
     * Gets normal user statistics.
     * @return statistics
     */
    @Override
    public ObjectNode getUserStats() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("topArtists", objectMapper.valueToTree(stats.getTopArtists()));
        objectNode.put("topGenres", objectMapper.valueToTree(stats.getTopGenres()));
        objectNode.put("topSongs", objectMapper.valueToTree(stats.getTopSongs()));
        objectNode.put("topAlbums", objectMapper.valueToTree(stats.getTopAlbums()));
        objectNode.put("topEpisodes", objectMapper.valueToTree(stats.getTopEpisodes()));

        return objectNode;
    }

    /**
     * Register revenue for the artists whose songs
     * have been played by the user.
     */
    public void payArtists() {
        HashMap<Song, Integer> content;
        Double monetizationFactor;
        if (!premium) {
            content = new HashMap<>(interAdsContent);
            monetizationFactor = player.getAdPrice().doubleValue();
        } else {
            content = new HashMap<>(premiumContent);
            monetizationFactor = INITIAL_PREMIUM_CREDIT;
        }
        // Calculate the number of songs the user has played.
        Integer totalListens = content.values().stream().mapToInt(Integer::intValue).sum();
        if (totalListens == 0) {
            return;
        }
        for (Artist artist : Admin.getInstance().getArtists()) {
            // Calculate the number of the artist's songs in the list
            Integer artistListens = artist.countOwnSongsOccurrence(content,
                    monetizationFactor / totalListens);
            if (artistListens != 0) {
                // Increase the profit made by the artist
                artist.songProfit(monetizationFactor / totalListens * artistListens);
            }
        }
    }

    /**
     * Add song to the corresponding
     * monetization list based on user account type.
     *
     * @param song the song
     */
    public void trackContent(final Song song) {
        if (!premium) {
            updateMonetizationList(song, interAdsContent);
        } else {
            updateMonetizationList(song, premiumContent);
        }
    }

    /**
     * Add a song to the monetization list or increase its
     * number of listens if it already exists.
     *
     * @param song the song
     * @param contentList the monetization list of songs
     *                    and their number of listens
     */
    private void updateMonetizationList(final Song song,
                                        final HashMap<Song, Integer> contentList) {
        for (Map.Entry<Song, Integer> entry : contentList.entrySet()) {
            if (entry.getKey().equals(song) || entry.getKey().getName().equals(song.getName())) {
                contentList.put(entry.getKey(), entry.getValue() + 1);
                return;
            }
        }
        contentList.put(song, 1);
    }


    /**
     * Create a notification for new content and add it
     * to the notifications list.
     *
     * @param contentType type of new content
     * @param subject content creator
     */
    @Override
    public void update(final String contentType, final Subject subject) {
        Notification notification = new Notification(contentType,
                ((ContentCreator) subject).getUsername());
        notifications.add(notification);
    }

    /**
     * Creates a song recommendation from the songs
     * listened to by the fans of the artist whose song is
     * in the user's player.
     *
     * @return true if the operation was successful, false otherwise
     */
    public boolean addRandomSongToRecs() {
        Song song;
        try {
            song = (Song) player.getCurrentAudioFile();
        } catch (Exception e) {
            return false;
        }
        if (!enoughTimePassed()) {
            return false;
        }
        int seed = player.getCurrentAudioFile().getDuration()
                - player.getSource().getDuration();
        homePage.getSongRecommandations().add(
                Admin.getInstance().getRandomSongByGenre(seed, song.getGenre()).getName());
        lastRecommendation.setType("song");
        lastRecommendation.setSong(Admin.getInstance().getRandomSongByGenre(seed, song.getGenre()));
        return true;
    }

    /**
     * Checks if more than 30 seconds have passed since
     * the beginning of the audio file currently in the player.
     *
     * @return true if more than 30 seconds have passed,
     *         false otherwise
     */
    public boolean enoughTimePassed() {
        return player.getCurrentAudioFile() != null
                && player.getCurrentAudioFile().getDuration()
                        - player.getSource().getDuration() >= TIME_LIMIT;
    }

    /**
     * Creates a random playlist from the songs
     * listened to by the user based on his top 3 genres.
     *
     * @param timestamp the timestamp of the playlist creation
     * @return true if the operation was successful, false otherwise
     */
    public boolean addRandomPlaylistToRecs(final int timestamp) {
        List<String> topGenres = getTop3PreferredGenres();
        if (topGenres.isEmpty()) {
            return false;
        }
        ArrayList<Song> playlistSongs = new ArrayList<>();
        int size = Admin.getInstance().getSongsFromGenre(topGenres.get(0)).size();

        // Add songs from first genre
        playlistSongs.addAll(Admin.getInstance().getSongsFromGenre(topGenres.get(0))
                .subList(0, min(FIRST_GENRE_LIMIT - 1, size - 1)));

        if (topGenres.size() > 1) {
            size = Admin.getInstance().getSongsFromGenre(topGenres.get(1)).size();
            // Add songs from second genre
            playlistSongs.addAll(Admin.getInstance().getSongsFromGenre(topGenres.get(1))
                    .subList(0, min(SECOND_GENRE_LIMIT - 1, size - 1)));

            if (topGenres.size() > 2) {
                size = Admin.getInstance().getSongsFromGenre(topGenres.get(2)).size();
                // Add songs from third genre
                playlistSongs.addAll(Admin.getInstance().getSongsFromGenre(topGenres.get(2))
                        .subList(0, min(THIRD_GENRE_LIMIT - 1, size - 1)));
            }
        }

        Playlist newPlaylist = new Playlist(username + "'s recommendations", username, timestamp);
        playlists.add(newPlaylist);
        homePage.getPlaylistRecommandations().add(newPlaylist.getName());
        lastRecommendation.setType("playlist");
        return true;
    }

    /**
     * Creates a Fan's Recommendation playlist from the songs
     * listened to by the fans of the artist whose song is
     * in the user's player.
     *
     * @return true if the operation was successful, false otherwise
     */
    public boolean addFansPlaylistToRecs() {
        Artist artist = Admin.getInstance()
                .getArtist(((Song) player.getCurrentAudioFile()).getArtist());
        if (artist == null) {
            return false;
        }
        ArrayList<String> fans = new ArrayList<>(artist.stats.topFans);
        if (fans.size() > LIMIT) {
            fans.removeIf(fan -> fans.indexOf(fan) >= LIMIT);
        }
        ArrayList<Song> songs = new ArrayList<>();
        for (String fan : fans) {
            List<Song> fanSongs = new ArrayList<>(Admin.getInstance()
                    .getNormalUser(fan).getLikedSongs()
                    .stream()
                    .sorted(Comparator.comparingInt(Song::getLikes).reversed())
                    .toList());
            if (fanSongs.isEmpty()) {
                continue;
            }
            songs.addAll(fanSongs.subList(0, min(LIMIT - 1, fanSongs.size() - 1)));
        }
        if (songs.isEmpty()) {
            return false;
        }
        Playlist playlist = new Playlist(artist.getUsername()
                + " Fan Club recommendations", username);
        songs.forEach(playlist::addSong);
        playlists.add(playlist);
        homePage.getPlaylistRecommandations().add(playlist.getName());
        lastRecommendation.setPlaylist(playlist);
        lastRecommendation.setType("playlist");
        return true;
    }

    /**
     * Determines the 3 most listened-to genres of the user.
     * @return the 3 top genres of the usr
     */
    public List<String> getTop3PreferredGenres() {
        HashMap<String, Integer> preferredGenres = new HashMap<>();
        likedSongs.forEach(song -> {
            if (preferredGenres.containsKey(song.getGenre())) {
                preferredGenres.put(song.getGenre(), preferredGenres.get(song.getGenre()) + 1);
            } else {
                preferredGenres.put(song.getGenre(), 1);
            }
        });
        playlists.forEach(playlist -> {
            playlist.getSongs().forEach(song -> {
                if (preferredGenres.containsKey(song.getGenre())) {
                    preferredGenres.put(song.getGenre(), preferredGenres.get(song.getGenre()) + 1);
                } else {
                    preferredGenres.put(song.getGenre(), 1);
                }
            });
        });
        followedPlaylists.forEach(playlist -> {
            playlist.getSongs().forEach(song -> {
                if (preferredGenres.containsKey(song.getGenre())) {
                    preferredGenres.put(song.getGenre(), preferredGenres.get(song.getGenre()) + 1);
                } else {
                    preferredGenres.put(song.getGenre(), 1);
                }
            });
        });
        List<String> list = preferredGenres
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .toList();
        return list.subList(0, min(NUMBER_OF_GENRES, list.size()));
    }

    /**
     * Gets the page of the artist that owns the song
     * the user is listening to.
     *
     * @return the artist's page
     */
    public ArtistPage getCurrentArtistPage() {
        if (player.getSource() == null) {
            return null;
        }
        Song currentSong = (Song) player.getSource().getAudioFile();
        return Admin.getInstance().getArtist(currentSong.getArtist()).getPage();
    }

    /**
     * Gets the page of the host that owns the podcast
     * the user is listening to.
     *
     * @return the host's page
     */
    public HostPage getCurrentHostPage() {
        if (player.getSource() == null) {
            return null;
        }
        Podcast currentPodcast = (Podcast) player.getSource().getAudioCollection();
        return Admin.getInstance().getHost(currentPodcast.getOwner()).getPage();
    }
}
