package app

import org.apache.commons.lang3.time.DateUtils

import java.time.LocalDate
import java.time.Period

/**
 * Created by dbe on 15.07.16.
 */
class DateUtil {

    public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }
}
