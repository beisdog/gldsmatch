package app

import app.comp.EditAreaComponent
import app.comp.NavigationComponent
import com.vaadin.annotations.DesignRoot
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.declarative.Design
import groovy.transform.CompileStatic

@CompileStatic
@DesignRoot
class MainView extends VerticalLayout implements View {

    EditAreaComponent editArea
    NavigationComponent navigationArea

    EventBus eventBus


    public MainView(EventBus eventBus){
        this.eventBus = eventBus
        Design.read("MainView.html",this)

        this.navigationArea.setEventBus(this.eventBus)


    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    public void dispatchEvent(String eventType, Object payload){

    }
}
