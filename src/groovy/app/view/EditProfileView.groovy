package app.view

import app.EventBus
import app.EventBusEvent
import app.EventBusSubscriber
import app.MyUI
import app.UserSession
import app.comp.SurveyResponseComp
import app.data.SiteUserData
import com.beisert.gldsmatch.SurveyResponse
import com.vaadin.grails.Grails
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Notification
import com.vaadin.ui.VerticalLayout
import gldsmatch.SiteService
import groovy.transform.CompileStatic

@CompileStatic
class EditProfileView extends HorizontalLayout implements EventBusSubscriber,View{

    SurveyResponse surveyResponse
    Button saveButton
    EventBus eventBus

    public void init(EventBus eventBus, SurveyResponse r){

        this.surveyResponse = r
        this.eventBus = eventBus

        VerticalLayout v = new VerticalLayout()

        v.addComponent(new SurveyResponseComp(surveyResponse))

        saveButton = new Button("Save")
        v.addComponent(saveButton)
        saveButton.addClickListener(
                new Button.ClickListener() {
                    @Override
                    void buttonClick(Button.ClickEvent clickEvent) {
                        SiteService service = Grails.get(SiteService)
                        service.saveSurveyResponse(surveyResponse)
                        UserSession.current.refreshSiteUser()
                        Notification.show("Survey saved", "Successfully saved survey", Notification.Type.TRAY_NOTIFICATION);
                    }
                }

        )
        addComponent(v)

    }


    @Override
    EventBusSubscriber.EventProcessingResult onEvent(EventBusEvent evt) {
        return null
    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //Load data
        EventBus evtB = ((MyUI)com.vaadin.ui.UI.current).eventBus
        SurveyResponse r = UserSession.current?.currentSiteUser?.profile
        init(evtB, r)

    }
}
