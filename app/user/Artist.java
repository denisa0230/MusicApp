package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Files.Song;
import app.pages.ArtistPage;
import app.searchBar.Searchable;
import app.user.artistFeatures.ArtistAccount;
import app.user.artistFeatures.Event;
import app.user.artistFeatures.Merchandise;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

import static app.utils.Utils.HUNDRED;

/**
 * This class represents an artist user in the application.
 * It extends the {@code ContentCreator} class and implements the {@code Searchable} interface.
 */
@Getter
@Setter
public class Artist extends ContentCreator implements Searchable {
    private ArrayList<Album> albums = new ArrayList<>();
    private ArrayList<Merchandise> merchList = new ArrayList<>();
    private ArrayList<Event> events = new ArrayList<>();
    private ArtistPage page;
    private ArtistAccount account;

    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
        page = new ArtistPage(this);
        account = new ArtistAccount();
        type = "artist";
    }

    /**
     * Returns artist username (used for the Searchable interface).
     * @return username
     */
    @Override
    public String getItemName() {
        return username;
    }

    /**
     * Gets the names of all merchandise associated with the artist.
     * @return a list of merchandise names
     */
    public List<String> getMerchNames() {
        return merchList.stream().map(Merchandise::getName).collect(Collectors.toList());
    }

    /**
     * Gets the names of all albums associated with the artist.
     * @return a list of album names
     */
    public List<String> getAlbumNames() {
        return albums.stream().map(Album::getName).collect(Collectors.toList());
    }

    /**
     * Gets the names of all events associated with the artist.
     * @return a list of event names
     */
    public List<String> getEventNames() {
        return events.stream().map(Event::getName).collect(Collectors.toList());
    }

    /**
     * Gets a list of all the songs in an artist's albums.
     * @return the list of songs
     */
    public List<Song> getSongs() {
        List<Song> songList = new ArrayList<>();
        for (Album album : albums) {
            songList.addAll(album.getSongs());
        }
        return songList;
    }

    /**
     * Gets an event.
     * @param name the name of an event
     * @return the event with that name
     */
    public Event getEvent(final String name) {
        for (Event event : events) {
            if (event.getName().equals(name)) {
                return event;
            }
        }
        return null;
    }

    /**
     * Calculates the number of likes received by an artist's songs.
     * @return number of likes
     */
    public int getLikes() {
        int likes = 0;
        for (Song song : getSongs()) {
            likes += song.getLikes();
        }
        return likes;
    }

    /**
     * Removes the artist from the application, including albums, songs, and user lists.
     */
    @Override
    public void removeUser() {
        // Remove all albums
        Admin.getInstance().getAlbums().removeAll(albums);

        // Remove all songs from library
        getSongs().forEach(Admin.getInstance()::removeSongFromLibrary);

        // Remove artist from the lists of artists/users
        Admin.getInstance().getArtists().remove(this);
        Admin.getInstance().getUsers().remove(this);
    }


    /**
     * Checks if the artist can be deleted.
     * @return true if the artist can be deleted, false otherwise
     */
    @Override
    public boolean canBeDeleted() {
        for (User user : Admin.getInstance().getNormalUsers()) {
            if (user.isPlayingArtist(username)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets artist statistics.
     * @return statistics
     */
    @Override
    public ObjectNode getUserStats() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("topAlbums", objectMapper.valueToTree(stats.getTopAlbums()));
        objectNode.put("topSongs", objectMapper.valueToTree(stats.getTopSongs()));
        objectNode.put("topFans", objectMapper.valueToTree(stats.getTopFans()));
        objectNode.put("listeners", stats.getListeners());

        return objectNode;
    }

    /**
     * Determines the song that gathered the most profit
     * for the artist.
     * @return the name of the most profitable song
     */
    public String findMostProfitableSong() {
        if (getSongs().isEmpty() || account.getSongRevenue() == 0.0) {
            return "N/A";
        }
        HashMap<String, Double> songRev = new HashMap<>();
        for (Song song : getSongs()) {
            if (songRev.containsKey(song.getName())) {
                songRev.put(song.getName(), songRev.get(song.getName()) + song.getRevenue());
            } else {
                songRev.put(song.getName(), song.getRevenue());
            }
        }
        return songRev.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder())
                    .thenComparing(Map.Entry.comparingByKey()))
                .toList().get(0).getKey();
    }

    /**
     * Formats the song revenue to a Double
     * value with two decimals.
     * @return song revenue sum with two decimals
     */
    public Double roundUpSongRevenue() {
        return Math.round(account.getSongRevenue() * HUNDRED) / HUNDRED;
    }

    /**
     * Receive money from merch acquisition.
     * @param name name of the bought merch
     */
    public void soldMerch(final String name) {
        for (Merchandise merchandise : merchList) {
            if (merchandise.getName().equals(name)) {
                account.increaseMerchRevenue(merchandise.getPrice());
            }
        }
    }

    /**
     * Receive money from the play of a song.
     * @param money revenue received
     */
    public void songProfit(final Double money) {
        account.increaseSongRevenue(money);
    }


    /**
     * Count the occurrence of the artist's songs in
     * a list of songs played by a normal user and register
     * their revenue.
     *
     * @param songList list of songs listened to by a user
     * @param commonFactor the monetization factor
     *                     particular to the user account
     * @return the number of listens the artist's songs have received
     */
    public Integer countOwnSongsOccurrence(final HashMap<Song, Integer> songList,
                                           final Double commonFactor) {
        Integer listens = 0;
        for (Map.Entry<Song, Integer> entry : songList.entrySet()) {
            if (entry.getKey().getArtist().equals(username)) {
                // the song belongs to the artist
                listens += entry.getValue();
                // increase song revenue
                Admin.getInstance().getSong(entry.getKey().getName())
                        .increaseRevenue(commonFactor * entry.getValue());
            }
        }
        return listens;
    }

    /**
     * Prepares artist account data for printing.
     * @return artist account information
     */
    public ArtistAccount getAccount() {
        account.setSongRevenue(roundUpSongRevenue());
        account.setMostProfitableSong(findMostProfitableSong());
        return account;
    }
}
