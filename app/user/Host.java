package app.user;

import app.Admin;
import app.audio.Collections.Podcast;
import app.pages.HostPage;
import app.searchBar.Searchable;
import app.user.hostFeatures.Announcement;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a host in the application.
 */
@Getter
public class Host extends ContentCreator implements Searchable {
    @Setter
    private HostPage page;
    private List<Announcement> announcements = new ArrayList<>();
    private ArrayList<Podcast> podcasts = new ArrayList<>();

    /**
     * Constructs a {@code Host} object with the given username, age, and city.
     *
     * @param username the username of the host
     * @param age      the age of the host
     * @param city     the city where the host resides
     */
    public Host(final String username, final int age, final String city) {
        super(username, age, city);
        page = new HostPage(this);
        type = "host";
    }

    /**
     * Removes the host from the application, including podcasts associated with the host.
     */
    @Override
    public void removeUser() {
        // Remove all the host's podcasts
        Admin.getInstance().getPodcasts().removeAll(this.getPodcasts());

        // Remove the host from the lists of hosts/users
        Admin.getInstance().getHosts().remove(this);
        Admin.getInstance().getUsers().remove(this);
    }

    /**
     * Checks if the host can be deleted, ensuring that the
     * host is not currently being used in the application.
     *
     * @return true if the host can be deleted, false otherwise
     */
    @Override
    public boolean canBeDeleted() {
        for (User user : Admin.getInstance().getNormalUsers()) {
            if (user.isPlayingHost(username)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Returns host username (used for the Searchable interface).
     * @return username
     */
    @Override
    public String getItemName() {
        return username;
    }

    /**
     * Gets host statistics.
     * @return statistics
     */
    @Override
    public ObjectNode getUserStats() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("topEpisodes", objectMapper.valueToTree(stats.getTopEpisodes()));
        objectNode.put("listeners", stats.getListeners());
        return objectNode;
    }

}
