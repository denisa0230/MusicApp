package app.subscriptions;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;


/**
 *
 */
@Getter
@Setter
public class Notification {
    private String name;
    private String description;
    @JsonIgnore
    private String contentCreator;

    /**
     * Creates a notification object for new content added
     * by a content creator.
     * @param newContent name of the new content posted
     * @param username the username of the content creator
     */
    public Notification(final String newContent, final String username) {
        name = newContent;
        this.contentCreator = username;
        description = name + " from " + this.contentCreator + ".";
    }
}
