package app.commands.userCommands.artist;

import app.Admin;
import app.commands.Command;
import app.user.Artist;
import app.user.artistFeatures.Merchandise;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.List;

/**
 * This command extends the {@link Command} class and is used to add new merchandise
 * to an artist's page.
 */
public class AddMerch extends Command {
    private final Merchandise merch;
    private final String username;

    public AddMerch(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        merch = new Merchandise(commandInput.getName(),
                commandInput.getDescription(), commandInput.getPrice());
        username = commandInput.getUsername();
    }

    /**
     * Executes the AddMerch command and sets the output with the generated result.
     */
    @Override
    public void execute() {
        String message = addMerch();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Adds new merchandise to the artist's merchList based on the provided input.
     * @return A message indicating the success or failure of the operation.
     */
    public String addMerch() {
        if (!Admin.getInstance().userExists(username)) {
            return "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        if (Admin.getInstance().getArtist(username) == null) {
            return commandInput.getUsername() + " is not an artist.";
        }
        if (merch.getPrice() < 0) {
            return "Price for merchandise can not be negative.";
        }
        List<String> merchNames = Admin.getInstance().getArtist(username).getMerchNames();
        if (merchNames.contains(merch.getName())) {
            return username + " has merchandise with the same name.";
        }

        Artist artist =  Admin.getInstance().getArtist(username);
        artist.getMerchList().add(merch);

        // notify subscribers
        artist.notifySubscribers("New Merchandise");

        return username + " has added new merchandise successfully.";
    }
}
