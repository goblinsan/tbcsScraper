package mobileTbcs.core;

import mobileTbcs.SiteScraper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LeagueTest {
    @Mock
    Game mockGame;
    @Mock
    SiteScraper mockSiteScraper;

    League league;

    @Before
    public void setup(){
        league = new League();
    }

    @Test
    public void testToString() throws Exception {
        String testString = "arbitraryString";
        league.setId(testString);
        league.setName(testString);
        String expectedOutput = testString;
        assertEquals(expectedOutput, league.toString());
    }

    @Test
    public void testGetSchedule() throws IOException {
        List<Game> expectedSchedule = Arrays.asList(mockGame,mockGame);
        league.setSiteScraper(mockSiteScraper);
        Mockito.when(mockSiteScraper.getSchedule(Mockito.any(League.class))).thenReturn(expectedSchedule);
        assertEquals(expectedSchedule, league.getSchedule());
    }
}