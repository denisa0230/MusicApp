package app.subscriptions;

/**
 * Interface for entities that support subscriptions.
 */
public interface Subject {
    /**
     * Add a subscriber.
     *
     * @param subscriber the subscriber
     */
    void addSubscriber(Subscriber subscriber);

    /**
     * Remove a subscriber.
     *
     * @param subscriber the subscriber
     */
    void removeSubscriber(Subscriber subscriber);

    /**
     * Notify subscribers of newly-posted content.
     *
     * @param newContent name of new content
     */
    void notifySubscribers(String newContent);
}
