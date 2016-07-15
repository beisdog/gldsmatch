package com.beisert.gldsmatch

class Question {

    static constraints = {
        questionKey nullable: false
        title nullable: true, blank:true
        help nullable:true, blank: true
        valueProviderClassName nullable:true
        valueProviderParams nullable:true
        validatorClassName nullable:true
        questionGroup nullable:true //only because it is easier
        optionType nullable:true
        optionFilter nullable:true
        surveyId nullable: true
    }

    String questionKey
    String question
    Integer questionIndex

    String help
    String title
    String questionType
    boolean multipleAllowed

    String optionType
    String optionFilter

    String valueProviderClassName
    String valueProviderParams

    String validatorClassName

    // filled before persist by questionGroup for easier querying
    Long surveyId

    //transient
    List<QuestionAnswer> answersOfUser
    //transient
    List<QuestionOption> options

    static transients = ["options", "answersOfUser" , "answer", "answerBooleanValue","answerDateValue","answerStringValue"]
    static belongsTo = [questionGroup:QuestionGroup]


    //Utility methods for accessing the answer value
    QuestionAnswer getAnswer(){
        return answersOfUser.get(0)
    }
    String getAnswerStringValue() {
        getAnswer().stringValue
    }

    void setAnswerStringValue(String v){
        getAnswer().stringValue = v
    }
    Date getAnswerDateValue() {
        getAnswer().dateValue
    }

    void setAnswerDateValue(Date v){
        getAnswer().dateValue = v
    }
    Boolean getAnswerBooleanValue() {
        getAnswer().booleanValue
    }

    void setAnswerBooleanValue(Boolean v){
        getAnswer().booleanValue = v
    }
}
