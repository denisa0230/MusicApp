package app.subscriptions;

/**
 * Interface for Subject subscribers.
 */
public interface Subscriber {
    /**
     * Update based on the subject's change of state
     * @param newContent new content posted by
     *                   the subject
     * @param subject the subject
     */
    void update(String newContent, Subject subject);
}
