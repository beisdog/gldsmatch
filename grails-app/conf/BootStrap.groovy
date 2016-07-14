import com.beisert.gldsmatch.Question
import com.beisert.gldsmatch.QuestionGroup
import com.beisert.gldsmatch.SecRole
import com.beisert.gldsmatch.SecUser
import com.beisert.gldsmatch.SecUserSecRole
import com.beisert.gldsmatch.Site
import com.beisert.gldsmatch.SiteUser
import com.beisert.gldsmatch.Survey
import gldsmatch.SiteService


class BootStrap {

    static boolean dataLoaded = false

    def init = { servletContext ->

        //Site
        Site site = new Site(siteName:"convention1", title: "Summerconvention Oslo 20016", description:"Summer fun and mingle")
        site.save(flush:true, failOnError: true)

        SecRole adminRole = new SecRole(authority: 'ROLE_ADMIN').save()
        SecRole userRole = new SecRole(authority: 'ROLE_USER').save()

        SecUser adminUser = new SecUser(username: 'admin', password: 'admin').save(flush:true, failOnError: true)
        SecUserSecRole.create adminUser, adminRole
        for(int i;i<10;i++) {
            SecUser janeUser = new SecUser(username: 'jane'+i, password: 'doe').save(flush:true, failOnError: true)
            SecUser joeUser = new SecUser(username: 'joe'+i, password: 'doe').save(flush:true, failOnError: true)

            SecUserSecRole.create janeUser, userRole
            SecUserSecRole.create joeUser, userRole

            SiteUser ass1 = new SiteUser(site:site , user:janeUser).save(flush:true, failOnError: true)
            SiteUser ass2 = new SiteUser(site:site , user:joeUser).save(flush:true, failOnError: true)
        }

        SecUserSecRole.withSession {
            it.flush()
            it.clear()
        }


    }
    def destroy = {
    }
}
