package mobileTbcs;

import mobileTbcs.core.Game;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class JodaTests {
    @Test
    public void simpleParseTest(){
        DateTime dateTime = new DateTime("2015-02-25");
        DateTime nextDay = new DateTime("2015-02-26");
        assertTrue(dateTime.getDayOfYear() < nextDay.getDayOfYear());
        assertEquals(nextDay, dateTime.plusDays(1));
    }

    @Test
    public void timeSubtraction(){
        DateTime dt = new DateTime();
        assertTrue(dt.minusHours(1).getMillis()< new DateTime().getMillis());
    }

    @Test
    public void createFromString(){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEEE, MMMMM d y h:m a");
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String dateString = "Monday, April 13 "+year+" 6:30 PM";
        DateTime dt = formatter.parseDateTime(dateString);
        //DateTime dt = new DateTime("2015-02-26");
        assertEquals(dateString,formatter.print(dt));
    }
    @Test
    public void substringTest() {
        String testString = "d_2015-01-05_18-30-00_1259_1";
        String dateStr = testString.substring(2, 12);
        String timeStr = testString.substring(13, 21);
        timeStr = timeStr.replace("-", ":");
        DateTime testDateTime = new DateTime(dateStr + "T" + timeStr);
        assertEquals("2015-01-05T18:30:00.000-05:00", testDateTime.toString());
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MMM d : k:m");
        assertEquals("Jan 5 : 18:30", fmt.print(testDateTime));
    }

    @Test
    public void compareGameTest(){
        Game g1 = new Game();
        Game g2 = new Game();
        g1.setDateTime(new DateTime("2015-02-26"));
        g2.setDateTime(new DateTime("2015-01-22"));
        List<Game> games = Arrays.asList(g1,g2);
        assertEquals(g1, games.get(0));
        Collections.sort(games);
        assertEquals(g2,games.get(0));
    }
}
