package com.beisert.gldsmatch

class SiteMenuItem {

    static constraints = {
        icon nullable: true
        navigationTarget nullable: true
        parent nullable: true
    }

    String key
    String title
    String icon

    String navigationTarget

    Site site

    List<SiteMenuItem> children
    SiteMenuItem parent

    static hasMany = [children: SiteMenuItem]
    static belongsTo = [parent:SiteMenuItem]
}
