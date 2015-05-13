package mobileTbcs.util;

import mobileTbcs.core.League;

import java.util.ArrayList;
import java.util.List;

public class TeamSearchUtil {

    public List<League> getLeagueBySport(List<League> leaguesToSort,String sportName) {
        List<League> leaguesOfSport = new ArrayList<League>();
        for (League league : leaguesToSort){
            if(league.getSport().equalsIgnoreCase(sportName)){
                leaguesOfSport.add(league);
            }
        }
        return leaguesOfSport;
    }

    public List<League> getLeagueByDay(List<League> leaguesToSort, String dayOfWeek) {
        List<League> leaguesOfDay = new ArrayList<League>();
        for (League league : leaguesToSort){
            if(league.getDay().equalsIgnoreCase(dayOfWeek)){
                leaguesOfDay.add(league);
            }
        }
        return leaguesOfDay;
    }

}
