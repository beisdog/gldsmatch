package app.comp

import com.beisert.gldsmatch.QuestionGroup
import com.beisert.gldsmatch.Survey
import com.beisert.gldsmatch.SurveyResponse
import com.vaadin.server.FontAwesome
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.VerticalLayout

/**
 * Created by dbe on 13.07.16.
 */
class SurveyResponseComp extends Panel{

    SurveyResponse response

    public SurveyResponseComp(SurveyResponse r){
        this.response = r
        setWidth("100%")
        Survey survey = response.survey
        setIcon(FontAwesome.EDIT)
        VerticalLayout layout = new VerticalLayout()

        setCaption(survey.title)
        layout.setMargin(true)
        for(QuestionGroup g: survey.questionGroups){
            layout.addComponent(new QuestionGroupComp(g,response))
        }
        setContent(layout)
    }
}
