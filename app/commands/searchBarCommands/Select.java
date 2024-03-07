package app.commands.searchBarCommands;

import app.Admin;
import app.commands.Command;
import app.searchBar.Searchable;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@link Command} class and is used to execute the selection
 * of an item from search results for a given user.
 */
public final class Select extends Command {

    /**
     * The user for whom the item is selected (receiver of the command).
     */
    private final User user;

    /**
     * The number representing the position of the item in the search results.
     */
    private final int itemNumber;

    public Select(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
        itemNumber = commandInput.getItemNumber();
    }

    /**
     * Executes the Select command and sets the output with the generated message.
     */
    public void execute() {
        String message = select();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Selects an item from the search results based on the specified item number.
     * @return A message indicating the result of the item selection.
     */
    public String select() {
        // Check if the user is online
        if (!user.isOnline()) {
            return user.getUsername() + " is offline.";
        }

        // Check if a search has been conducted
        if (!user.isLastSearched()) {
            return "Please conduct a search before making a selection.";
        }

        // Reset the last searched flag
        user.setLastSearched(false);

        // Select the item from the search results
        Searchable selected = user.getSearchBar().select(itemNumber);

        // Check if the selected item is null
        if (selected == null) {
            return "The selected ID is too high.";
        }

        // Set the current page based on the selected item type
        String suffix = "";
        if (user.getSearchBar().getLastSearchType().equals("artist")) {
            user.setCurrentPage(((Artist) selected).getPage());
            suffix = "'s page";
        }
        if (user.getSearchBar().getLastSearchType().equals("host")) {
            user.setCurrentPage(((Host) selected).getPage());
            suffix = "'s page";
        }

        return "Successfully selected %s.".formatted(selected.getItemName() + suffix);
    }
}
