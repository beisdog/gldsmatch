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
        //
        siteId nullable: true
        userName nullable:true
        user nullable:true
        surveyId nullable:true
        questionKey nullable:true
    }

    String stringValue = ""
    Date dateValue
    BigDecimal numberValue
    Long longValue
    Integer index = 0
    Boolean booleanValue

    Date answeredAt

    /** Just for information and easier querying . It is set in beforeUpdate/beforeInsert in SurveyResponse*/
    Long siteId
    String userName
    SecUser user
    Long surveyId
    /** just for easier querying. It is calculated on save */
    String questionKey

    def beforeUpdate(){
        questionKey = question?.questionKey
    }
    def beforeInsert(){
        questionKey = question?.questionKey
    }


   static belongsTo = [surveyResponse:SurveyResponse, question: Question]
}
