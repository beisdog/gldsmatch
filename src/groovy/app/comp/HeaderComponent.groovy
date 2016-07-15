package app.comp

import com.beisert.gldsmatch.SecUser
import com.vaadin.annotations.DesignRoot
import com.vaadin.grails.Grails
import com.vaadin.ui.Button
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.declarative.Design
import grails.plugin.springsecurity.SpringSecurityService
import groovy.transform.CompileStatic



@CompileStatic
@DesignRoot
class HeaderComponent extends CssLayout{

    Label nameLabel
    Button logoutButton

    public HeaderComponent(){
        Design.read("HeaderComponent.html",this)

        setPrimaryStyleName("valo-menu")

        nameLabel = new Label("")
        logoutButton = new Button("Logout")

        logoutButton.addClickListener(
                new Button.ClickListener() {
                    @Override
                    void buttonClick(Button.ClickEvent clickEvent) {
                        getUI().getPage().setLocation("logout");
                    }
                }
        )
        SpringSecurityService sec = Grails.get(SpringSecurityService)
        SecUser user = (SecUser) sec.currentUser

        nameLabel.setValue(user?.username)

        addComponent(new Label("Welcome "))
        addComponent(nameLabel)
        Label space = new Label("")
        space.setWidth("100px")
        addComponent(space)
        addComponent(logoutButton)
        setWidth("100%")

    }

}
