/**
 * The package containing classes related to the application.
 */
package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import app.user.UserAbstract;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * The {@code Admin} class represents the administrator of the application.
 * It manages users, artists, hosts, songs, podcasts, and albums, and provides
 * various functionalities related to these entities.
 */
public final class Admin {
    /**
     * The list of all users in the application.
     */
    @Getter
    private List<UserAbstract> users = new ArrayList<>();

    /**
     * The list of normal users in the application.
     */
    @Setter
    @Getter
    private List<User> normalUsers = new ArrayList<>();

    /**
     * The list of all artists in the application.
     */
    @Getter
    private List<Artist> artists = new ArrayList<>();

    /**
     * The list of all hosts in the application.
     */
    @Getter
    private List<Host> hosts = new ArrayList<>();

    /**
     * The list of all songs in the application.
     */
    @Getter
    private List<Song> songs = new ArrayList<>();

    /**
     * The list of all podcasts in the application.
     */
    @Getter
    private List<Podcast> podcasts = new ArrayList<>();

    /**
     * The list of all albums in the application.
     */
    @Getter
    private List<Album> albums = new ArrayList<>();

    /**
     * The timestamp used for simulating time in the application.
     */
    private int timestamp = 0;
    private static Admin instance = null;

    private Admin() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static Admin getInstance() {
        if (instance == null) {
            instance = new Admin();
        }
        return instance;
    }

    /**
     * Reset instance.
     */
    public static void resetInstance() {
        instance = null;
    }

    /**
     * Sets users based on the provided user input list.
     *
     * @param userInputList the user input list
     */
    public void setUsers(final List<UserInput> userInputList) {
        normalUsers = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            normalUsers.add(new User(userInput.getUsername(),
                    userInput.getAge(), userInput.getCity()));
        }
        users.addAll(normalUsers);
    }

    /**
     * Sets songs based on the provided song input list.
     *
     * @param songInputList the song input list
     */
    public void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));

        }
    }

    /**
     * Sets podcasts based on the provided podcast input list.
     *
     * @param podcastInputList the podcast input list
     */
    public void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                        episodeInput.getDuration(),
                        episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Returns a list of song names in the application.
     *
     * @return the list of song names
     */
    public List<String> getSongNames() {
        return songs.stream().map(Song::getName).collect(Collectors.toList());
    }

    /**
     * Returns a list of all playlists in the application.
     *
     * @return the list of playlists
     */
    public List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : normalUsers) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets the user with the specified username.
     * @param username the username
     * @return the user with the specified username, or null if not found
     */
    public UserAbstract getUser(final String username) {
        for (UserAbstract user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Gets the artist with the specified username.
     * @param username the username
     * @return the artist with the specified username, or null if not found
     */
    public Artist getArtist(final String username) {
        for (Artist artist : artists) {
            if (artist.getUsername().equals(username)) {
                return artist;
            }
        }
        return null;
    }

    /**
     * Gets the host with the specified username.
     *
     * @param username the username
     * @return the host with the specified username, or null if not found
     */
    public Host getHost(final String username) {
        for (Host host : hosts) {
            if (host.getUsername().equals(username)) {
                return host;
            }
        }
        return null;
    }

    /**
     * Gets the normal user with the specified username.
     *
     * @param username the username
     * @return the normal user with the specified username, or null if not found
     */
    public User getNormalUser(final String username) {
        for (User user : normalUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Checks if a user with the specified username exists.
     *
     * @param username the username
     * @return true if the user exists, false otherwise
     */
    public boolean userExists(final String username) {
        return getUser(username) != null;
    }

    /**
     * Updates the timestamp and simulates time for online users.
     *
     * @param newTimestamp the new timestamp
     */
    public void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : normalUsers) {
            if (user.isOnline()) {
                user.simulateTime(elapsed);
            }
        }
    }

    /**
     * Resets all data in the application.
     */
    public void reset() {
        users = new ArrayList<>();
        hosts = new ArrayList<>();
        artists = new ArrayList<>();
        normalUsers = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        albums = new ArrayList<>();
        timestamp = 0;
    }

    /**
     * Checks if any user is currently playing the specified album.
     *
     * @param name the name of the album
     * @return true if the album is being played, false otherwise
     */
    public boolean albumIsPlayed(final String name) {
        for (User user : normalUsers) {
            if (user.isPlayingAlbum(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if any user is currently playing the specified podcast.
     *
     * @param name the name of the podcast
     * @return true if the podcast is being played, false otherwise
     */
    public boolean podcastIsPlayed(final String name) {
        for (User user : normalUsers) {
            if (user.isPlayingPodcast(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the specified song from the application.
     *
     * @param song the song to be removed
     */
    public void removeSongFromLibrary(final Song song) {
        songs.remove(song);
        for (User user : normalUsers) {
            user.getLikedSongs().removeIf(likedSong ->
                    likedSong.getName().equals(song.getName()));
        }
    }

    /**
     * Deletes playlists owned by the specified user.
     *
     * @param username the username of the user
     */
    public void deleteUserPlaylists(final String username) {
        getPlaylists().removeIf(playlist -> playlist.getOwner().equals(username));
        for (User user : normalUsers) {
            user.getFollowedPlaylists().removeIf(playlist -> playlist.getOwner().equals(username));
        }
    }

    /**
     *
     */
    public ArrayList<Artist> rankArtists() {
        ArrayList<Artist> rankedArtists = new ArrayList<>();
        artists.forEach(artist -> {
            if (artist.getAccount().getMerchRevenue() != 0.0 || !artist.getStats().noStats()) {
                rankedArtists.add(artist);
            }
        });
        rankedArtists.sort((o1, o2) -> {
            Double profit1 = o1.getAccount().getMerchRevenue() + o1.getAccount().getSongRevenue();
            Double profit2 = o2.getAccount().getMerchRevenue() + o2.getAccount().getSongRevenue();
            if (profit1.compareTo(profit2) != 0) {
                return (int) (profit2 - profit1);
            }
            return o1.getUsername().compareTo(o2.getUsername());
        });
        int idx = 1;
        for (Artist artist : rankedArtists) {
            artist.getAccount().setRanking(idx);
            idx++;
        }
        return rankedArtists;
    }

    /**
     * Find first song with given name.
     * @param name the given name
     * @return the first song with that name
     */
    public Song getSong(final String name) {
        return songs.stream()
                .filter(song -> song.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the Ad file from the library.
     * @return the ad file
     */
    public Song getAd() {
        return getSong("Ad Break");
    }

    /**
     * Gets a random song from the library that
     * fits in a certain genre.
     *
     * @param seed the seed
     * @param genre the genre
     * @return a random song from that genre
     */
    public Song getRandomSongByGenre(final int seed, final String genre) {
        Random rand = new Random(seed);
        int songsInGenre = songs.stream()
                .filter(s -> s.getGenre().equals(genre))
                .toList()
                .size();
        int position = rand.nextInt(songsInGenre);
        return songs.stream()
                .filter(s -> s.getGenre().equals(genre))
                .toList().get(position);
    }

    /**
     * Gets all music genres.
     *
     * @return list of genres
     */
    public List<String> getGenres() {
        return songs.stream()
                .map(Song::getGenre)
                .distinct()
                .toList();
    }

    /**
     * Gets all songs from a certain genre.
     *
     * @param genre the genre
     * @return a list of songs from that genre
     */
    public List<Song> getSongsFromGenre(final String genre) {
        return songs.stream()
                .filter(song -> song.getGenre().equals(genre))
                .toList();
    }

    /**
     * Ends program and handles monetization.
     *
     * @return the output of the command
     */
    public ObjectNode endProgram() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "endProgram");

        // collect money from users for playing artists' songs
        normalUsers.forEach(User::payArtists);

        ObjectNode artistProfit = objectMapper.createObjectNode();
        for (Artist artist : rankArtists()) {
            artistProfit.put(artist.getUsername(), objectMapper.valueToTree(artist.getAccount()));
        }
        objectNode.put("result", artistProfit);

        return objectNode;
    }
}
