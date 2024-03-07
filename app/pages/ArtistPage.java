package app.pages;

import app.audio.Collections.Album;
import app.user.Artist;
import app.user.artistFeatures.Event;
import app.user.artistFeatures.Merchandise;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

/**
 * This page class extends the {@code Page} class and provides specific
 * details about an artist, including their albums, merchandise, and events.
 */
public class ArtistPage extends Page {

    /**
     * The artist associated with the page.
     */
    @Setter
    @Getter
    private Artist artist;

    /**
     * The list of album names associated with the artist.
     */
    private List<String> albums;

    /**
     * The list of merchandise associated with the artist.
     */
    private ArrayList<Merchandise> merch;

    /**
     * The list of events associated with the artist.
     */
    private ArrayList<Event> events;

    /**
     * Constructs an {@code ArtistPage} object with the given artist.
     *
     * @param artist the artist for the page
     */
    public ArtistPage(final Artist artist) {
        this.artist = artist;
        setType("artist");
    }

    /**
     * Generates a formatted representation of the artist page.
     *
     * @return a formatted string representing the artist page
     */
    @Override
    public String pageFormat() {
        albums = artist.getAlbums().stream().map(Album::getName).collect(Collectors.toList());
        merch = artist.getMerchList();
        events = artist.getEvents();
        return "Albums:\n\t" + albums
                + "\n\nMerch:\n\t" + merch.toString()
                + "\n\nEvents:\n\t" + events.toString();
    }
}
