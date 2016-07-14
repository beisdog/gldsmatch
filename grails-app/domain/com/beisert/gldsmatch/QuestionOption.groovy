package com.beisert.gldsmatch

class QuestionOption {

    static constraints = {
        site nullable: true
    }

    Site site

    String optionKey
    String optionValue

    String optionType

    static belongsTo = [site:Site]

}
