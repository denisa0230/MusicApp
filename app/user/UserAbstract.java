package app.user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract class representing a generic user in the app.
 */
@Getter
@Setter
public abstract class UserAbstract {
    String username;
    int age;
    String city;
    UserStats stats;
    String type;

    /**
     * Instantiates a new User abstract.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public UserAbstract(final String username, final int age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
        this.stats = new UserStats();
    }


    /**
     * Checks if a user can be deleted.
     * @return true if the user can be Deleted, false otherwise
     */
    public abstract boolean canBeDeleted();


    /**
     * Removes a user from the system and all
     * traces of them.
     */
    public abstract void removeUser();


    /**
     * Checks if the user's username matches the given name.
     * @param name the name to match
     * @return true if the username matches, false otherwise
     */
    public boolean matchesName(final String name) {
        return name == null || this.username.toLowerCase().startsWith(name.toLowerCase());
    }


    /**
     * Gets user wrapped statistics based on user type.
     * @return statistics in Json format
     */
    public abstract ObjectNode getUserStats();

}
