package app.commands.statistics;

import app.Admin;
import app.audio.Files.Song;
import app.commands.Command;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@link Command} class and is used to execute the retrieval
 * of the user's preferred genre based on liked songs and provide the result in the output.
 */
public final class GetPreferredGenre extends Command {

    /**
     * The user for whom the preferred genre is retrieved (receiver of the command).
     */
    private User user;

    public GetPreferredGenre(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        user = Admin.getInstance().getNormalUser(commandInput.getUsername());
    }

    /**
     * Executes the GetPreferredGenre command and sets the output with the generated result.
     */
    public void execute() {
        String preferredGenre = getPreferredGenre();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        this.setOutput(objectNode);
    }

    /**
     * Retrieves the preferred genre of the user based on liked songs.
     * @return A message indicating the user's preferred genre.
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        // Count liked songs in each genre
        for (Song song : user.getLikedSongs()) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        // Determine the preferred genre
        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }
}
