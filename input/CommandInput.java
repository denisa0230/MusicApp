package fileio.input;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public final class CommandInput {
    private String command;
    private String username;
    private Integer timestamp;
    private String type; // can be song/playlist/podcast or user/artist/host
    private FiltersInput filters; // for search
    private Integer itemNumber; // for select
    private Integer repeatMode; // for repeat
    private Integer playlistId; // for add/remove song
    private String playlistName; // for create playlist
    private Integer seed; // for shuffle
    private Integer age; // for addUser
    private String city; // for addUser
    private Integer releaseYear; // for album
    private String description; // for album/event
    private ArrayList<SongInput> songs; // for album
    private String date; // for event
    private String name; // for album
    private Integer price; // for merch
    private String nextPage;
    private Integer duration;
    private ArrayList<EpisodeInput> episodes;
    private String recommendationType;

    public CommandInput() {
    }
    public CommandInput(final CommandInput commandInput) {
        command = commandInput.getCommand();
        username = commandInput.getUsername();
        timestamp = commandInput.getTimestamp();
        type = commandInput.getType();
        filters = commandInput.getFilters();
        itemNumber = commandInput.getItemNumber();
        repeatMode = commandInput.getRepeatMode();
        playlistId = commandInput.getPlaylistId();
        playlistName = commandInput.getPlaylistName();
        seed = commandInput.getSeed();
        age = commandInput.getAge();
        city = commandInput.getCity();
        releaseYear = commandInput.getReleaseYear();
        description = commandInput.getDescription();
        songs = commandInput.getSongs();
        date = commandInput.getDate();
        name = commandInput.getName();
        price = commandInput.getPrice();
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setTimestamp(final Integer timestamp) {
        this.timestamp = timestamp;
    }

    public void setFilters(final FiltersInput filters) {
        this.filters = filters;
    }

    public void setItemNumber(final Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public void setRepeatMode(final Integer repeatMode) {
        this.repeatMode = repeatMode;
    }

    public void setPlaylistId(final Integer playlistId) {
        this.playlistId = playlistId;
    }

    public void setPlaylistName(final String playlistName) {
        this.playlistName = playlistName;
    }

    public void setSeed(final Integer seed) {
        this.seed = seed;
    }

    @Override
    public String toString() {
        return "CommandInput{"
                + "command='" + command + '\''
                + ", username='" + username + '\''
                + ", timestamp=" + timestamp
                + ", type='" + type + '\''
                + ", filters=" + filters
                + ", itemNumber=" + itemNumber
                + ", repeatMode=" + repeatMode
                + ", playlistId=" + playlistId
                + ", playlistName='" + playlistName + '\''
                + ", seed=" + seed
                + '}';
    }
}
