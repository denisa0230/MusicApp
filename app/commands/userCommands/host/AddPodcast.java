package app.commands.userCommands.host;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.commands.Command;
import app.user.Host;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;

import java.util.ArrayList;
import java.util.List;

/**
 * This command extends the {@code Command} and adds a podcast for a given host.
 */
public class AddPodcast extends Command {
    private final String username;
    private final String name;
    private ArrayList<EpisodeInput> episodeInputs;
    public AddPodcast(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        username = commandInput.getUsername();
        name = commandInput.getName();
        episodeInputs = commandInput.getEpisodes();
    }

    /**
     * Execute the AddPodcast command and set the output.
     */
    @Override
    public void execute() {
        String message = addPodcast();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }


    /**
     * Adds a new podcast for the specified host.
     * @return a message indicating the success or failure of the operation
     */
    public String addPodcast() {
        if (!Admin.getInstance().userExists(username)) {
            return "The username " + username + "doesn't exist.";
        }
        if (Admin.getInstance().getHost(username) == null) {
            return username + " is not a host.";
        }
        Host host = Admin.getInstance().getHost(username);
        if (host.getPodcasts().stream().anyMatch(pod -> pod.getName().equals(name))) {
            return host.getUsername() + " has another podcast with the same name.";
        }
        // might change
        List<Episode> episodes = new ArrayList<>();
        for (EpisodeInput episodeInput : episodeInputs) {
            episodes.add(new Episode(episodeInput.getName(),
                    episodeInput.getDuration(),
                    episodeInput.getDescription()));
        }
        ArrayList<String> uniqueEps = new ArrayList<>();
        for (Episode episode : episodes) {
            if (uniqueEps.contains(episode.getName())) {
                return host.getUsername() + " has the same episode in this podcast.";
            }
            uniqueEps.add(episode.getName());
        }
        Podcast newPodcast = new Podcast(name, host.getUsername(), episodes);
        host.getPodcasts().add(newPodcast);
        Admin.getInstance().getPodcasts().add(newPodcast);

        // notify subscribers
        host.notifySubscribers("New Podcast");

        return host.getUsername() + " has added new podcast successfully.";
    }

}
