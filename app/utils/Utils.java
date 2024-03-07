package app.utils;

public final class Utils {
    public  static final int LIMIT = 5;
    public static final int FEBRUARY = 2;
    public static final int FEBRUARY_MAX_DAY = 28;
    public static final int MAX_DAY = 31;
    public static final int MAX_MONTH = 12;
    public static final int MAX_YEAR = 2023;
    public static final int MIN_YEAR = 1900;
    public static final Double INITIAL_PREMIUM_CREDIT = 1000000.0;
    public static final int FIRST_GENRE_LIMIT = 5;
    public static final int SECOND_GENRE_LIMIT = 3;
    public static final int THIRD_GENRE_LIMIT = 2;
    public static final int NUMBER_OF_GENRES = 3;
    public static final int TIME_LIMIT = 30;
    public static final Double HUNDRED = 100.0;
    public static final int TEN = 10;

    public enum Visibility {
        PUBLIC,
        PRIVATE
    }

    public enum RepeatMode {
        REPEAT_ALL, REPEAT_ONCE, REPEAT_INFINITE, REPEAT_CURRENT_SONG, NO_REPEAT,
    }

    public enum PlayerSourceType {
        LIBRARY, PLAYLIST, PODCAST, ALBUM
    }
    private Utils() { }

    /**
     * Get the minimum value out of two numbers.
     *
     * @param a first number
     * @param b second number
     * @return the minimum out of the two
     */
    public static int min(final int a, final int b) {
        return Math.min(a, b);
    }

}
