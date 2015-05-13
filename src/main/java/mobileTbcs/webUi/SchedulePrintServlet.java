package mobileTbcs.webUi;

import mobileTbcs.core.Game;
import mobileTbcs.core.SportsClub;
import mobileTbcs.core.Team;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SchedulePrintServlet extends HttpServlet
{
    SportsClub sportsClub;
    String teamId;
    String teamName;

    /*
        File input = new File("/exampleSelect.html");
        Document doc = Jsoup.parse(input, "UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(document.html());
     */

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String[] selectedTeam = request.getParameter("TEAMSELECT").split(",");
        String selectedTeamId = selectedTeam[0];
        String selectedTeamName = selectedTeam[1];
        response.addCookie(new Cookie("TeamId",selectedTeamId));
        response.addCookie(new Cookie("TeamName",selectedTeamName));
        response.sendRedirect("/printSchedule");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Cookie[] cookies = request.getCookies();
        if(teamCookieExists(request)) {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            sportsClub = (SportsClub) this.getServletContext().getAttribute("sportsClub");
            response.getWriter().println(getHead()+getBody());
        } else {
            response.sendRedirect("/selectTeam");
        }
        //response.getWriter().println("session=" + request.getSession(true).getId());
    }

    private boolean teamCookieExists(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        boolean foundCookie = false;
        for (Cookie cookie : cookies){
            if(cookie.getName().equalsIgnoreCase("TeamId")){
                teamId = cookie.getValue();
                foundCookie = true;
            }
            if(cookie.getName().equalsIgnoreCase("TeamName")){
                teamName = cookie.getValue();
                foundCookie = true;
            }
        }
        return foundCookie;
    }

    String getHead(){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <title>Mobile TBCS - "+teamName+"</title>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "  <link rel=\"stylesheet\" href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css\">\n" +
                "  <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js\"></script>\n" +
                "  <script src=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js\"></script>\n" +
                "</head>";
    }



    String getBody(){
        String body = "<body>\n" +
                "<nav class=\"navbar navbar-inverse navbar-fixed-top\">\n" +
                "    <div class=\"container-fluid\">\n" +
                "        <div class=\"navbar-header\">\n" +
                "            <div class=\"navbar-brand\">Mobile TBCS - "+teamName +"</div>\n" +
                "               <form class=\"navbar-form navbar-left\" action=\"/selectTeam\" role=\"search\">\n" +
                "                 <button type=\"submit\" class=\"btn btn-primary\">Change Team</button>\n" +
                "               </form>"+
                "        </div>\n" +
                "    </div>\n" +
                "</nav>\n" +
                "<div class=\"container\" style=\"margin-top:60px\">" +
                "<h2 class=\"sub-header\">Games</h2>\n" +
                "<div class=\"table-responsive\">" +
                "<table class=\"table table-striped\">\n" +
                "               <thead>" +
                "                <tr><th>Date : Time</th><th>Opponent</th><th>Location</th></tr>" +
                "               </thead>" +
                "              <tbody>\n" +
                makeGameRows() +
                "              </tbody>\n" +
                "            </table>\n" +
                "            </div>" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
        return body;
    }

    public String makeGameRows() {
        String output = "";
        List<Game> games = new ArrayList<Game>();
        try {
            games = getGamesFromTeamPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Game g : games){
            output += printScheduleRow(g);
        }
        return output;
    }

    public List<Game> getGamesFromTeamPage() throws IOException {
        Document doc = Jsoup.connect("http://www.tampabayclubsport.com/team/"+teamId).get();
        Element teamScheduleTable = doc.select("table#teamScheduleTable").first();
        Elements gameDateRows = teamScheduleTable.select("tr.gameDateRow");
        List<Game> schedule = new ArrayList<Game>();
        for(Element row : gameDateRows){
            Game game = parseGameRow(row);
            if (game != null) schedule.add(game);
        }
        return schedule;
    }

    private Game parseGameRow(Element row) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
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
        opponent.setName(opponentUrl.text());
        opponent.setUrl(opponentElement.attr("abs:href"));
        Element opponentIcon = nextSib.select("div.teamColorSwatch_Large").first();
        if(opponentIcon != null){
            opponent.setColor(opponentIcon.attr("style").replace("background:",""));
        }
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

    String printScheduleRow(Game g){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MMM d : k:m");
        String output = "<tr>\n" +
        "<td>"+ fmt.print(g.getDateTime()) +"</td>\n";
        if(g.getOpponent() != null) {
            output += "<td>" + g.getOpponent().getName() + "</td>\n";
        }
        output += "<td>"+ g.getLocation() +"</td>\n</tr>\n";
        return output;
    }

}
