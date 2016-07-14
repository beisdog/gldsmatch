package com.beisert.gldsmatch

class Site {

    static constraints = {
        title nullable:true
        description nullable:true
        siteSurvey nullable:true
    }

    String siteName
    String title
    String description

    Survey siteSurvey

}
