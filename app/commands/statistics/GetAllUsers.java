package app.commands.statistics;

import app.Admin;
import app.commands.Command;
import app.user.UserAbstract;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This command extends the {@link Command} class and is used to
 * print all the registered usernames.
 */
public class GetAllUsers extends Command {

    public GetAllUsers(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
    }

    /**
     * Executes the GetAllUsers command and sets the output with the generated results.
     */
    @Override
    public void execute() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(getUserNames()));

        this.setOutput(objectNode);
    }

    /**
     * Retrieves the list of users.
     * @return a list of usernames
     */
    public static List<String> getUserNames() {
        List<UserAbstract> allUsers = new ArrayList<>(Admin.getInstance().getNormalUsers());
        allUsers.addAll(Admin.getInstance().getArtists());
        allUsers.addAll(Admin.getInstance().getHosts());
        return allUsers.stream().map(UserAbstract::getUsername).collect(Collectors.toList());
    }
}
