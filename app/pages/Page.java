package app.pages;

import lombok.Getter;
import lombok.Setter;

/**
 * Generic page class inherited by the ArtistPage, HostPage, HomePage
 * and LikedContent page classes.
 */
public abstract class Page {
    /**
     * Indicates the type of the page.
     * Can be home, likedContent, artist or host.
     */
    @Getter
    @Setter
    private String type;

    /**
     * @return the page output format
     */
    public String pageFormat() {
        return null;
    }
}
