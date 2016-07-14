package app.comp

import com.beisert.gldsmatch.Question
import com.beisert.gldsmatch.QuestionAnswer
import com.beisert.gldsmatch.SurveyResponse
import com.sun.tools.javac.main.Option
import com.vaadin.data.Property
import com.vaadin.data.util.BeanItem
import com.vaadin.ui.CheckBox
import com.vaadin.ui.ComboBox
import com.vaadin.ui.DateField
import com.vaadin.ui.OptionGroup
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import groovy.transform.CompileStatic


class QuestionComp extends VerticalLayout{

    SurveyResponse response
    Question question

    public QuestionComp(SurveyResponse resp, Question q){
        response = resp
        question = q

        if(question.questionType == "text"){
            BeanItem<Question> beanItem = new BeanItem<Question>(question,"answerStringValue")

            addComponent(new TextField(caption:question.question, buffered: false, propertyDataSource: beanItem.getItemProperty("answerStringValue")))
        }
        if(question.questionType == "date"){
            BeanItem<Question> beanItem = new BeanItem<Question>(question,"answerDateValue")
            addComponent(new DateField(caption:question.question, buffered: false, propertyDataSource: beanItem.getItemProperty("answerDateValue")))
        }
        if(question.questionType == "checkbox"){
            BeanItem<Question> beanItem = new BeanItem<Question>(question,"answerBooleanValue")
            addComponent(new CheckBox(caption:question.question, buffered: false, propertyDataSource: beanItem.getItemProperty("answerBooleanValue")))
        }

        if(question.questionType == "combo"){
            BeanItem<Question> beanItem = new BeanItem<Question>(question,"answerStringValue")
            ComboBox cbo = new ComboBox(caption:question.question, buffered: false)
            cbo.setPropertyDataSource(beanItem.getItemProperty("answerStringValue"))
            q.options.each {o ->
                cbo.addItem(o.optionKey)
                cbo.setItemCaption(o.optionKey,o.optionValue)
            }
            cbo.setNullSelectionAllowed(true)
            addComponent(cbo)
        }
        if(question.questionType == "optionGroup" && question.multipleAllowed==false){
            BeanItem<Question> beanItem = new BeanItem<Question>(question,"answerStringValue")
            OptionGroup optionGroup = new OptionGroup(q.question)
            optionGroup.setPropertyDataSource( beanItem.getItemProperty("answerStringValue"))
            q.options.each {o ->
                optionGroup.addItem(o.optionKey)
                optionGroup.setItemCaption(o.optionKey,o.optionValue)
            }
            addComponent(optionGroup)
        }

        if(question.questionType == "optionGroup" && question.multipleAllowed){
            BeanItem<Question> beanItem = new BeanItem<Question>(question,"answerStringValue")
            OptionGroup multi = new OptionGroup(q.question)
            multi.setMultiSelect(true);
            q.options.each {o ->
                multi.addItem(o.optionKey)
                multi.setItemCaption(o.optionKey,o.optionValue)
            }
            q.answersOfUser.each {
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
                                QuestionAnswer a = new QuestionAnswer(surveyResponse: response,question: q)
                                a.stringValue = val
                                answers.add(a)
                            }

                            response.answersToDelete = new ArrayList<QuestionAnswer>()

                            Collection<QuestionAnswer> set = response.answers.findAll {
                                        //Collect all answers that are for this question and not in the new answers
                                        it.question.id == q.id
                                    }
                            response.answersToDelete.addAll(set)
                            response.answers.removeAll(response.answersToDelete)

                            q.answersOfUser = answers
                            resp.answers.addAll(answers)

                        }
                    }
            )
            addComponent(multi)
        }

    }


}
