package mobileTbcs;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

    /*
    List of things to do:
    - return list of games for a given team
    -- each game has a time, opponent (name, color, record), field
    - select a team
    - store selection

    - Display team selection menu
    -- Sport, Day, Team Name
    -- Predictive team name search
     */

public class ShowMySchedulesTest {
    Schedule simpleSchedule = new Schedule();
    private Calendar arbitraryToday;

    @Before
    public void setUp(){
        arbitraryToday = Calendar.getInstance();
        arbitraryToday.set(2015, Calendar.FEBRUARY, 24);
        simpleSchedule.setToday(arbitraryToday);
    }

    @Test
    public void createScheduleHasOneGame(){
        Schedule simpleSchedule = new Schedule();
        assertEquals(1,simpleSchedule.getNumberOfGames());
    }

    @Test
    public void print3GameSchedule(){
        createScheduleWith3Games();
        simpleSchedule.printNextGames();
    }

    @Test
    public void createScheduleWith3Games(){
        List<Calendar> testGames = createDummyGames(3, arbitraryToday);
        simpleSchedule.setGames(testGames);
        assertEquals(3, simpleSchedule.getNumberOfGames());
    }

    @Test
    public void printSingleGameSchedule(){
        String expectedSchedule = "Tonight at 7:30";
        assertEquals(expectedSchedule, simpleSchedule.showNextGame());
    }
    @Test
    public void getNextScheduleDayTestWhenGameIsToday(){
        String expectedNextDay = "Tonight";
        assertEquals(expectedNextDay, simpleSchedule.getNextScheduleDay());
    }
    @Test
    public void getNextScheduleDayTestWhenGameIsNotToday(){
        String expectedNextDay = "24";
        Calendar arbitraryNotToday = Calendar.getInstance();
        arbitraryNotToday.set(2015, Calendar.FEBRUARY, 10);
        simpleSchedule.setToday(arbitraryNotToday);
        assertEquals(expectedNextDay, simpleSchedule.getNextScheduleDay());
    }

    @Test
    public void setCalendarTimeToZeroTest(){
        Schedule simpleSchedule = new Schedule();
        Calendar expectedReturnCalendar = Calendar.getInstance();
        expectedReturnCalendar.set(2015, 1, 24, 0, 0, 0);
        Calendar calendarToChange = Calendar.getInstance();
        calendarToChange.set(2015, 1, 24, 1, 2, 3);
        Calendar actualReturnedCalendar = CalendarUtils.setTimeToZero(calendarToChange);
        expectedReturnCalendar.compareTo(actualReturnedCalendar);
    }

    @Test
    public void simpleCalendarTests(){
        Calendar c = Calendar.getInstance();
        c.set(2015,Calendar.FEBRUARY,24);
        SimpleDateFormat sdf1 = new SimpleDateFormat("d MMMMM y");
        assertEquals("24 February 2015",sdf1.format(c.getTime()));
        c.add(Calendar.DAY_OF_MONTH,1);
        assertEquals("25 February 2015",sdf1.format(c.getTime()));
    }


    private List<Calendar> createDummyGames(int numberOfGames, Calendar today) {
        List<Calendar> arbitraryList = new ArrayList<Calendar>();
        arbitraryList.add(today);
        for (int i=1;i<numberOfGames;i++){
            Calendar anotherDay = (Calendar)arbitraryList.get(i-1).clone();
            anotherDay.add(Calendar.DAY_OF_MONTH,1);
            arbitraryList.add(anotherDay);
        }
        return arbitraryList;
    }
}
