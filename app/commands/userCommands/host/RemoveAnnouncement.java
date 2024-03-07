/**
 * The package containing classes related to user commands for hosts.
 */
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
 * This command extends the {@code Command} class and
 * removes an announcement for a specific host.
 */
public class RemoveAnnouncement extends Command {
    /**
     * The username associated with the command.
     */
    private String username;


    public RemoveAnnouncement(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        username = commandInput.getUsername();
    }

    /**
     * Executes the RemoveAnnouncement command and sets the output.
     */
    @Override
    public void execute() {
        String message = removeAnnouncement();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Removes an announcement for the specified host.
     * @return a message indicating the success or failure of the operation
     */
    public String removeAnnouncement() {
        if (!Admin.getInstance().userExists(username)) {
            return "The username " + username + " doesn't exist.";
        }
        if (Admin.getInstance().getHost(username) == null) {
            return username + " is not a host.";
        }
        Host host = Admin.getInstance().getHost(username);
        List<String> announcements = host.getAnnouncements().stream().
                map(Announcement::getName).collect(Collectors.toList());
        if (!announcements.contains(commandInput.getName())) {
            return username + " has no announcement with the given name.";
        }
        host.getAnnouncements().removeIf(announcement
                -> announcement.getName().equals(commandInput.getName()));
        return username + " has successfully deleted the announcement.";
    }
}
