package mobileTbcs.webUi;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HtmlPage {
    String header;
    String body;
    String leaguesInClub;
    String selectDropDown;
    String gamesInLeague;
    Cookie[] cookies;

    public HtmlPage(){
        header = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <title>Bootstrap Example</title>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "  <link rel=\"stylesheet\" href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css\">\n" +
                "  <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js\"></script>\n" +
                "  <script src=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js\"></script>\n" +
                "</head>";


        selectDropDown = "<select class=\"form-control\">\n" +
                "    <option value=\"one\">One</option>\n" +
                "    <option value=\"two\">Two</option>\n" +
                "    <option value=\"three\">Three</option>\n" +
                "    <option value=\"four\">Four</option>\n" +
                "    <option value=\"five\">Five</option>\n" +
                "</select>";
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody(HttpServletResponse httpServletResponse) {
        if(teamCookieExists()) {
            body = "<body>\n" +
                    "<div class=\"container\">\n" +
                    "  <div class=\"jumbotron\">\n" +
                    "    <h1>" + leaguesInClub + "</h1>\n" +
                    "    <p>Check your cookies! -> " + printCookies() + "</p> \n" +
                    selectDropDown +
                    "  </div>\n" +
                    "<h2 class=\"sub-header\">Games</h2>\n" +
                    "<div class=\"table-responsive\">" +
                    "<table class=\"table table-striped\">\n" +
                    "               <thead>" +
                    "                <tr><th>date time</th><th>location</th><th>team 1</th><th>team 2</th></tr>" +
                    "               </thead>" +
                    "              <tbody>\n" +
                    gamesInLeague +
                    "              </tbody>\n" +
                    "            </table>\n" +
                    "            </div>" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>";
        } else {
            body = "<body>\n" +
                    "<div class=\"container\">\n" +
                    "    <h1>Select your team</h1>\n" +
                    "    <p>Check your cookies! -> " + printCookies() + "</p> \n" +
                    selectDropDown +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>";
            httpServletResponse.addCookie(new Cookie("TeamId","1234"));
        }

        return body;
    }

    public boolean teamCookieExists(){
        for (Cookie cookie : cookies){
            if(cookie.getName().equalsIgnoreCase("TeamId")) return true;
        }
        return false;
    }

    public String printCookies(){
        String output = "";
        for (Cookie cookie : cookies){
            output += cookie.getName() +":"+ cookie.getValue() +"\r\n";
        }
        return output;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAllGames() {
        return leaguesInClub;
    }

    public void setLeaguesInClub(String leaguesInClub) {
        this.leaguesInClub = leaguesInClub;
    }

    public String getSelectDropDown() {
        return selectDropDown;
    }

    public void setSelectDropDown(String selectDropDown) {
        this.selectDropDown = selectDropDown;
    }

    public void setGames(String gamesPrint) {
        this.gamesInLeague = gamesPrint;
    }

    public Cookie[] getCookies() {
        return cookies;
    }

    public void setCookies(Cookie[] cookies) {
        this.cookies = cookies;
    }
}
