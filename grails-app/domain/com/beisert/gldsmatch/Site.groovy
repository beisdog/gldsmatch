package com.beisert.gldsmatch

class Site {

    static constraints = {
        title nullable:true
        description nullable:true
        siteSurvey nullable:true
        parentSite nullable:true
        siteSurveyId nullable: true
    }

    String siteName
    String title
    String description

    Survey siteSurvey

    Long siteSurveyId

    Site parentSite

    static belongsTo = [parentSite:Site]

    static mapping = {
        siteSurveyId insertable: false, updateable: false
    }
}
