package app.commands.userCommands.normalUser;

import app.Admin;
import app.commands.Command;
import app.pages.ArtistPage;
import app.pages.HostPage;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@code Command} class and
 * enables the user to subscribe to a content creator.
 */
public class Subscribe extends Command {
    private final String username;
    public Subscribe(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        username = commandInput.getUsername();
    }

    /**
     * Executes the Subscribe command and sets the output with the generated result.
     */
    public void execute() {
        String message = subscribe();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Subscribe to a content creator.
     *
     * @return message
     */
    public String subscribe() {
        User user = Admin.getInstance().getNormalUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (user.getCurrentPage().getType().equals("artist")) {
            Artist artist = ((ArtistPage) user.getCurrentPage()).getArtist();
            if (artist.hasSubscriber(user)) {
                artist.removeSubscriber(user);
                return username + " unsubscribed from " + artist.getUsername() + " successfully.";
            }
            artist.addSubscriber(user);
            return username + " subscribed to " + artist.getUsername() + " successfully.";
        }
        if (user.getCurrentPage().getType().equals("host")) {
            Host host = ((HostPage) user.getCurrentPage()).getHost();
            if (host.hasSubscriber(user)) {
                host.removeSubscriber(user);
                return username + "unsubscribed from " + host.getUsername() + " successfully.";
            }
            host.addSubscriber(user);
            return username + "subscribed to " + host.getUsername() + " successfully.";
        }
        return "To subscribe you need to be on the page of an artist or host.";
    }
}
