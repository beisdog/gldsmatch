package app.comp

import app.DateUtil
import app.data.SiteUserData
import com.beisert.gldsmatch.QuestionAnswer
import com.vaadin.navigator.Navigator
import com.vaadin.server.FontAwesome
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import groovy.transform.CompileStatic

import java.time.LocalDate
import java.time.ZoneId

@CompileStatic
class SiteUserTileComponent extends CssLayout{

    SiteUserData userData
    Navigator navigator

    public SiteUserTileComponent(Navigator navigator, SiteUserData u){
        this.navigator = navigator
        this.userData = u

        setPrimaryStyleName("user-tile")

        HorizontalLayout contMain = new HorizontalLayout()

        Label pic = new Label()
        pic.setIcon(FontAwesome.USER)

        contMain.addComponent(pic)

        VerticalLayout right = new VerticalLayout()
        contMain.addComponent(right)

        right.addComponent(new Label(userData.user.username))
        String ageString = "?"

        QuestionAnswer a = userData.profile.findAnswerByQuestionKey("birthday")
        if(a != null) {
            LocalDate birthday = a.dateValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int age = DateUtil.calculateAge(birthday, LocalDate.now())
            ageString = age + "y"
        }
        right.addComponent(new Label(ageString))

        addComponent(contMain)



    }


}
