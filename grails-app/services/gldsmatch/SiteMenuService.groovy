package gldsmatch

import com.beisert.gldsmatch.SiteMenuItem
import com.beisert.gldsmatch.SecUser
import com.beisert.gldsmatch.Site
import grails.transaction.Transactional

@Transactional
class SiteMenuService {

    List<SiteMenuItem> findMenuItemsForSiteAndUser(Site site, SecUser user) {

        return SiteMenuItem.findAllBySite(site)

    }
}
