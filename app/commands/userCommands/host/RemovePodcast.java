package app.commands.userCommands.host;

import app.Admin;
import app.commands.Command;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import app.user.Host;

/**
 * This command extends the {@code Command} class and
 * removes a podcast for a specific host.
 */
public class RemovePodcast extends Command {
    private Host host;
    public RemovePodcast(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        host = Admin.getInstance().getHost(commandInput.getUsername());
    }

    /**
     * Executes the RemovePodcast command and sets the output.
     */
    @Override
    public void execute() {
        String message = removePodcast(commandInput.getName());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Removes a podcast from a host's list of podcasts.
     * @param name the name of the podcast
     * @return a message indicating the result of the operation
     */
    public String removePodcast(final String name) {
        if (!Admin.getInstance().userExists(commandInput.getUsername())) {
            return "The username " + commandInput.getUsername() + "doesn't exist.";
        }
        if (Admin.getInstance().getHost(commandInput.getUsername()) == null) {
            return commandInput.getUsername() + " is not a host.";
        }
        if (host.getPodcasts().stream().noneMatch(pod -> pod.getName().equals(name))) {
            return host.getUsername() + " doesn't have a podcast with the given name.";
        }
        if (Admin.getInstance().podcastIsPlayed(name)) {
            return host.getUsername() + " can't delete this podcast.";
        }
        host.getPodcasts().removeIf(podcast -> podcast.getName().equals(name));
        Admin.getInstance().getPodcasts().removeIf(podcast -> podcast.getName().equals(name));
        return host.getUsername() + " deleted the podcast successfully.";
    }
}
