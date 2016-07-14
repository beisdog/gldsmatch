package com.beisert.gldsmatch

class SurveyResponse {

    static constraints = {
        createdAt nullable:true
    }

    Survey survey
    SecUser user
    Date createdAt

    List<QuestionAnswer> answersToDelete

    static hasMany = [answers: QuestionAnswer]
    static mapping = {
        answers cascade: "all-delete-orphan"
    }

    static transients = ["answersToDelete"]


}
