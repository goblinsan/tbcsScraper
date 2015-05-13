package mobileTbcs.util;

import mobileTbcs.core.League;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TeamSearchUtilTest {

    @Mock
    League mockLeague;

    @Mock
    League notInSortCriteriaLeague;

    TeamSearchUtil teamSearchUtil;
    List<League> leaguesToSort;

    @Before
    public void setup(){
        teamSearchUtil = new TeamSearchUtil();
        leaguesToSort = Arrays.asList(mockLeague,mockLeague, notInSortCriteriaLeague);
    }

    @Test
    public void getLeagueBySportTest(){
        Mockito.when(mockLeague.getSport()).thenReturn("Soccer");
        Mockito.when(notInSortCriteriaLeague.getSport()).thenReturn("Football");
        List<League> soccerLeagues = teamSearchUtil.getLeagueBySport(leaguesToSort,"Soccer");
        assertEquals(2,soccerLeagues.size());
    }

    @Test
    public void getLeaguesByDayTest(){
        Mockito.when(mockLeague.getDay()).thenReturn("Monday");
        Mockito.when(notInSortCriteriaLeague.getDay()).thenReturn("Tuesday");
        List<League> soccerLeagues = teamSearchUtil.getLeagueByDay(leaguesToSort, "Monday");
        assertEquals(2,soccerLeagues.size());
    }


}
