package app.comp

import com.beisert.gldsmatch.Question
import com.beisert.gldsmatch.QuestionAnswer
import com.beisert.gldsmatch.SurveyResponse
import com.vaadin.data.Property
import com.vaadin.data.util.BeanItem
import com.vaadin.ui.CheckBox
import com.vaadin.ui.ComboBox
import com.vaadin.ui.DateField
import com.vaadin.ui.FormLayout
import com.vaadin.ui.OptionGroup
import com.vaadin.ui.TextField
import groovy.transform.CompileStatic

@CompileStatic
class QuestionComp {

    SurveyResponse response
    Question question

    public QuestionComp(SurveyResponse resp, Question q) {
        response = resp
        question = q
    }
    public void addToFormLayout(FormLayout formLayout){
        if(question.questionType == "text"){
            BeanItem<Question> beanItem = new BeanItem<Question>(question,"answerStringValue")

            formLayout.addComponent(new TextField(caption:question.question, buffered: false, propertyDataSource: beanItem.getItemProperty("answerStringValue")))
        }
        if(question.questionType == "date"){
            BeanItem<Question> beanItem = new BeanItem<Question>(question,"answerDateValue")
            formLayout.addComponent(new DateField(caption:question.question, buffered: false, propertyDataSource: beanItem.getItemProperty("answerDateValue")))
        }
        if(question.questionType == "checkbox"){
            BeanItem<Question> beanItem = new BeanItem<Question>(question,"answerBooleanValue")
            formLayout.addComponent(new CheckBox(caption:question.question, buffered: false, propertyDataSource: beanItem.getItemProperty("answerBooleanValue")))
        }

        if(question.questionType == "combo"){
            BeanItem<Question> beanItem = new BeanItem<Question>(question,"answerStringValue")
            ComboBox cbo = new ComboBox(caption:question.question, buffered: false)
            cbo.setPropertyDataSource(beanItem.getItemProperty("answerStringValue"))
            question.options.each {o ->
                cbo.addItem(o.optionKey)
                cbo.setItemCaption(o.optionKey,o.optionValue)
            }
            cbo.setNullSelectionAllowed(true)
            formLayout.addComponent(cbo)
        }
        if(question.questionType == "optionGroup" && question.multipleAllowed==false){
            BeanItem<Question> beanItem = new BeanItem<Question>(question,"answerStringValue")
            OptionGroup optionGroup = new OptionGroup(question.question)
            optionGroup.setPropertyDataSource( beanItem.getItemProperty("answerStringValue"))
            question.options.each {o ->
                optionGroup.addItem(o.optionKey)
                optionGroup.setItemCaption(o.optionKey,o.optionValue)
            }
            formLayout.addComponent(optionGroup)
        }

        if(question.questionType == "optionGroup" && question.multipleAllowed){
            BeanItem<Question> beanItem = new BeanItem<Question>(question,"answerStringValue")
            OptionGroup multi = new OptionGroup(question.question)
            multi.setMultiSelect(true);
            question.options.each {o ->
                multi.addItem(o.optionKey)
                multi.setItemCaption(o.optionKey,o.optionValue)
            }
            question.answersOfUser.each {
                multi.select(it.stringValue)
            }
            multi.setImmediate(true)
            multi.addValueChangeListener(
                    new Property.ValueChangeListener() {
                        @Override
                        void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                            Collection<String> values = (Collection<String>) valueChangeEvent.property.value
                            //print "selected in multi: " + valueChangeEvent.property.value

                            List<QuestionAnswer> answers = new ArrayList<QuestionAnswer>()
                            for(String val: values){
                                QuestionAnswer a = new QuestionAnswer(surveyResponse: response,question: question)
                                a.stringValue = val
                                answers.add(a)
                            }


                            def set = new ArrayList<QuestionAnswer>()
                            for(QuestionAnswer a: response.answers){
                                if(a.question == question.id)
                                    set.add(a)
                            }
                            //response.answers.findAll( { a ->(a.question.id == question.id)})
                            response.answers.removeAll(set)

                            question.answersOfUser = answers
                            response.answers.addAll(answers)

                        }
                    }
            )
            formLayout.addComponent(multi)
        }

    }


}
