package app

/**
 * Created by dbe on 14.07.16.
 */
interface EventBusSubscriber {

    public static class EventProcessingResult{
        boolean processed
        boolean ignored
        boolean stopProcessing
    }

    public  EventProcessingResult onEvent(EventBusEvent evt)

    public EventBus getEventBus()
    public void setEventBus(EventBus bus)

}
