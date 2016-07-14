package app.comp

import app.EventBus
import app.EventBusEvent
import app.EventBusSubscriber
import com.beisert.gldsmatch.Question
import com.beisert.gldsmatch.QuestionGroup
import com.beisert.gldsmatch.SecUser
import com.beisert.gldsmatch.Site
import com.beisert.gldsmatch.Survey
import com.beisert.gldsmatch.SurveyResponse
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.grails.Grails
import com.vaadin.server.Page
import com.vaadin.ui.Button
import com.vaadin.ui.Label
import com.vaadin.ui.Notification
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import gldsmatch.SiteService
import groovy.transform.CompileStatic

@CompileStatic
class EditProfileComp extends VerticalLayout implements EventBusSubscriber{

    SurveyResponse surveyResponse
    Button saveButton
    EventBus eventBus

    public EditProfileComp(EventBus eventBus, SurveyResponse r){
        this.surveyResponse = r
        this.eventBus = eventBus
        addComponent(new SurveyResponseComp(surveyResponse))

        saveButton = new Button("Save")
        addComponent(saveButton)
        saveButton.addClickListener(
                new Button.ClickListener() {
                    @Override
                    void buttonClick(Button.ClickEvent clickEvent) {
                        SiteService service = Grails.get(SiteService)
                        service.saveSurveyResponse(surveyResponse)
                        Notification.show("Survey saved", "Successfully saved survey", Notification.Type.TRAY_NOTIFICATION);
                    }
                }

        )
    }

    @Override
    EventBusSubscriber.EventProcessingResult onEvent(EventBusEvent evt) {
        return null
    }
}
