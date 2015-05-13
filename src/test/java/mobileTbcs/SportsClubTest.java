package mobileTbcs;

import mobileTbcs.core.League;
import mobileTbcs.core.LeagueIndex;
import mobileTbcs.core.SportsClub;
import mobileTbcs.core.Team;
import org.joda.time.DateTime;
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
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class SportsClubTest {

    @Mock
    SiteScraper mockSiteScraper;

    @Mock
    League mockLeague;

    SportsClub sc;
    List<League> actualLeagues;

    @Before
    public void setup() throws IOException {
        sc = new SportsClub();
        sc.setSiteScraper(mockSiteScraper);
        List<League> leagues = Arrays.asList(mockLeague,mockLeague,mockLeague);
        Mockito.when(mockSiteScraper.getLeagues()).thenReturn(leagues);
        actualLeagues = sc.getLeagues();
    }

    @Test
    public void clubURLisSet(){
        String expectedURL = "http://www.tampabayclubsport.com";
        assertEquals(expectedURL, sc.getClubUrl());
    }

    @Test
    public void getLeaguesCreatesList() {
        assertEquals(3, actualLeagues.size());
    }

    @Test
    public void printLeagueTest(){
        Mockito.when(mockLeague.toString()).thenReturn("arbitraryString");
        String expectedOutput = "arbitraryString\r\narbitraryString\r\narbitraryString\r\n";
        assertEquals(expectedOutput, sc.printLeagues());
    }

    @Test
    public void getTeamInLeagueTest(){
        Team one = new Team();
        one.setName("one");
        Team spectrum = new Team();
        spectrum.setName("Spectrum FC");
        Team spectrums = new Team();
        spectrums.setName("Spectrums");
        List<Team> teams = Arrays.asList(one, spectrum, spectrums);
        Mockito.when(mockLeague.getTeams()).thenReturn(teams);
        List<Team> returnedTeams = sc.getTeamsByName("pectrum");
        Team firstTeam = returnedTeams.get(0);
        assertEquals(firstTeam.getName(),"Spectrum FC");
        assertEquals(6,returnedTeams.size());
    }

    @Test
    public void getLeaguesBySportTest(){
        League first = new League();
        first.setSport("Soccer");
        first.setName("mondaySoccerLeague");
        first.setDay("Monday");
        League second = new League();
        second.setSport("Soccer");
        second.setDay("Tuesday");
        second.setName("tuesdaySoccerLeague");
        League third = new League();
        third.setSport("Kickball");
        third.setDay("Monday");
        third.setName("mondayKickballLeague");
        List<League> testLeagues = Arrays.asList(first,second,third);
        List<League> expectedMondayLeagues = Arrays.asList(first,third);
        List<League> expectedSoccerLeagues = Arrays.asList(first,second);
        sc.setLeagues(testLeagues);
        List<League> actualMondayLeagues = sc.getLeaguesByIndex("", "Monday");
        assertEquals(expectedMondayLeagues,actualMondayLeagues);
        List<League> actualSoccerLeagues = sc.getLeaguesByIndex("Soccer", "");
        assertEquals(expectedSoccerLeagues,actualSoccerLeagues);
        List<League> actualSoccerNullLeagues = sc.getLeaguesByIndex("Soccer", null);
        assertEquals(expectedSoccerLeagues,actualSoccerNullLeagues);
    }

    @Test
    public void reindexLeagueWhenTooOld(){
        LeagueIndex li = new LeagueIndex();
        DateTime overAnHour = new DateTime().minusHours(1).minusMinutes(1);
        li.setAge(overAnHour);
        assertTrue(sc.indexIsTooOld(li));
    }

    @Test
    public void getTeamIndexByLeague() throws IOException {
        Team team1 = new Team();
        team1.setName("team1");
        Team team2 = new Team();
        team2.setName("team2");
        Team team3 = new Team();
        team3.setName("team3");
        List<Team> expectedTeams = Arrays.asList(team1,team2,team3);
        Mockito.when(mockSiteScraper.getTeams(Mockito.anyString())).thenReturn(expectedTeams);
        List<Team> actualTeams = sc.getTeamsByLeague("arbitraryId");
        assertEquals(expectedTeams,actualTeams);
    }


}
