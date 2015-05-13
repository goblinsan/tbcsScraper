package mobileTbcs;

import mobileTbcs.core.Game;
import mobileTbcs.core.League;
import mobileTbcs.core.Team;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SiteScraper {
    Document targetDoc;
    String baseURL = "http://www.tampabayclubsport.com";
    List<League> leagues;;
    String targetURL;


    public void fetchPage() throws IOException {
        targetDoc = Jsoup.connect(targetURL).get();
    }

    public Document getTargetDoc() {
        return targetDoc;
    }

    public List<League> getLeagues() throws IOException {
        targetURL = baseURL + "/leagues";
        fetchPage();
        List<League> leagues = new ArrayList<League>();
        Elements leagueElements = targetDoc.select("[^data-leagueid]");
        for(Element leagueTag : leagueElements){
            League league = new League();
            league.setName(leagueTag.attr("data-leaguename"));
            league.setId(leagueTag.attr("data-leagueid"));
            league.setDay(leagueTag.attr("data-days"));
            league.setSport(leagueTag.attr("data-sport"));
            leagues.add(league);
        }
        return leagues;
    }

    public List<Team> getTeams(String leagueId) throws IOException {
        targetURL = baseURL +"/league/"+ leagueId;
        fetchPage();
        List<Team> teams = new ArrayList<Team>();
        Elements teamElements = targetDoc.select(".team");
        for (Element e : teamElements){
            teams.addAll(getTeamsInElement(e));
        }
        return teams;
    }

    public List<Game> getSchedule(League league) throws IOException {
        targetURL = baseURL + "/league/" + league.getId() + "/schedule";
        fetchPage();
        List<Game> schedule = new ArrayList<Game>();
        Elements gameDateElements = targetDoc.select(".gameDate");
        for (Element gameDate : gameDateElements){
            schedule.addAll(getGamesOnGameDate(gameDate));
        }
        return schedule;
    }

    private List<Game> getGamesOnGameDate(Element gameDate) {
        List<Game> games = new ArrayList<Game>();
        Elements gamesInRow = gameDate.select("tr.even");
        if(gamesInRow.size()>0){
            games.addAll(getGamesInRows(gamesInRow));
        }
        gamesInRow = gameDate.select("tr.odd");
        games.addAll(getGamesInRows(gamesInRow));

        return games;
    }

    private List<Game> getGamesInRows(Elements gamesInRow) {
        List<Game> games = new ArrayList<Game>();
        for(Element row : gamesInRow){
            Elements gameFieldElement = row.select(".gameField");
            Elements locationLink = gameFieldElement.get(0).select("a[href]");
            String location = locationLink.get(0).text();
            Elements gamesOnDate = row.select("[^data-teamoneid]");
            for (Element gameElement : gamesOnDate){
                Game game = new Game();
                game.setLocation(location);
                String dateToParse = gameElement.attr("id");
                String dateStr = dateToParse.substring(2,12);
                String timeStr = dateToParse.substring(13,21);
                timeStr = timeStr.replace("-",":");
                game.setDateTime(new DateTime(dateStr+"T"+timeStr));
                game.setTeams(getTeamsInElement(gameElement));
                if(game.getTeams().size()>0){
                    games.add(game);
                }
            }
        }
        return games;
    }

    private List<Team> getTeamsInElement(Element sourceElement) {
        List<Team> teams = new ArrayList<Team>();
        Elements teamElements = sourceElement.select("a[href]");
        for(Element team : teamElements){
            Team newTeam = new Team();
            newTeam.setName(team.text());
            String relUrl = team.attr("href");
            newTeam.setUrl(relUrl);
            String[] urlParts = relUrl.split("/");
            newTeam.setId(urlParts[2]);
            teams.add(newTeam);
        }
        return teams;
    }


}
