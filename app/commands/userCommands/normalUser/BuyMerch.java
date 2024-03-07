package app.commands.userCommands.normalUser;

import app.Admin;
import app.commands.Command;
import app.pages.ArtistPage;
import app.user.Artist;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command simulates the process of a user
 * buying merch from an artist's page.
 */
public class BuyMerch extends Command {
    private final String name;
    private final String username;
    public BuyMerch(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        name = commandInput.getName();
        username = commandInput.getUsername();
    }

    /**
     * Executes the BuyMerch command and sets the output with the generated result.
     */
    public void execute() {
        String message = buyMerch();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Buys merch from artist's page.
     * @return message
     */
    public String buyMerch() {
        User user = Admin.getInstance().getNormalUser(username);
        if (user == null) {
            return "The username " + username + " doesn't exist.";
        }
        if (!user.getCurrentPage().getType().equals("artist")) {
            return "Cannot buy merch from this page.";
        }
        Artist artist = ((ArtistPage) user.getCurrentPage()).getArtist();
        if (!artist.getMerchNames().contains(name)) {
            return "The merch " + name + " doesn't exist.";
        }
        artist.soldMerch(name);
        user.getMerch().add(name);
        return username + " has added new merch successfully.";
    }
}
