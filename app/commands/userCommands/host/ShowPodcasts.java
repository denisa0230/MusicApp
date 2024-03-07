package app.commands.userCommands.host;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Collections.PodcastOutput;
import app.commands.Command;
import app.user.Host;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;

/**
 * The {@code ShowPodcasts} extends the {@link Command} class and
 * is used to display the podcasts associated with a host.
 */
public class ShowPodcasts extends Command {

    /**
     * The host for whom the podcasts are retrieved (receiver of the command).
     */
    private final Host host;

    public ShowPodcasts(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        host = Admin.getInstance().getHost(commandInput.getUsername());
    }

    /**
     * Executes the ShowPodcasts command and sets the output with the generated result.
     */
    public void execute() {
        ArrayList<Podcast> podcasts = host.getPodcasts();
        ArrayList<PodcastOutput> results = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            results.add(new PodcastOutput(podcast));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(results));

        this.setOutput(objectNode);
    }
}
