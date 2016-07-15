package app

import app.comp.HeaderComponent
import app.comp.NavigationComponent
import app.data.SiteUserData
import com.beisert.gldsmatch.SiteMenuItem
import com.beisert.gldsmatch.SecUser
import com.beisert.gldsmatch.Site
import com.beisert.gldsmatch.SiteUser
import com.beisert.gldsmatch.Survey
import com.vaadin.annotations.Theme
import com.vaadin.navigator.Navigator
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.server.Page
import com.vaadin.ui.Component
import com.vaadin.ui.ComponentContainer
import com.vaadin.ui.UI
import com.vaadin.server.VaadinRequest
import com.vaadin.grails.Grails
import com.vaadin.ui.themes.ValoTheme
import gldsmatch.SiteMenuService
import gldsmatch.SiteService
import gldsmatch.SiteSetupService
import grails.plugin.springsecurity.SpringSecurityService
import groovy.transform.CompileStatic

/**
 *
 *
 * @author
 */
@CompileStatic
@Theme("gldsmatch")
class MyUI extends UI {

    private static boolean dataLoaded = false

    EventBus eventBus = new EventBusImpl()

    ValoMenuLayout root = new ValoMenuLayout()
    ComponentContainer viewDisplay = root.getContentContainer()


    @Override
    protected void init(VaadinRequest vaadinRequest) {

        //Load data
        SiteService siteService = Grails.get(SiteService)
        SiteSetupService siteSetupService = Grails.get(SiteSetupService)
        if(dataLoaded == false) {
            List<Site> allSites = siteService.getAllSites()
            for(Site site: allSites){
                siteSetupService.setUpData(site)
            }
            dataLoaded = true
        }



        SecUser user = (SecUser)Grails.get(SpringSecurityService).currentUser
        List<SiteUser> sites = siteService.getSitesForUser(user)
        Long lastSiteId = null
        for(SiteUser s: sites){
                Site site = s.site
                print "Site " + site
                //initialize all relations
                lastSiteId = site.id

        }
        Site site = siteService.getSiteById(lastSiteId)
        Survey survey = site.siteSurvey
        UserSession.current.site = site


        List<SiteMenuItem> menuItems = Grails.get(SiteMenuService).findMenuItemsForSiteAndUser(site,user)
        SiteUserData userData = UserSession.current.getCurrentSiteUser()

        // -------------- Layout
        if (browserCantRenderFontsConsistently()) {
            getPage().getStyles().add(
                    ".v-app.v-app.v-app {font-family: Sans-Serif;}");
        }


        getPage().setTitle("LDS Match");
        setContent(root);
        root.setWidth("100%");

        //MENU
        navigator = new Navigator(this, viewDisplay);

        menuItems.each { m ->
            navigator.addView(m.key, (Class<? extends View>)Class.forName(m.navigationTarget))
        }

        def navigationComp = new NavigationComponent(eventBus,menuItems, navigator)
        root.addMenu(navigationComp)

        addStyleName(ValoTheme.UI_WITH_MENU);



        final String f = Page.getCurrent().getUriFragment();
        if (f == null || f.equals("")) {
            navigator.navigateTo(menuItems.get(0).key);
        }

        navigator.addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(final ViewChangeListener.ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(final ViewChangeListener.ViewChangeEvent event) {

                navigationComp.select(event.getViewName())
            }
        });

    }

    private boolean browserCantRenderFontsConsistently() {
        // PhantomJS renders font correctly about 50% of the time, so
        // disable it to have consistent screenshots
        // https://github.com/ariya/phantomjs/issues/10592

        // IE8 also has randomness in its font rendering...

        return getPage().getWebBrowser().getBrowserApplication()
                .contains("PhantomJS") || (getPage().getWebBrowser().isIE() && getPage()
                .getWebBrowser().getBrowserMajorVersion() <= 9);
    }


}
