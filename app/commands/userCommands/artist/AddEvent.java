package app.commands.userCommands.artist;

import app.Admin;
import app.commands.Command;
import app.user.Artist;
import app.user.artistFeatures.Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import static app.utils.Utils.*;


/**
 * This command extends the {@link Command} class and is used to add a new
 * event to an artist's page.
 */
public class AddEvent extends Command {

    public AddEvent(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
    }

    /**
     * Executes the AddEvent command and sets the output with the generated result.
     */
    @Override
    public void execute() {
        String message = addEvent(commandInput.getName(), commandInput.getDescription(),
                commandInput.getDate());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Adds a new event to the artist's schedule based on the provided input.
     *
     * @param name        The name of the new event.
     * @param description The description of the new event.
     * @param date        The date of the new event.
     * @return A message indicating the success or failure of the addition.
     */
    public String addEvent(final String name, final String description, final String date) {
        if (!Admin.getInstance().userExists(commandInput.getUsername())) {
            return "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        if (Admin.getInstance().getArtist(commandInput.getUsername()) == null) {
            return commandInput.getUsername() + " is not an artist.";
        }
        Artist artist = Admin.getInstance().getArtist(commandInput.getUsername());
        if (artist.getEventNames().contains(name)) {
            return artist.getUsername() + " has another event with the same name.";
        }
        if (!dateIsValid(date)) {
            return "Event for " + artist.getUsername() + " does not have a valid date.";
        }
        Event newEvent = new Event(name, description, date);
        artist.getEvents().add(newEvent);

        // notify subscribers
        artist.notifySubscribers("New Event");

        return artist.getUsername() + " has added new event successfully.";
    }

    /**
     * Checks if the provided date is valid.
     *
     * @param date The date to be validated.
     * @return {@code true} if the date is valid, {@code false} otherwise.
     */
    public boolean dateIsValid(final String date) {
        int day = getNumericValue(date.charAt(0)) * TEN + getNumericValue(date.charAt(1));
        int month = getNumericValue(date.charAt(3)) * TEN + getNumericValue(date.charAt(4));
        int year = Integer.parseInt(date.substring(6, TEN));
        return !((month == FEBRUARY && day > FEBRUARY_MAX_DAY)
                || day > MAX_DAY || month > MAX_MONTH || year < MIN_YEAR || year > MAX_YEAR);
    }

    /**
     * Gets the numeric value of a character.
     *
     * @param ch The character.
     * @return The numeric value.
     */
    private int getNumericValue(final char ch) {
        return Character.getNumericValue(ch);
    }
}
