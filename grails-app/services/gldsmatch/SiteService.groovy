package gldsmatch

import com.beisert.gldsmatch.Question
import com.beisert.gldsmatch.QuestionAnswer
import com.beisert.gldsmatch.QuestionGroup
import com.beisert.gldsmatch.QuestionOption
import com.beisert.gldsmatch.SecRole
import com.beisert.gldsmatch.SecUser
import com.beisert.gldsmatch.SecUserSecRole
import com.beisert.gldsmatch.Site
import com.beisert.gldsmatch.SiteUser
import com.beisert.gldsmatch.Survey
import com.beisert.gldsmatch.SurveyResponse
import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional
import groovy.transform.CompileStatic


@Transactional
class SiteService {

    List<Site> getAllSites() {
        return Site.findAll(  )
    }

    List<SiteUser> getSitesForUser(SecUser user) {
        return SiteUser.findAllByUser( user )
    }

    List<SiteUser> getUsersForSite(Site site) {
        return SiteUser.findAllBySite( site )
    }



    Survey getSiteSurvey(Site site){

        return getSiteById(site.id).siteSurvey
    }

    Site getSiteById(Long id){
        Site freshSite = Site.get(id)

        Survey survey = freshSite.siteSurvey
        List<QuestionGroup> questionGroups = survey?.questionGroups

        if(questionGroups != null) {
            for(QuestionGroup g : questionGroups){
                if(g.questions!=null){
                    for(Question q: g.questions){
                    }
                }
            }
        }

        return freshSite
    }

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


    }

    public void saveSurveyResponse(SurveyResponse resp){

        //resp.answersToDelete.each {it.delete(id:it.id)}

        resp.save(flush: true,failOnError: true)


    }


        public SurveyResponse getOrCreateProfileSurvey(SecUser user, Site site){
        Site s = Site.get(site.id)

        SurveyResponse resp = SurveyResponse.findBySurveyAndUser(s.siteSurvey,user)
        Survey survey = s.siteSurvey
        if(resp == null) {
            resp = new SurveyResponse(survey: survey, user: user)
        }
        //Prefill the answers
        List<Question> questions = getAllQuestionsInSurvey(survey)
        for(Question q: questions){

            if(q.multipleAllowed ) {
                List<QuestionAnswer> answers = getAnswersInSurveryResponse(resp,q)
                q.answersOfUser = answers
            }
            else{
                QuestionAnswer a = getOrCreateAnswerInSurveyResponse(resp,q)
                q.answersOfUser = Arrays.asList(a)
            }
            //options
            if(q.optionType != null){
                if(q.optionFilter != null) {

                    def options = QuestionOption.findAll(
                            "from QuestionOption option WHERE "
                                    + "option.optionType = '" + q.optionType + "' AND "
                                    + "(option.site.id is null OR option.site.id = " + site.id + ") AND" + q.optionFilter)
                    q.options = options
                }else{
                    def options = QuestionOption.findAll(
                            "from QuestionOption option WHERE "
                                    + "option.optionType = '" + q.optionType + "' AND "
                                    + "(option.site.id is null OR option.site.id = " + site.id + ")"
                    )
                    q.options = options
                }
            }
        }
        resp.save(flush: true, failOnError: true)
        return resp
    }
    QuestionAnswer getOrCreateAnswerInSurveyResponse(SurveyResponse r, Question q){

        if(r.answers!=null){
            for(QuestionAnswer a: r.answers){
                if(a.question.id == q.id) return a
            }
        }
        QuestionAnswer answer = new QuestionAnswer(question: q,surveyResponse: r, surveyId:r.survey.id)
        r.addToAnswers(answer)

        return answer
    }

    List<QuestionAnswer> getAnswersInSurveryResponse(SurveyResponse r, Question q){
        List<QuestionAnswer> list = new ArrayList<QuestionAnswer>()
        if(r.answers!=null){
            for(QuestionAnswer a: r.answers){
                if(a.question.id == q.id) list.add( a )
            }
        }
        return list
    }


    //Survey must be active in session
    private List<Question> getAllQuestionsInSurvey(Survey s){
        List<Question> result = new ArrayList<Question>()

        s.questionGroups.each ({g ->
            result.addAll(g.questions)
        })

        return result
    }

}
