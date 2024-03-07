package app.player;

import app.Admin;
import app.audio.Collections.AudioCollection;
import app.audio.Files.AudioFile;
import app.audio.LibraryEntry;
import app.user.User;
import app.utils.Utils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Player.
 */
public final class Player {
    private Utils.RepeatMode repeatMode;
    private boolean shuffle;

    @Setter
    private boolean paused;

    @Getter
    private PlayerSource source;
    @Getter
    private String type;
    private final int skipTime = 90;

    private ArrayList<PodcastBookmark> bookmarks = new ArrayList<>();

    @Getter
    @Setter
    private User user;

    @Setter
    @Getter
    private boolean adIncoming = false;
    @Getter
    @Setter
    private Integer adPrice = 0;


    /**
     * Instantiates a new Player.
     */
    public Player() {
        this.repeatMode = Utils.RepeatMode.NO_REPEAT;
        this.paused = true;
    }

    /**
     * Stop.
     */
    public void stop() {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }

        repeatMode = Utils.RepeatMode.NO_REPEAT;
        paused = true;
        source = null;
        shuffle = false;
    }

    private void bookmarkPodcast() {
        if (source != null && source.getAudioFile() != null) {
            PodcastBookmark currentBookmark =
                    new PodcastBookmark(source.getAudioCollection().getName(),
                                        source.getIndex(),
                                        source.getDuration());
            bookmarks.removeIf(bookmark -> bookmark.getName().equals(currentBookmark.getName()));
            bookmarks.add(currentBookmark);
        }
    }

    /**
     * Create source player source.
     *
     * @param type      the type
     * @param entry     the entry
     * @param bookmarks the bookmarks
     * @return the player source
     */
    public static PlayerSource createSource(final String type,
                                            final LibraryEntry entry,
                                            final List<PodcastBookmark> bookmarks) {
        PlayerSource playerSource;
        if ("podcast".equals(type)) {
            playerSource = createPodcastSource((AudioCollection) entry, bookmarks);
        } else {
            if ("song".equals(type)) {
                playerSource = new PlayerSource(Utils.PlayerSourceType.LIBRARY, (AudioFile) entry);
            } else {
                if ("playlist".equals(type)) {
                    playerSource = new PlayerSource(Utils.PlayerSourceType.PLAYLIST,
                            (AudioCollection) entry);
                } else {
                    if ("album".equals(type)) {
                        playerSource = new PlayerSource(Utils.PlayerSourceType.ALBUM,
                                (AudioCollection) entry);
                    } else {
                        return null;
                    }
                }
            }

        }
        return playerSource;
    }

    private static PlayerSource createPodcastSource(final AudioCollection collection,
                                                    final List<PodcastBookmark> bookmarks) {
        for (PodcastBookmark bookmark : bookmarks) {
            if (bookmark.getName().equals(collection.getName())) {
                return new PlayerSource(Utils.PlayerSourceType.PODCAST, collection, bookmark);
            }
        }
        return new PlayerSource(Utils.PlayerSourceType.PODCAST, collection);
    }

    /**
     * Sets source.
     *
     * @param entry the entry
     * @param sourceType  the sourceType
     */
    public void setSource(final LibraryEntry entry, final String sourceType) {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }

        this.type = sourceType;
        this.source = createSource(sourceType, entry, bookmarks);
        this.repeatMode = Utils.RepeatMode.NO_REPEAT;
        this.shuffle = false;
        this.paused = true;
    }

    /**
     * Pause.
     */
    public void pause() {
        paused = !paused;
    }

    /**
     * Shuffle.
     *
     * @param seed the seed
     */
    public void shuffle(final Integer seed) {
        if (seed != null) {
            source.generateShuffleOrder(seed);
        }

        if (source.getType() == Utils.PlayerSourceType.PLAYLIST
                || source.getType() == Utils.PlayerSourceType.ALBUM) {
            shuffle = !shuffle;
            if (shuffle) {
                source.updateShuffleIndex();
            }
        }
    }

    /**
     * Repeat enums . repeat mode.
     *
     * @return the enums . repeat mode
     */
    public Utils.RepeatMode repeat() {
        if (repeatMode == Utils.RepeatMode.NO_REPEAT) {
            if (source.getType() == Utils.PlayerSourceType.LIBRARY) {
                repeatMode = Utils.RepeatMode.REPEAT_ONCE;
            } else {
                repeatMode = Utils.RepeatMode.REPEAT_ALL;
            }
        } else {
            if (repeatMode == Utils.RepeatMode.REPEAT_ONCE) {
                repeatMode = Utils.RepeatMode.REPEAT_INFINITE;
            } else {
                if (repeatMode == Utils.RepeatMode.REPEAT_ALL) {
                    repeatMode = Utils.RepeatMode.REPEAT_CURRENT_SONG;
                } else {
                    repeatMode = Utils.RepeatMode.NO_REPEAT;
                }
            }
        }

        return repeatMode;
    }

    /**
     * Simulate player.
     *
     * @param time the time
     */
    public void simulatePlayer(final int time) {
        int elapsedTime = time;
        if (!paused) {
            while (elapsedTime >= source.getDuration()) {
                elapsedTime -= source.getDuration();
                next();
                if (paused) {
                    break;
                }
                // the player has moved to the next track => update user stats
                try {
                    if (!getCurrentAudioFile().getName().equals("Ad Break")) {
                        user.registerListen(source);
                    }
                } catch (Exception e) {
                    return;
                }
            }
            if (!paused) {
                source.skip(-elapsedTime);
            }
        }
    }

    /**
     * Next.
     */
    public void next() {
        if (adIncoming) {
            source.setAudioFile(Admin.getInstance().getAd());
            source.setRemainedDuration(Admin.getInstance().getAd().getDuration());
            user.payArtists();
            user.getInterAdsContent().clear();
            adIncoming = false;
            adPrice = 0;
        } else {
            paused = source.setNextAudioFile(repeatMode, shuffle);

            if (repeatMode == Utils.RepeatMode.REPEAT_ONCE) {
                repeatMode = Utils.RepeatMode.NO_REPEAT;
            }

            if (source.getDuration() == 0 && paused) {
                stop();
            }
        }
    }

    /**
     * Prev.
     */
    public void prev() {
        source.setPrevAudioFile(shuffle);
        paused = false;
    }

    private void skip(final int duration) {
        source.skip(duration);
        paused = false;
    }

    /**
     * Skip next.
     */
    public void skipNext() {
        if (source.getType() == Utils.PlayerSourceType.PODCAST) {
            skip(-skipTime);
        }
    }

    /**
     * Skip prev.
     */
    public void skipPrev() {
        if (source.getType() == Utils.PlayerSourceType.PODCAST) {
            skip(skipTime);
        }
    }

    /**
     * Gets current audio file.
     *
     * @return the current audio file
     */
    public AudioFile getCurrentAudioFile() {
        if (source == null) {
            return null;
        }
        return source.getAudioFile();
    }

    /**
     * Gets paused.
     *
     * @return the paused
     */
    public boolean getPaused() {
        return paused;
    }

    /**
     * Gets shuffle.
     *
     * @return the shuffle
     */
    public boolean getShuffle() {
        return shuffle;
    }

    /**
     * Gets stats.
     *
     * @return the stats
     */
    public PlayerStats getStats() {
        String filename = "";
        int duration = 0;
        if (source != null && source.getAudioFile() != null) {
            filename = source.getAudioFile().getName();
            duration = source.getDuration();
        } else {
            stop();
        }

        return new PlayerStats(filename, duration, repeatMode, shuffle, paused);
    }

}
