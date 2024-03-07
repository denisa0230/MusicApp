package app.commands.playerCommands;

import app.Admin;
import app.audio.Collections.AudioCollection;
import app.audio.LibraryEntry;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@link Command} class and is used to
 * load a source in a user's player.
 */
public class Load extends Command {
    private final User user;
    public Load(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
    }

    /**
     * Executes a Load command and sets the output.
     */
    public void execute() {
        String message = load();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Implementation of the Load command.
     * @return message
     */
    public String load() {

        if (!user.isOnline()) {
            return user.getUsername() + " is offline.";
        }

        if (user.getSearchBar().getLastSelected() == null
                || user.getSearchBar().getLastSearchType().equals("artist")
                || user.getSearchBar().getLastSearchType().equals("host")) {
            return "Please select a source before attempting to load.";
        }

        if (!user.getSearchBar().getLastSearchType().equals("song")
                && ((AudioCollection) user.getSearchBar().
                    getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        // load cancels ads
        if (user.getPlayer().isAdIncoming()) {
            user.getPlayer().setAdIncoming(false);
            user.getPlayer().setAdPrice(0);
        }

        // set player source to last selected LibraryEntry
        user.getPlayer().setSource((LibraryEntry) user.getSearchBar().getLastSelected(),
                user.getSearchBar().getLastSearchType());

        // register listen
        user.registerListen(user.getPlayer().getSource());

        user.getSearchBar().clearSelection();
        user.getPlayer().pause();

        return "Playback loaded successfully.";
    }
}
