package app

/**
 * Created by dbe on 14.07.16.
 */
class EventBusEvent {

    String type
    EventBusSubscriber source
    Object payload

}
