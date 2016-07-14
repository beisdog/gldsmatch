package app.comp

import app.EventBus
import app.EventBusEvent
import app.EventBusSubscriber
import com.vaadin.ui.Component
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import groovy.transform.CompileStatic

@CompileStatic
class EditAreaComponent extends CustomComponent {



    public EditAreaComponent(){
        super(new Label("Edit Area"))
    }
    public EditAreaComponent(Component comp){
        super(comp)
    }

    public void init(Component comp){
        setCompositionRoot(comp)
    }


}
