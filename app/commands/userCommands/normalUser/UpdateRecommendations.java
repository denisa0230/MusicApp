package app.commands.userCommands.normalUser;

import app.Admin;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

public class UpdateRecommendations extends Command {
    private final String username;
    private final String recommendationType;
    public UpdateRecommendations(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        username = commandInput.getUsername();
        recommendationType = commandInput.getRecommendationType();
    }

    /**
     * Executes the BuyMerch command and sets the output with the generated result.
     */
    public void execute() {
        String message = updateRecommendations();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Implementation of the UpdateRecommendations command.
     * @return message
     */
    public String updateRecommendations() {
        if (Admin.getInstance().getUser(username) == null) {
            return username + " doesn't exist.";
        }
        User user = Admin.getInstance().getNormalUser(username);
        if (user == null) {
            return username + " is not a normal user.";
        }

        switch (recommendationType) {
            case "random_song":
                if (!user.addRandomSongToRecs()) {
                    return "No new recommendations were found";
                }
                break;
            case "random_playlist":
                if (!user.addRandomPlaylistToRecs(commandInput.getTimestamp())) {
                    return "No new recommendations were found";
                }
                break;
            case "fans_playlist":
                if (!user.addFansPlaylistToRecs()) {
                    return "No new recommendations were found";
                }
                break;
            default:
                return "";
        }
        return "The recommendations for user " + username + " have been updated successfully.";
    }
}
