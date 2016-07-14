package com.beisert.gldsmatch

class SiteUser {

    static constraints = {
    }

    static mapping = {
        site lazy: false
        user lazy:false
    }
    Site site
    SecUser user
}
