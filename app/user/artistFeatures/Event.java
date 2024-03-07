package app.user.artistFeatures;

import lombok.Getter;
import lombok.Setter;

/**
 * This class represents an event associated with an artist in the application.
 */
public class Event {

    /**
     * The name of the event.
     */
    @Setter
    @Getter
    private String name;

    /**
     * The description of the event.
     */
    @Setter
    @Getter
    private String description;

    /**
     * The date of the event.
     */
    @Setter
    @Getter
    private String date;

    public Event(final String name, final String description, final String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    /**
     * Returns a string representation of the event.
     * @return a string containing the name, date, and description of the event
     */
    @Override
    public String toString() {
        return name + " - " + date + ":\n\t" + description;
    }
}
