package app.comp

import app.EventBus
import app.EventBusEvent
import app.EventBusSubscriber
import app.UserSession
import app.data.SiteUserData
import com.beisert.gldsmatch.SiteMenuItem
import com.vaadin.navigator.Navigator
import com.vaadin.server.ClassResource
import com.vaadin.server.FontAwesome
import com.vaadin.server.Resource
import com.vaadin.shared.ui.label.ContentMode
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.MenuBar
import com.vaadin.ui.themes.ValoTheme
import groovy.transform.CompileStatic

@CompileStatic
class NavigationComponent extends CssLayout implements EventBusSubscriber{

    EventBus eventBus
    List<SiteMenuItem> menuItems
    Navigator navigator


    public NavigationComponent(EventBus eventBus, List<SiteMenuItem> menuItems, Navigator navigator){
        this.eventBus = eventBus
        this.menuItems = menuItems
        this.navigator = navigator
        CssLayout menu = this;
        menu.addStyleName("large-icons");

        final Label logo = new Label("Va");
        logo.setSizeUndefined();
        logo.setPrimaryStyleName("valo-menu-logo");
        menu.addComponent(logo);

        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");
        final MenuBar.MenuItem settingsItem = settings.addItem(UserSession.current.currentSiteUser.user.username,
                FontAwesome.USER,
                null);
        settingsItem.addItem("Edit Profile", new MenuBar.Command() {
            @Override
            void menuSelected(MenuBar.MenuItem menuItem) {
                navigateTo("profile")
            }
        });

        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", new MenuBar.Command() {
            @Override
            void menuSelected(MenuBar.MenuItem menuItem) {
                getUI().getPage().setLocation("logout");
            }
        });
        menu.addComponent(settings)

        for (final SiteMenuItem item : menuItems) {

            final Button b = new Button(item.getTitle(), new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {

                    navigateTo(event.getButton().id);
                }
            });
            b.setId(item.key)
            b.setHtmlContentAllowed(true);
            b.setPrimaryStyleName("valo-menu-item");
            if(item.icon != null) {
                b.setIcon(FontAwesome.valueOf(item.icon))
            };
            menu.addComponent(b);
        }

    }

    @Override
    EventBusSubscriber.EventProcessingResult onEvent(EventBusEvent evt) {
        return null
    }

    public void select(String viewName){
        for (final Iterator<Component> it = this.iterator(); it
                .hasNext();) {
            it.next().removeStyleName("selected");
        }
        for (SiteMenuItem item: menuItems) {
            if (viewName.equals(item.getKey())) {
                for (final Iterator<Component> it = this
                        .iterator(); it.hasNext();) {
                    final Component c = it.next();
                    if (c.getCaption() != null
                            && c.getCaption().startsWith(
                            item.getTitle())) {
                        c.addStyleName("selected");
                        break;
                    }
                }
                break;
            }
        }
        //menu.removeStyleName("valo-menu-visible");
    }

    public void navigateTo(String key){
        navigator.navigateTo(key)
    }
}
