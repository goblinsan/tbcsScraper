package mobileTbcs;

import mobileTbcs.core.League;
import mobileTbcs.core.SportsClub;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class SourcePageIT {

    SportsClub sc;
    SiteScraper siteScraper;

    @Before
    public void setup() throws IOException {
        sc = new SportsClub();
        siteScraper = new SiteScraper();
    }

    @Test
    public void connectionTest(){
        try {
            SiteScraper siteScraper = new SiteScraper();
            siteScraper.targetURL = sc.getClubUrl();
            siteScraper.fetchPage();
            assertTrue(true);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void checkLeaguesArePopulated() throws IOException {
        List<League> leagues = siteScraper.getLeagues();
        assertEquals(1,leagues.size());
    }

    @Test
    public void checkGamesArePopulated(){

    }
}
