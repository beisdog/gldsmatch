package gldsmatch

import app.data.SiteUserData
import app.view.EditProfileView
import com.beisert.gldsmatch.Question
import com.beisert.gldsmatch.QuestionAnswer
import com.beisert.gldsmatch.QuestionGroup
import com.beisert.gldsmatch.QuestionOption
import com.beisert.gldsmatch.SecRole
import com.beisert.gldsmatch.SecUser
import com.beisert.gldsmatch.SecUserSecRole
import com.beisert.gldsmatch.Site
import com.beisert.gldsmatch.SiteMenuItem
import com.beisert.gldsmatch.SiteUser
import com.beisert.gldsmatch.Survey
import com.beisert.gldsmatch.SurveyResponse
import com.vaadin.grails.Grails
import gldsmatch.data.SiteUserDataSearchInput
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



    public void saveSurveyResponse(SurveyResponse resp){
         resp.save(flush: true,failOnError: true)
    }


    public SurveyResponse getOrCreateProfileSurvey(SecUser user, Site site){
        Site s = Site.get(site.id)

        SurveyResponse resp = SurveyResponse.findBySurveyAndUser(s.siteSurvey,user)
        Survey survey = s.siteSurvey
        if(resp == null) {
            resp = new SurveyResponse(survey: survey, user: user, siteId: site.id)
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



    Collection<SiteUserData> findSiteUserDataByQuestionKeyAndStringValueAndSite(String questionKey, String stringValue,Site site){

        List<QuestionAnswer> answers = QuestionAnswer.findAllByQuestionKeyAndStringValueAndSiteIdAndSurveyId(questionKey,stringValue,site.id,site.siteSurveyId)
        Map<String, SiteUserData> resultMap = new LinkedHashMap<String,SiteUserData>()

        answers.each {a ->
            SiteUserData userData = resultMap.get(a.userName)
            if(userData == null) {
                userData = new SiteUserData(user:a.user,site:site, profile: getOrCreateProfileSurvey(a.user,site))
                resultMap.put(a.userName, userData)
            }
        }

        return resultMap.values()

    }

    Survey getSiteSurvey(Long siteId){
        Site site = Site.get(siteId)
        return site.siteSurvey
    }

    void setUserProfileStringValue(SecUser user,Site site, String questionKey, String stringValue){
        SurveyResponse resp = getOrCreateProfileSurvey(user, site)
        QuestionAnswer answer = resp.findAnswerByQuestionKey(questionKey)
        if(answer == null) {

            Question question = getQuestionBySurveyIdAndQuestionKey(site.siteSurveyId, questionKey)
            QuestionAnswer answerNew = new QuestionAnswer(question: question, surveyResponse: resp, stringValue: stringValue)
            resp.addToAnswers(answerNew)
            saveSurveyResponse(resp)
        }else{
            answer.stringValue = stringValue
            answer.save(failOnError: true, flush: true)
        }

    }

    Question getQuestionBySurveyIdAndQuestionKey(Long surveyId, String questionKey){
        return Question.findBySurveyIdAndQuestionKey(surveyId,questionKey)
    }

    SiteUserData getSiteUserDataByUsernameAndSite(String username, Site site) {
        SecUser user = SecUser.findByUsername(username)
        SurveyResponse resp = getOrCreateProfileSurvey(user,site)
        SiteUserData userData = new SiteUserData(user:user,site:site,profile: resp)
        return userData
    }
}
