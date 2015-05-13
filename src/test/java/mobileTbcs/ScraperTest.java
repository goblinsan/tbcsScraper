package mobileTbcs;


import mobileTbcs.core.Game;
import mobileTbcs.core.Team;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ScraperTest {

    int year = Calendar.getInstance().get(Calendar.YEAR);

    @Ignore
    @Test
    public void getLeagues() throws IOException {
        Document doc = Jsoup.connect("http://www.tampabayclubsport.com/league").get();
        Elements leagues = doc.select("[^data-leagueid]");
        for (Element league : leagues){
            System.out.println(league.attr("data-leaguename"));
            System.out.println(" - Sport : " + league.attr("data-sport"));
            System.out.println(" - League ID : " + league.attr("data-leagueid"));
            System.out.println(" - Day : " + league.attr("data-days"));
        }

    }

    @Ignore
    @Test
    public void getTeamsInLeague() throws IOException{
        Document doc = Jsoup.connect("http://www.tampabayclubsport.com/league/8258").get();
        Elements teams = doc.select(".team");
        for (Element team : teams){
            Elements teamEle = team.select("a[href]");
            System.out.println("Team Name: " + teamEle.first().text());
            System.out.println("Team URL: " + teamEle.first().attr("abs:href"));
            String relUrl = teamEle.first().attr("href");
            String[] urlParts = relUrl.split("/");
            String teamId = urlParts[2];
            System.out.println("Team ID: " + teamId);
        }
    }

    @Ignore
    @Test
    public void selectGames() throws IOException {
        Document doc = Jsoup.connect("http://www.tampabayclubsport.com/league/8258/schedule").get();
        Elements gameDates = doc.select("[^data-date]");
        Elements games = gameDates.select("div.gameCellContent");
        for (Element gameDate : gameDates ){
            System.out.println(gameDate.attr("data-date"));
            Elements gameCell = gameDate.select("div.gameCellContent");
            //Element cellParent = gameCell.outerHtml();
            Elements teams = gameCell.select("a[href]");
            System.out.println(teams);
        }
        //System.out.println(games);
    }

    @Test
    public void getGamesFromTeamPage() throws IOException {
        Document doc = Jsoup.connect("http://www.tampabayclubsport.com/team/85021").get();
        Element teamScheduleTable = doc.select("table#teamScheduleTable").first();
        Elements gameDateRows = teamScheduleTable.select("tr.gameDateRow");
        assertNotNull(teamScheduleTable);
        assertEquals(10,gameDateRows.size());
        List<Game> schedule = new ArrayList<Game>();
        for(Element row : gameDateRows){
            Game game = parseGameRow(row);
            if (game != null) schedule.add(game);
        }
        assertEquals(7, schedule.size());
        assertEquals("Sensi Athletico (r)", schedule.get(0).getOpponent().getName());
        assertEquals("The Long Center Long Center 2", schedule.get(0).getLocation());
    }

    private Game parseGameRow(Element row) {
        Game game = new Game();
        Element gameDate = row.select("td.gameDate").first();
        Element nextSib = row.nextElementSibling();
        nextSib = skipBannerRow(nextSib);
        if(gameDateMissingGameTime(nextSib)){
            return null;
        }
        String gameTime = nextSib.select("td.gameTime").first().text();
        game.setDateTime(parseGameDateTimeElements(year,gameDate,gameTime));
        game.setLocation(nextSib.select("td.gameLocation").first().text());
        game.setOpponent(parseOpponentData(nextSib));
        return game;
    }

    private Team parseOpponentData(Element nextSib) {
        Team opponent = new Team();
        Element opponentElement = nextSib.select("td.opponent").first();
        Element opponentUrl = opponentElement.select("a[href]").first();
        Element opponentIcon = nextSib.select("div.teamColorSwatch_Large").first();
        opponent.setName(opponentUrl.text());
        opponent.setUrl(opponentElement.attr("abs:href"));
        opponent.setColor(opponentIcon.attr("style").replace("background:",""));

        return opponent;
    }

    private boolean gameDateMissingGameTime(Element nextSib) {
        return (nextSib.select("td.gameTime").first() == null);
    }

    private DateTime parseGameDateTimeElements(int year, Element gameDate, String gameTime) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("EEEE, MMMMM d y h:m a");
        return formatter.parseDateTime(gameDate.children().first().text()+" "+year+" "+gameTime);
    }

    private Element skipBannerRow(Element nextSib) {
        if(nextSib.className().contains("scheduleBannerRow")){
            nextSib = nextSib.nextElementSibling();
        }
        return nextSib;
    }


}
