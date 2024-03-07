package app.user.userFeatures;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a typical recommendation
 * for a user.
 */
@Getter
@Setter
public class Recommendation {
    private Song song;
    private Playlist playlist;
    private String type;
}
