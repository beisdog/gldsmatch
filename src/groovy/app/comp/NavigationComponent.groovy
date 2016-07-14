package app.comp

import app.EventBus
import app.EventBusEvent
import app.EventBusSubscriber
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import groovy.transform.CompileStatic

@CompileStatic
class NavigationComponent extends VerticalLayout implements EventBusSubscriber{

    EventBus eventBus
    public NavigationComponent(){
        addComponent(new Label("Navigation"))


    }

    @Override
    EventBusSubscriber.EventProcessingResult onEvent(EventBusEvent evt) {
        return null
    }
}
