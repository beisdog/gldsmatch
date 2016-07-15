package app.data

import com.beisert.gldsmatch.SecUser
import com.beisert.gldsmatch.Site
import com.beisert.gldsmatch.SurveyResponse

/**
 * Value object for flattened data
 */
class SiteUserData {

    SecUser user
    Site site

    SurveyResponse profile

}
