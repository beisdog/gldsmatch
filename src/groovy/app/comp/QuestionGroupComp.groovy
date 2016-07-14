package app.comp

import com.beisert.gldsmatch.Question
import com.beisert.gldsmatch.QuestionGroup
import com.beisert.gldsmatch.SurveyResponse
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import groovy.transform.CompileStatic

@CompileStatic
class QuestionGroupComp extends VerticalLayout{

    QuestionGroup group
    SurveyResponse response

    public QuestionGroupComp(QuestionGroup group, SurveyResponse r){
        this.group = group
        response = r

        addComponent(new Label(group.title))

        for(Question q:group.questions){
            addComponent(new QuestionComp(r,q))
        }

    }
}
