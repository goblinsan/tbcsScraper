package mobileTbcs.core;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Table;
import mobileTbcs.SiteScraper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SportsClub {
    String clubUrl = "http://www.tampabayclubsport.com";
    List<League> leagues;
    List<Team> allTeams;
    SiteScraper siteScraper;
    Table<String,String,LeagueIndex> leagueIndex;
    BiMap<String, TeamIndex> teamIndex;

    public SportsClub(){
        siteScraper = new SiteScraper();
        leagueIndex = HashBasedTable.create();
        teamIndex = HashBiMap.create();
    }

    public String printLeagues() {
        String output = "";
        for(League league : getLeagues()){
            output += league.toString()+"\r\n";
        }
        return output;
    }

    public String getClubUrl() {
        return clubUrl;
    }

    public void setClubUrl(String clubUrl) {
        this.clubUrl = clubUrl;
    }

    public List<League> getLeagues() {
        if (leagues == null){
            try {
                leagues = siteScraper.getLeagues();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return leagues;
    }

    public void setLeagues(List<League> leagues) {
        this.leagues = leagues;
    }

    public SiteScraper getSiteScraper() {
        return siteScraper;
    }

    public void setSiteScraper(SiteScraper siteScraper) {
        this.siteScraper = siteScraper;
    }

    public List<Team> getTeamsByName(final String teamSearchTerm) {
        List<Team> teamsByName = getAllTeams();
        CollectionUtils.filter(teamsByName, new Predicate() {
            @Override
            public boolean evaluate(Object team) {
                String regEx1 = ".*"+teamSearchTerm.toLowerCase()+".*.";
                String regEx2 = ".*."+teamSearchTerm.toLowerCase()+".*.";
                String lowerTeamName = ((Team)team).getName().toLowerCase();
                return lowerTeamName.matches(regEx1) || lowerTeamName.matches(regEx2);
            }
        });

        return teamsByName;
    }

    public List<Team> getAllTeams() {
        if(allTeams == null){
            allTeams = new ArrayList<Team>();
            for(League league : getLeagues()){
                allTeams.addAll(league.getTeams());
            }
        }
        return allTeams;
    }

    public List<League> getLeaguesByIndex(String sport, String dayOfWeek) {
        LeagueIndex filteredLeagues = leagueIndex.get(sport, dayOfWeek);
        if(filteredLeagues == null || indexIsTooOld(filteredLeagues)){
            return buildAndAddIndex(sport,dayOfWeek);
        }
        return filteredLeagues.getLeagues();
    }

    public boolean indexIsTooOld(Indexed filteredLeagues) {
        DateTime currentTime = new DateTime();
        return (filteredLeagues.getAge().getMillis() < currentTime.minusHours(1).getMillis());
    }

    private List<League> buildAndAddIndex(String sport, String dayOfWeek) {
        if(null==sport){sport="";}
        if(null==dayOfWeek){dayOfWeek="";}
        List<League> filteredLeagues = new ArrayList<League>();
        boolean matchedSport = false;
        boolean matchedDay = false;
        for (League league : getLeagues()){
            matchedSport = (sport.equals("") || league.getSport().equalsIgnoreCase(sport));
            matchedDay = (dayOfWeek.equals("") || league.getDay().contains(dayOfWeek));
            if(matchedSport && matchedDay){
                filteredLeagues.add(league);
            }
        }
        addToLeageIndex(sport,dayOfWeek,filteredLeagues);
        return filteredLeagues;
    }

    public void addToLeageIndex(String sport, String dayOfWeek, List<League> leagues) {
        LeagueIndex updatedLeageIndex = new LeagueIndex();
        updatedLeageIndex.setLeagues(leagues);
        leagueIndex.put(sport,dayOfWeek,updatedLeageIndex);
    }

    public List<Team> getTeamsByLeague(String leagueId) {
        TeamIndex filteredTeams = teamIndex.get(leagueId);
        if(filteredTeams == null || indexIsTooOld(filteredTeams)){
            return buildAndAddTeamIndex(leagueId);
        }
        return filteredTeams.getTeams();
    }

    private List<Team> buildAndAddTeamIndex(String leagueId) {
        if(null==leagueId){leagueId="";}
        List<Team> filteredTeams = new ArrayList<Team>();
        try {
            filteredTeams.addAll(siteScraper.getTeams(leagueId));
            TeamIndex updatedTeamIndex = new TeamIndex();
            updatedTeamIndex.setTeams(filteredTeams);
            teamIndex.put(leagueId,updatedTeamIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filteredTeams;
    }
}
