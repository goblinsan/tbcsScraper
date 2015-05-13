package mobileTbcs;

import java.util.Calendar;

public class CalendarUtils {
    public static Calendar setTimeToZero(Calendar calendarToChange) {
        Calendar changedCal = (Calendar)calendarToChange.clone();
        changedCal.set(Calendar.HOUR,0);
        changedCal.set(Calendar.MINUTE,0);
        changedCal.set(Calendar.SECOND,0);
        changedCal.set(Calendar.MILLISECOND,0);
        return changedCal;
    }

    static boolean theseAreTheSameDay(Calendar dayOne, Calendar dayTwo) {
        Calendar midnightDayOne = setTimeToZero(dayOne);
        Calendar midnightDayTwo = setTimeToZero(dayTwo);
        return midnightDayOne.compareTo(midnightDayTwo) == 0;
    }

    static boolean dayOneIsBeforeDayTwo(Calendar dayOne, Calendar dayTwo) {
        Calendar midnightDayOne = setTimeToZero(dayOne);
        Calendar midnightDayTwo = setTimeToZero(dayTwo);
        return midnightDayOne.compareTo(midnightDayTwo) < 0;
    }
}
