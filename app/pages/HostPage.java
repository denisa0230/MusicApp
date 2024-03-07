package app.pages;

import app.audio.Collections.Podcast;
import app.user.Host;
import app.user.hostFeatures.Announcement;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * This page class extends the {@code Page} class and
 * contains specific details about a host,
 * such as their podcasts and announcements.
 */
public class HostPage extends Page {

    /**
     * The host associated with the page.
     */
    @Setter
    @Getter
    private Host host;

    /**
     * The list of podcasts associated with the host.
     */
    private List<Podcast> podcasts;

    /**
     * The list of announcements associated with the host.
     */
    private List<Announcement> announcements;

    public HostPage(final Host host) {
        this.host = host;
        setType("host");
    }

    /**
     * Generates a formatted representation of the host page.
     * @return a formatted string representing the host page
     */
    @Override
    public String pageFormat() {
        podcasts = host.getPodcasts();
        announcements = host.getAnnouncements();
        return "Podcasts:\n\t" + podcasts.toString()
                + "\n\nAnnouncements:\n\t" + announcements.toString();
    }
}
