package app.audio.Collections;

import app.audio.Files.Episode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Setter;
import lombok.Getter;

/**
 * Class used for formatting the output of the showPodcasts command.
 */
@Getter
public class PodcastOutput {
    @Setter
    private String name;
    @Setter
    private List<String> episodes;
    public PodcastOutput(final Podcast podcast) {
        name = podcast.getName();
        episodes = podcast.getEpisodes().stream().
                map(Episode::getName).collect(Collectors.toList());
    }
}
