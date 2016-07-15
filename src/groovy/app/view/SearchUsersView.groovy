package app.view

import app.UserSession
import app.comp.SiteUserTileComponent
import app.data.SiteUserData
import com.beisert.gldsmatch.SiteUser
import com.vaadin.grails.Grails
import com.vaadin.navigator.Navigator
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.CssLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import gldsmatch.SiteService
import groovy.transform.CompileStatic

@CompileStatic
class SearchUsersView extends CssLayout implements View{

    Collection<SiteUserData> users

    void init(Collection<SiteUserData> users){

        this.users = users
        Navigator navigator = com.vaadin.ui.UI.current.getNavigator()

        this.users.each {u ->

            addComponent(new SiteUserTileComponent(navigator, u))

        }

    }

    @Override
    void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        String mygender = UserSession.current.currentSiteUser.profile.getAnswerString("gender")
        String gender = mygender == "F" ? "M" : "F"
        Collection<SiteUserData> users = Grails.get(SiteService).findSiteUserDataByQuestionKeyAndStringValueAndSite("gender",gender,UserSession.current.site)
        init(users)
    }
}
