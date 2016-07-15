package com.beisert.gldsmatch

import javax.persistence.PrePersist

class SurveyResponse {

    static constraints = {
        createdAt nullable:true
        userName nullable:true
    }

    Survey survey
    SecUser user
    Long siteId
    Date createdAt
    /** calculated*/
    String userName


    static hasMany = [answers: QuestionAnswer]
    static mapping = {
        answers cascade: "all-delete-orphan"

    }

    static transients = ["questions"]

    def beforeInsert() {
        this.userName = this.user?.username
        updateSiteIdAndUserInAnswers()
    }
    def beforeUpdate() {
        this.userName = this.user?.username
        updateSiteIdAndUserInAnswers()
    }

    def updateSiteIdAndUserInAnswers(){
        this.answers.each { a ->
            a.siteId = siteId
            a.userName = this.user?.username
            a.user = this.user
            a.surveyId = this.survey?.id
        }
    }

    QuestionAnswer findAnswerByQuestionKey(String questionKey){
       return this.answers.find ({a ->
            a.questionKey == questionKey
        })
    }

    String getAnswerString(String questionKey){
        QuestionAnswer a = findAnswerByQuestionKey(questionKey)

        return a?.stringValue
    }

    Collection<QuestionAnswer> findAllAnswersByQuestionKey(String questionKey){
        return this.answers.collect ({a ->
            a.questionKey == questionKey
        })
    }

}
