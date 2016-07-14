package app.comp

import com.beisert.gldsmatch.QuestionGroup
import com.beisert.gldsmatch.Survey
import com.beisert.gldsmatch.SurveyResponse
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

/**
 * Created by dbe on 13.07.16.
 */
class SurveyResponseComp extends VerticalLayout{

    SurveyResponse response

    public SurveyResponseComp(SurveyResponse r){
        this.response = r

        Survey survey = response.survey

        addComponent(new Label(value: survey.title,styleName:"header"))
        for(QuestionGroup g: survey.questionGroups){
            addComponent(new QuestionGroupComp(g,response))
        }
    }
}
