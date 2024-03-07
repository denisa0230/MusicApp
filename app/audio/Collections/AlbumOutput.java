package app.audio.Collections;

import java.util.ArrayList;
import lombok.Setter;
import lombok.Getter;

/**
 * Class used for formatting the output of the showAlbums command.
 */
public class AlbumOutput {
    @Setter
    @Getter
    private String name;

    @Getter
    private ArrayList<String> songs = new ArrayList<>();
    public AlbumOutput(final Album album) {
        name = album.getName();
        album.getSongs().forEach(song -> songs.add(song.getName()));
    }

}
