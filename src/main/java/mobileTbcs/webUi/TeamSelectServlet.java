package mobileTbcs.webUi;

import com.google.gson.Gson;
import mobileTbcs.core.League;
import mobileTbcs.core.SportsClub;
import mobileTbcs.core.Team;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class TeamSelectServlet extends HttpServlet
{
    SportsClub sportsClub;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        sportsClub = (SportsClub) this.getServletContext().getAttribute("sportsClub");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/exampleSelect.html");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String json = new Gson().toJson(getOptions(request));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    private Map<String, String> getOptions(HttpServletRequest request){
        Map<String, String> options = new LinkedHashMap<String, String>();
        String leagueSelect = request.getParameter("LEAGUESELECT");
        if(leagueSelect != null){
            for(Team team : sportsClub.getTeamsByLeague(leagueSelect)){
                options.put(team.getId()+","+team.getName(),team.getName());
            }
        } else {
            String sportSelect = request.getParameter("SPORTSELECT");
            String daySelect = request.getParameter("DAYSELECT");
            for(League league : sportsClub.getLeaguesByIndex(sportSelect,daySelect)){
                options.put(league.getId(), league.getName());
            }
        }
        return options;
    }

}
