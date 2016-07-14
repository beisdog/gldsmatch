package com.beisert.gldsmatch

class Survey {

    static constraints = {
        help nullable: true
        description nullable: true
    }

    String title
    String description
    String help

    List<QuestionGroup> questionGroups

    static hasMany = [questionGroups:QuestionGroup]
}
