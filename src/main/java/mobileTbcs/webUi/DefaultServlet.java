package mobileTbcs.webUi;

import mobileTbcs.core.SportsClub;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DefaultServlet extends HttpServlet
{
    SportsClub sportsClub;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Cookie[] cookies = request.getCookies();
        if(teamCookieExists(request)) {
            response.sendRedirect("/printSchedule/");
        } else {
            response.sendRedirect("/selectTeam/");
        }
    }

    private boolean teamCookieExists(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies){
            if(cookie.getName().equalsIgnoreCase("TeamId")) return true;
        }
        return false;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        System.out.println("doing some posting!");
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println("what what???");

        String DATA = request.getParameter("DATA");

        if(DATA != null){
            out.println(DATA);
        } else {
            out.println("No text entered.");
        }
        out.close();
    }

}
