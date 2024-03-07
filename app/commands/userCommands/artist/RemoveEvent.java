package app.commands.userCommands.artist;

import app.Admin;
import app.commands.Command;
import app.user.Artist;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@code Command} class and represents
 * a command for removing an artist's event.
 */
public class RemoveEvent extends Command {
    private String username;
    private String eventName;

    public RemoveEvent(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        username = commandInput.getUsername();
        eventName = commandInput.getName();
    }

    /**
     * Executes the command to remove an event and generates the output message.
     */
    @Override
    public void execute() {
        String message = removeEvent();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Removes a specific event from the artist's list of events.
     * @return A message indicating the success or failure of the event removal.
     */
    public String removeEvent() {
        if (!Admin.getInstance().userExists(username)) {
            return "The username " + username + " doesn't exist.";
        }
        if (Admin.getInstance().getArtist(username) == null) {
            return username + " is not an artist.";
        }
        Artist artist = Admin.getInstance().getArtist(username);
        if (!artist.getEventNames().contains(eventName)) {
            return artist.getUsername() + " doesn't have an event with the given name.";
        }
        artist.getEvents().remove(artist.getEvent(eventName));
        return artist.getUsername() + " deleted the event successfully.";
    }
}
