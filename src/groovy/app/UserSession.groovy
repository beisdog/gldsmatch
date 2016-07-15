package app

import app.data.SiteUserData
import com.beisert.gldsmatch.SecUser
import com.beisert.gldsmatch.Site
import com.beisert.gldsmatch.SurveyResponse
import com.vaadin.grails.Grails
import com.vaadin.server.VaadinService
import com.vaadin.server.VaadinSession
import com.vaadin.server.WrappedSession
import com.vaadin.ui.UI
import gldsmatch.SiteService
import grails.plugin.springsecurity.SpringSecurityService
import groovy.transform.CompileStatic

import javax.servlet.http.HttpSession

/**
 * Contains Session data
 */
@CompileStatic
class UserSession {

    static UserSession current = new UserSession()


    UserSession(){

    }

    public Site getSite(){
        return (Site)VaadinService.getCurrentRequest().getWrappedSession()
                .getAttribute("site")
    }

    public void setSite(Site s){
        VaadinService.getCurrentRequest().getWrappedSession()
                .setAttribute("site",s)
    }

    public WrappedSession getSession(){
        return VaadinService.getCurrentRequest().getWrappedSession()
    }

    public SecUser getCurrentUser(){
        SpringSecurityService sec = Grails.get(SpringSecurityService)
        SecUser user = (SecUser) sec.currentUser
        return user
    }

    public SiteUserData getCurrentSiteUser(){

        SecUser user = getCurrentUser()
        Site site = getSite()

        SiteUserData userData = (SiteUserData)getSession().getAttribute("siteUserData")

        if(userData == null){
            SurveyResponse resp = Grails.get(SiteService).getOrCreateProfileSurvey(user,site)
            userData = new SiteUserData(user:user,site:site,profile: resp)
            getSession().setAttribute("siteUserData", userData)
        }
        return userData
    }

    public SiteUserData refreshSiteUser(){

        SecUser user = getCurrentUser()
        Site site = getSite()

        SurveyResponse resp = Grails.get(SiteService).getOrCreateProfileSurvey(user,site)
        SiteUserData    userData = new SiteUserData(user:user,site:site,profile: resp)
        getSession().setAttribute("siteUserData", userData)

        return userData
    }
}
