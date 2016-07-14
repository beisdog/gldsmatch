package app

/**
 * Created by dbe on 14.07.16.
 */
interface EventBus {

    public void dispatchEvent(EventBusEvent evt);

    void addSubscriber(String key, EventBusSubscriber subscriber)

    EventBusSubscriber removeSubscriber(String key)

}
