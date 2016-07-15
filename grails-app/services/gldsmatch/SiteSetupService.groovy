package gldsmatch

import app.view.EditProfileView
import app.view.SearchUsersView
import com.beisert.gldsmatch.Question
import com.beisert.gldsmatch.QuestionGroup
import com.beisert.gldsmatch.QuestionOption
import com.beisert.gldsmatch.SecUser
import com.beisert.gldsmatch.Site
import com.beisert.gldsmatch.SiteMenuItem
import com.beisert.gldsmatch.Survey
import com.vaadin.server.FontAwesome
import grails.transaction.Transactional

@Transactional
class SiteSetupService {

    SiteService siteService

    public void setUpData(Site site){

        //Survey
        Survey survey = new Survey(title: "Profile",site: site )
        survey.save(flush:true,failOnError:true)
        site.siteSurvey = survey
        site.save(flush:true, failOnError: true)

        QuestionGroup gr = new QuestionGroup(title: "Profile Information", survey: survey)
        survey.addToQuestionGroups(gr)
        survey.save(flush:true, failOnError: true)

        Question q = new Question(questionIndex:1,questionKey:"firstname",question:"Firstname",questionType:"text",questionGroup: gr)
        Question q1 = new Question(questionIndex:2,questionKey:"lastname",question:"Lastname",questionType:"text",questionGroup: gr)
        Question q2 = new Question(questionIndex:3,questionKey:"gender",question:"Gender",questionType:"optionGroup",optionType:"Gender",questionGroup: gr)
        Question q3 = new Question(questionIndex:4,questionKey:"ward",question:"Ward",questionType:"text",questionGroup: gr)
        Question q4 = new Question(questionIndex:5,questionKey:"stake",question:"Stake",questionType:"text",questionGroup: gr)
        Question q5 = new Question(questionIndex:6,questionKey:"country",question:"Country",questionType:"combo", optionType:"Country", questionGroup: gr)
        Question q6 = new Question(questionIndex:7,questionKey:"birthdate",question:"Birthdate",questionType:"date",questionGroup: gr)
        Question q7 = new Question(questionIndex:8,questionKey:"vegetarian",question:"Vegetarian",questionType:"checkbox",questionGroup: gr)
        Question q8 = new Question(questionIndex:9,questionKey:"musicstyles",question:"What are your favorite music styles",questionType:"optionGroup",multipleAllowed: true, optionType:"musicstyles",questionGroup: gr)
        Question q9 = new Question(questionIndex:10,questionKey:"height",question:"Your height",questionType:"combo",optionType:"height",questionGroup: gr)


        gr.addToQuestions(q)
        gr.addToQuestions(q1)
        gr.addToQuestions(q2)
        gr.addToQuestions(q3)
        gr.addToQuestions(q4)
        gr.addToQuestions(q5)
        gr.addToQuestions(q6)
        gr.addToQuestions(q7)
        gr.addToQuestions(q8)
        gr.addToQuestions(q9)


        gr.save(flush:true, failOnError: true)

        //Gender
        new QuestionOption(site:site, optionType: "Gender", optionKey:"M", optionValue: "Male").save(failOnError: true,flush: true)
        new QuestionOption(site:site, optionType: "Gender", optionKey:"F", optionValue: "Female").save(failOnError: true,flush: true)

        //Country
        new QuestionOption(site:site, optionType: "Country", optionKey:"CH", optionValue: "Switzerland").save(failOnError: true,flush: true)
        new QuestionOption(site:site, optionType: "Country", optionKey:"DE", optionValue: "Germany").save(failOnError: true,flush: true)
        new QuestionOption(site:site, optionType: "Country", optionKey:"UK", optionValue: "United Kingdom").save(failOnError: true,flush: true)
        new QuestionOption(site:site, optionType: "Country", optionKey:"NL", optionValue: "Netherlands").save(failOnError: true,flush: true)

        //Music styles
        new QuestionOption(site:site, optionType: "musicstyles", optionKey:"rock", optionValue: "Rock").save(failOnError: true,flush: true)
        new QuestionOption(site:site, optionType: "musicstyles", optionKey:"pop", optionValue: "Pop").save(failOnError: true,flush: true)
        new QuestionOption(site:site, optionType: "musicstyles", optionKey:"metal", optionValue: "Metal").save(failOnError: true,flush: true)
        new QuestionOption(site:site, optionType: "musicstyles", optionKey:"techno", optionValue: "Techno").save(failOnError: true,flush: true)

        //Height
        for(int i=100;i<220;){
            new QuestionOption(site:site, optionType: "height", optionKey:i + "_CM", optionValue: i+ "cm").save(failOnError: true,flush: true)
            i = i + 5
        }

        //Set the gender in all users
        List<SecUser> users = SecUser.findAll( "FROM SecUser s WHERE s.username LIKE 'jane%'")

        users.each {u ->
            siteService.setUserProfileStringValue(u,site,"gender","F")
        }

        users = SecUser.findAll( "FROM SecUser s WHERE s.username LIKE 'joe%'")
        users.each {u ->
            siteService.setUserProfileStringValue(u,site,"gender","M")
        }

        //Menu structure

        SiteMenuItem item = new SiteMenuItem(key: "profile", title: "Profile", icon: FontAwesome.USER.toString(),site: site, navigationTarget: EditProfileView.class.getName()).save(failOnError: true, flush:true)
        SiteMenuItem item1 = new SiteMenuItem(key: "searchUsers", title: "Users", icon: FontAwesome.GROUP.toString(), site: site, navigationTarget: SearchUsersView.class.getName()).save(failOnError: true, flush:true)


    }
}
