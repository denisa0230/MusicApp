package app.user;

import app.subscriptions.Subject;
import app.subscriptions.Subscriber;
import lombok.Getter;

import java.util.ArrayList;

/**
 * This class represents a generic content creator on the app.
 */
@Getter
public abstract class ContentCreator extends UserAbstract implements Subject {
    private ArrayList<Subscriber> subscribers = new ArrayList<>();

    public ContentCreator(final String username, final int age, final String city) {
         super(username, age, city);
    }

    /**
     * Add a subscriber to the subscriber's list.
     *
     * @param subscriber the subscriber
     */
    @Override
    public void addSubscriber(final Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    /**
     * Checks if the content creator has a certain subscriber
     *
     * @param subscriber the subscriber
     * @return true if the subscriber is indeed a subscriber,
     *         false if it is not
     */
    public boolean hasSubscriber(final Subscriber subscriber) {
         return subscribers.contains(subscriber);
     }

    /**
     * Remove a subscriber from the subscriber's list.
     *
     * @param subscriber the subscriber
     */
    @Override
    public void removeSubscriber(final Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    /**
     * Notify subscribers about new content.
     *
     * @param newContent name of new content
     */
    @Override
    public void notifySubscribers(final String newContent) {
        subscribers.forEach(subscriber -> subscriber.update(newContent, this));
    }
}
