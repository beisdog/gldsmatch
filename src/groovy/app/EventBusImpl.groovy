package app


class EventBusImpl implements EventBus{

    Map<String,EventBusSubscriber> subscriberMap = new LinkedHashMap<String,EventBusSubscriber>()

    @Override
    void dispatchEvent(EventBusEvent evt) {
        for(EventBusSubscriber sub : subscriberMap.values()) {

            if(evt.source != sub){
                EventBusSubscriber.EventProcessingResult processingResult = sub.onEvent(evt)
                if(processingResult.stopProcessing){
                    return
                }
            }
        }
    }

    @Override
    void addSubscriber(String key, EventBusSubscriber subscriber) {
        this.subscriberMap.put(key,subscriber)
    }

    @Override
    EventBusSubscriber removeSubscriber(String key) {
        return this.subscriberMap.remove(key)
    }
}
