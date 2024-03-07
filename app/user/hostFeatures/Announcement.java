package app.user.hostFeatures;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents an announcement made by a host.
 */
public class Announcement {

    /**
     * The name of the announcement.
     */
    @Setter
    @Getter
    private String name;

    /**
     * The description of the announcement.
     */
    @Setter
    @Getter
    private String description;

    public Announcement(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        return name + ":\n\t" + description + "\n";
    }
}
