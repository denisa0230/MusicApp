package app.commands.userCommands.normalUser;

import app.Admin;
import app.commands.Command;
import app.user.User;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@code Command} class and changes
 * the current page of a user.
 */
public class ChangePage extends Command {
    private final User user;
    private final String nextPage;
    public ChangePage(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
        nextPage = commandInput.getNextPage();
    }

    /**
     * Executes the ChangePage command and sets the output to the generated results.
     */
    @Override
    public void execute() {
        String message = changePage();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        this.setOutput(objectNode);
    }

    /**
     * Changes the current page for the user based on the specified next page.
     * @return A message indicating the success or failure of the operation.
     */
    public String changePage() {
        if (!user.isOnline()) {
            return user.getUsername() + " is offline.";
        }
        // remove forward history
        user.getPageHistory().removeIf(page ->
                user.getPageHistory().indexOf(page) > user.getCurrentPageIndex());

        switch (nextPage) {
            case "Home":
                user.setCurrentPage(user.getHomePage());
                break;
            case "LikedContent":
                user.setCurrentPage(user.getLikedContentPage());
                break;
            case "Artist":
                if (user.getPlayer().getSource().getType() != Utils.PlayerSourceType.PODCAST) {
                    user.setCurrentPage(user.getCurrentArtistPage());
                }
                break;
            case "Host":
                if (user.getPlayer().getSource().getType() == Utils.PlayerSourceType.PODCAST) {
                    user.setCurrentPage(user.getCurrentHostPage());
                }
                break;
            default:
                return user.getUsername() + " is trying to access a non-existent page.";
        }

        user.getPageHistory().add(user.getCurrentPage());
        user.setCurrentPageIndex(user.getPageHistory().size() - 1);
        return user.getUsername() + " accessed " + nextPage + " successfully.";
    }
}
