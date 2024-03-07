package main;

import app.Admin;
import app.CommandRunner;
import app.commands.playerCommands.*;
import app.commands.playlistCommands.*;
import app.commands.searchBarCommands.Search;
import app.commands.searchBarCommands.Select;
import app.commands.statistics.*;
import app.commands.userCommands.*;
import app.commands.userCommands.artist.*;
import app.commands.userCommands.host.*;
import app.commands.userCommands.normalUser.*;
import checker.Checker;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.getName().startsWith("library")) {
                continue;
            }

            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }
        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        LibraryInput library = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH
                                                               + "library/library.json"),
                                                               LibraryInput.class);
        CommandInput[] commands = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH
                                                                  + filePath1),
                                                                  CommandInput[].class);
        ArrayNode outputs = objectMapper.createArrayNode();

        Admin.getInstance().setUsers(library.getUsers());
        Admin.getInstance().setSongs(library.getSongs());
        Admin.getInstance().setPodcasts(library.getPodcasts());

        CommandRunner commandRunner = new CommandRunner();

        for (CommandInput command : commands) {
            Admin.getInstance().updateTimestamp(command.getTimestamp());

            String commandName = command.getCommand();

            switch (commandName) {
                case "search" -> commandRunner.setCommand(new Search(command));
                case "select" -> commandRunner.setCommand(new Select(command));
                case "load" -> commandRunner.setCommand(new Load(command));
                case "playPause" ->  commandRunner.setCommand(new PlayPause(command));
                case "repeat" -> commandRunner.setCommand(new Repeat(command));
                case "shuffle" -> commandRunner.setCommand(new Shuffle(command));
                case "forward" -> commandRunner.setCommand(new Forward(command));
                case "backward" -> commandRunner.setCommand(new Backward(command));
                case "like" -> commandRunner.setCommand(new Like(command));
                case "next" -> commandRunner.setCommand(new Next(command));
                case "prev" -> commandRunner.setCommand(new Prev(command));
                case "createPlaylist" -> commandRunner.setCommand(new CreatePlaylist(command));
                case "addRemoveInPlaylist" -> commandRunner.setCommand(
                         new AddRemoveInPlaylist(command));
                case "switchVisibility" -> commandRunner.setCommand(new SwitchVisibility(command));
                case "showPlaylists" -> commandRunner.setCommand(new ShowPlaylists(command));
                case "follow" -> commandRunner.setCommand(new Follow(command));
                case "status" -> commandRunner.setCommand(new Status(command));
                case "showPreferredSongs" -> commandRunner.setCommand(
                        new ShowPreferredSongs(command));
                case "getPreferredGenre" -> commandRunner.setCommand(
                        new GetPreferredGenre(command));
                case "getTop5Songs" -> commandRunner.setCommand(new GetTop5Songs(command));
                case "getTop5Playlists" -> commandRunner.setCommand(new GetTop5Playlists(command));
                case "switchConnectionStatus" -> commandRunner.setCommand(
                        new SwitchConnectionStatus(command));
                case "getOnlineUsers" -> commandRunner.setCommand(new GetOnlineUsers(command));
                case "addUser" -> commandRunner.setCommand(new AddUser(command));
                case "deleteUser" -> commandRunner.setCommand(new DeleteUser(command));
                case "addAlbum" -> commandRunner.setCommand(new AddAlbum(command));
                case "showAlbums" -> commandRunner.setCommand(new ShowAlbums(command));
                case "printCurrentPage" -> commandRunner.setCommand(new PrintCurrentPage(command));
                case "addMerch" -> commandRunner.setCommand(new AddMerch(command));
                case "addEvent" -> commandRunner.setCommand(new AddEvent(command));
                case "removeEvent" -> commandRunner.setCommand(new RemoveEvent(command));
                case "getAllUsers" -> commandRunner.setCommand(new GetAllUsers(command));
                case "addPodcast" -> commandRunner.setCommand(new AddPodcast(command));
                case "addAnnouncement" -> commandRunner.setCommand(new AddAnnouncement(command));
                case "removeAnnouncement" -> commandRunner.setCommand(
                        new RemoveAnnouncement(command));
                case "showPodcasts" -> commandRunner.setCommand(new ShowPodcasts(command));
                case "removeAlbum" -> commandRunner.setCommand(new RemoveAlbum(command));
                case "removePodcast" -> commandRunner.setCommand(new RemovePodcast(command));
                case "changePage" -> commandRunner.setCommand(new ChangePage(command));
                case "getTop5Albums" -> commandRunner.setCommand(new GetTop5Albums(command));
                case "getTop5Artists" -> commandRunner.setCommand(new GetTop5Artists(command));
                case "wrapped" -> commandRunner.setCommand(new Wrapped(command));
                case "buyMerch" -> commandRunner.setCommand(new BuyMerch(command));
                case "seeMerch" -> commandRunner.setCommand(new SeeMerch(command));
                case "buyPremium" -> commandRunner.setCommand(new BuyPremium(command));
                case "cancelPremium" -> commandRunner.setCommand(new CancelPremium(command));
                case "adBreak" -> commandRunner.setCommand(new AdBreak(command));
                case "subscribe" -> commandRunner.setCommand(new Subscribe(command));
                case "getNotifications" -> commandRunner.setCommand(new GetNotifications(command));
                case "loadRecommendations" -> commandRunner.setCommand(
                        new LoadRecommendations(command));
                case "updateRecommendations" -> commandRunner.setCommand(
                        new UpdateRecommendations(command));
                case "nextPage" -> commandRunner.setCommand(new NextPage(command));
                case "previousPage" -> commandRunner.setCommand(new PreviousPage(command));
                default -> System.out.println("Invalid command " + commandName);
            }
            outputs.add(commandRunner.run());
        }

        outputs.add(Admin.getInstance().endProgram());

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), outputs);

        Admin.resetInstance();
    }
}
