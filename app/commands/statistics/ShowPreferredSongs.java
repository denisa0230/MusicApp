package app.commands.statistics;

import app.Admin;
import app.audio.Files.AudioFile;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;

/**
 * This command extends the {@link Command} class and is used to execute the retrieval
 * of preferred songs and provide the result in the output.
 */
public class ShowPreferredSongs extends Command {

    /**
     * The user for whom the preferred songs are retrieved (receiver of the command).
     */
    private User user;

    public ShowPreferredSongs(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
    }

    /**
     * Executes the ShowPreferredSongs command and sets the output with the generated result.
     */
    public void execute() {
        ArrayList<String> songs = showPreferredSongs();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        this.setOutput(objectNode);
    }

    /**
     * Retrieves the list of preferred songs for the user.
     * @return A list of names of preferred songs.
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : user.getLikedSongs()) {
            results.add(audioFile.getName());
        }

        return results;
    }
}
