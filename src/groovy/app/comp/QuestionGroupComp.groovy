package app.comp

import com.beisert.gldsmatch.Question
import com.beisert.gldsmatch.QuestionGroup
import com.beisert.gldsmatch.SurveyResponse
import com.vaadin.ui.FormLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Panel
import com.vaadin.ui.VerticalLayout
import groovy.transform.CompileStatic

@CompileStatic
class QuestionGroupComp extends Panel{

    QuestionGroup group
    SurveyResponse response

    public QuestionGroupComp(QuestionGroup group, SurveyResponse r){
        this.group = group
        response = r

        setCaption(group.title)
        setSizeFull()
        setWidth("100%")

        FormLayout layout = new FormLayout()

        for(Question q:group.questions){
            def qComp = new QuestionComp(r,q)

            qComp.addToFormLayout(layout)
        }
        setContent(layout)
    }
}
