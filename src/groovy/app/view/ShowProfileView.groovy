package app.view

import app.EventBus
import app.EventBusEvent
import app.EventBusSubscriber
import app.UserSession
import app.data.SiteUserData
import com.beisert.gldsmatch.Site
import com.beisert.gldsmatch.SurveyResponse
import com.vaadin.grails.Grails
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.VerticalLayout
import gldsmatch.SiteService

/**
 * Created by dbe on 15.07.16.
 */
class ShowProfileView extends VerticalLayout implements EventBusSubscriber,View{

    EventBus eventBus
    SurveyResponse profile

    public void init(EventBus eventBus, SurveyResponse profile){
        this.eventBus = eventBus



    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        String parameters = viewChangeEvent.getParameters()
        Site site = UserSession.current.site
        SiteUserData userData = Grails.get(SiteService).getSiteUserDataByUsernameAndSite(parameters,site)

        init(userData.profile)

    }

    @Override
    EventBusSubscriber.EventProcessingResult onEvent(EventBusEvent evt) {
        return new EventBusSubscriber.EventProcessingResult(stopProcessing: false, processed:false, ignore: true)
    }
}
