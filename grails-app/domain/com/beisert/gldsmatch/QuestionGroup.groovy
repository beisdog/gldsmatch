package com.beisert.gldsmatch

class QuestionGroup {

    static constraints = {
        description blank:true, nullable:true
    }

    List<Question> questions

    static hasMany = [questions:Question]
    static belongsTo = [survey: Survey]

    String title
    String description


}
