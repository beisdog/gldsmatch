package app

import app.comp.EditAreaComponent
import app.comp.EditProfileComp
import com.beisert.gldsmatch.QuestionGroup
import com.beisert.gldsmatch.SecUser
import com.beisert.gldsmatch.Site
import com.beisert.gldsmatch.SiteUser
import com.beisert.gldsmatch.Survey
import com.beisert.gldsmatch.SurveyResponse
import com.vaadin.annotations.Theme
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.Label
import com.vaadin.grails.Grails
import gldsmatch.SiteService
import grails.plugin.springsecurity.SpringSecurityService
import groovy.transform.CompileStatic

/**
 *
 *
 * @author
 */
@CompileStatic
@Theme("valo")
class MyUI extends UI {

    private static boolean dataLoaded = false

    EventBus eventBus = new EventBusImpl()


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        SiteService siteService = Grails.get(SiteService)

        if(dataLoaded == false) {
            List<Site> allSites = siteService.getAllSites()
            for(Site site: allSites){
                siteService.setUpData(site)
            }
            dataLoaded = true
        }

        //show users in edit view
		VerticalLayout layout = new VerticalLayout()

        SecUser user = (SecUser)Grails.get(SpringSecurityService).currentUser
        List<SiteUser> sites = siteService.getSitesForUser(user)
        Long lastSiteId = null
        for(SiteUser s: sites){
            layout.addComponent(new Label(s.site.siteName) )

                Site site = s.site
                print "Site " + site
                //initialize all relations
                lastSiteId = site.id

        }

        MainView mainView = new MainView(this.eventBus)
        mainView.editArea.init(layout)
        // Get a fully loaded site
        Site site = siteService.getSiteById(lastSiteId)
        Survey survey = site.siteSurvey

        SurveyResponse surveyResponse = siteService.getOrCreateProfileSurvey(user, site)

        mainView.editArea.init(
                new EditProfileComp(this.eventBus, surveyResponse)
        )

		setContent(mainView)
    }
}
