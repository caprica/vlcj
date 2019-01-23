package uk.co.caprica.vlcj.eventmanager;

/**
 * Specification for a event that notifies another component.
 *
 * @param <L> type of object that receives notifications (i.e. a "listener" type)
 */
public interface EventNotification<L> {

    void notify(L listener);

}