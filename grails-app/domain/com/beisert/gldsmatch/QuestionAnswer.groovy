package com.beisert.gldsmatch

class QuestionAnswer {

    static constraints = {
        stringValue nullable:true
        dateValue nullable:true
        numberValue nullable:true
        longValue nullable:true
        index nullble:true
        booleanValue nullable:true
        answeredAt nullable:true
    }

    String stringValue = ""
    Date dateValue
    BigDecimal numberValue
    Long longValue
    Integer index = 0
    Boolean booleanValue

    Date answeredAt




   static belongsTo = [surveyResponse:SurveyResponse, question: Question]
}
