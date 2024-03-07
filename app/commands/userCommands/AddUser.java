package app.commands.userCommands;

import app.Admin;
import app.commands.Command;
import app.user.Artist;
import app.user.Host;
import app.user.User;
import app.user.UserAbstract;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

/**
 * This command extends the {@link Command} class and is used to add a new
 * user, artist, or host to the system.
 */
public class AddUser extends Command {
    private final String type;
    private final String username;
    private final String city;
    private final int age;

    public AddUser(final CommandInput commandInput) {
        this.setCommandInput(commandInput);
        type = commandInput.getType();
        username = commandInput.getUsername();
        city = commandInput.getCity();
        age = commandInput.getAge();
    }

    /**
     * Executes the AddUser command and sets the output with the generated result.
     */
    public void execute() {
        String message = addUser();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        this.setOutput(objectNode);
    }

    /**
     * Adds a new user, artist, or host to the system based on the provided type.
     * @return A message indicating the success or failure of the operation.
     */
    public String addUser() {
        for (UserAbstract user : Admin.getInstance().getUsers()) {
            if (user.getUsername().equals(username)) {
                return "The username " + username + " is already taken.";
            }
        }
        UserAbstract newUser;
        switch (type) {
            case "user" -> {
                newUser = new User(username, age, city);
                Admin.getInstance().getNormalUsers().add((User) newUser);
            }
            case "artist" -> {
                newUser = new Artist(username, age, city);
                Admin.getInstance().getArtists().add((Artist) newUser);
            }
            default -> {
                newUser = new Host(username, age, city);
                Admin.getInstance().getHosts().add((Host) newUser);
            }
        }
        Admin.getInstance().getUsers().add(newUser);
        return "The username " + username + " has been added successfully.";
    }
}
