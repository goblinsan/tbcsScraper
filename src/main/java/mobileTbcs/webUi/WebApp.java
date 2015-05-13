package mobileTbcs.webUi;

import mobileTbcs.core.SportsClub;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebApp {

    static SportsClub sportsClub = new SportsClub();

    public static void main(String[] args) throws Exception
    {
        Server server = new Server(80);

        WebAppContext webContext = new WebAppContext();
        webContext.setResourceBase("../tbcsScraper/src/main/webapp");
        webContext.setContextPath("/");
        webContext.addServlet(new ServletHolder(new DefaultServlet()), "");
        webContext.addServlet(new ServletHolder(new TeamSelectServlet()), "/selectTeam/*");
        webContext.addServlet(new ServletHolder(new SchedulePrintServlet()), "/printSchedule/*");
        webContext.setParentLoaderPriority(true);
        webContext.setAttribute("sportsClub", sportsClub);

        server.setHandler(webContext);

        server.start();
        server.join();
    }

}
