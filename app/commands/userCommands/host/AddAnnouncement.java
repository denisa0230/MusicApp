package app.commands.userCommands.host;


import app.Admin;
import app.commands.Command;
import app.user.hostFeatures.Announcement;
import app.user.Host;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class extends the {@code Command} class and
 * adds announcements for a host.
 */
public class AddAnnouncement extends Command {
    /**
     * The host associated with the command.
     */
    private Host host;

    public AddAnnouncement(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
    }


    /**
     * Executes the AddAnnouncement command and sets the output.
     */
    @Override
    public void execute() {
        String message = addAnnouncement(commandInput.getUsername());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Adds a new announcement for the specified host.
     * @param username the username of the host
     * @return a message for the success or failure of the operation
     */
    public String addAnnouncement(final String username) {
        if (!Admin.getInstance().userExists(username)) {
            return "The username " + username + " doesn't exist.";
        }
        if (Admin.getInstance().getHost(username) == null) {
            return username + " is not a host.";
        }
        host = Admin.getInstance().getHost(username);
        List<String> announcements = host.getAnnouncements().stream().
                map(Announcement::getName).collect(Collectors.toList());
        if (announcements.contains(commandInput.getName())) {
            return username + " has already added an announcement with this name.";
        }
        host.getAnnouncements().add(new Announcement(commandInput.getName(),
                commandInput.getDescription()));
        // notify subscribers
        host.notifySubscribers("New Announcement");
        return username + " has successfully added new announcement.";
    }
}
