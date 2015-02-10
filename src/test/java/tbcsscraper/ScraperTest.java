package tbcsscraper;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

public class ScraperTest {

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


}
