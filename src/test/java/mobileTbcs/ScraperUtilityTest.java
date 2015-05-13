package mobileTbcs;

import mobileTbcs.core.Game;
import mobileTbcs.core.League;
import mobileTbcs.core.Team;
import org.joda.time.DateTime;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Jsoup.class)
public class ScraperUtilityTest {
    static String mockStringPageResult;
    static Document mockDoc;
    SiteScraper siteScraper;
    String arbitraryURL = "arbitraryURL";


    @BeforeClass
    public static void init() throws IOException {
        setMockStringPageResult();
        parseMockStringToDoc();
    }

    @Before
    public void setup() throws IOException {
        PowerMockito.mockStatic(Jsoup.class);
        Connection mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockConnection.get()).thenReturn(mockDoc);
        Mockito.when(Jsoup.connect(Mockito.anyString())).thenReturn(mockConnection);
        siteScraper = new SiteScraper();
    }

    @Test
    public void fetchPageTest() throws IOException {
        siteScraper.fetchPage();
        assertEquals(mockDoc, siteScraper.getTargetDoc());
    }

    @Test
    public void getLeaguesTest() throws IOException {
        List<League> leagues = siteScraper.getLeagues();
        assertEquals(2,leagues.size());
        assertEquals("Monday Coed Kickball / Northshore (Winter 2015)",leagues.get(0).getName());
        assertEquals("8638",leagues.get(0).getId());
        assertEquals("Monday",leagues.get(0).getDay());
        assertEquals("Kickball", leagues.get(0).getSport());
        //assertEquals(9,leagues.get(0).getTeams().size());
        //assertEquals("77966",leagues.get(0).getTeams().get(8).getId());
        //assertEquals(4,leagues.get(0).getSchedule().size());
        //assertEquals(2,leagues.get(0).getSchedule().get(0).getTeams().size());
    }

    @Test
    public void getTeamsTest() throws IOException {
        List<Team> actualTeams;
        League testLeague = new League();
        testLeague.setId("8207");
        actualTeams = siteScraper.getTeams(testLeague.getId());
        assertEquals("http://www.tampabayclubsport.com/league/8207",siteScraper.targetURL);
        assertEquals(9,actualTeams.size());
        assertEquals("One Kick Wonders",actualTeams.get(8).getName());
        String expectedUrl = "/team/77966/One-Kick-Wonders";
        assertEquals(expectedUrl,actualTeams.get(8).getUrl());
        assertEquals("77966",actualTeams.get(8).getId());
        assertEquals("Rolling Stones",actualTeams.get(3).getName());
        expectedUrl = "/team/76835/Rolling-Stones";
        assertEquals(expectedUrl,actualTeams.get(3).getUrl());
        assertEquals("76835",actualTeams.get(3).getId());
    }

    @Test
    public void getSchedulesTest () throws IOException {
        List<Game> actualSchedule;
        League testLeague = new League();
        testLeague.setId("8207");
        actualSchedule = siteScraper.getSchedule(testLeague);
        assertEquals(4, actualSchedule.size());
        DateTime expectedDate = new DateTime("2015-01-05T22:30:00");
        assertEquals(expectedDate, actualSchedule.get(1).getDateTime());
        assertEquals(2, actualSchedule.get(0).getTeams().size());
        assertEquals("North Shore Park", actualSchedule.get(0).getLocation());
    }

    private static void parseMockStringToDoc(){
        mockDoc = Jsoup.parse(mockStringPageResult);
    }

    private static void setMockStringPageResult() {
        mockStringPageResult = "<div class=\"league-listing current closed\" id=\"league_8207\" data-leagueid=\"8638\" data-leaguename=\"Monday Coed Kickball / Northshore (Winter 2015)\" data-sport=\"Kickball\" data-locationids=\"1259\" data-neighborhoodids=\"\" data-days=\"Monday\">\n" +
                "\t<h4 class=\"league-name\">\n" +
                "\t\tMonday Coed Kickball / Northshore (Winter 2015)\n" +
                "\t</h4>\n" +
                "\t<a href=\"/league/8207/details\" class=\"btn-sm learn-more-button\">\n" +
                "\t\t<img src=\"/static/1.74.0/graphics/icon-learn-more-button.png\" class=\"learn-more-button-icon ll-icon\"> Learn More\n" +
                "\t</a>\n" +
                "\t\n" +
                "\t\n" +
                "\t\n" +
                "\t<ul class=\"league-links\">\n" +
                "\t\t<li>\n" +
                "\t\t\t<a href=\"/league/8207\">\n" +
                "\t\t\t\t<span class=\"ll-icon icon-home\"></span>Home\n" +
                "\t\t\t</a>\n" +
                "\t\t</li>\n" +
                "\t\t<li class=\"active\">\n" +
                "\t\t\t<a href=\"/league/8207/details\">\n" +
                "\t\t\t\t<span class=\"ll-icon icon-info\"></span>Info\n" +
                "\t\t\t</a>\n" +
                "\t\t</li>\n" +
                "\t\t<li>\n" +
                "\t\t\t<a href=\"/league/8207/schedule\">\n" +
                "\t\t\t\t<span class=\"ll-icon icon-schedule\"></span>Schedule\n" +
                "\t\t\t</a>\n" +
                "\t\t</li>\n" +
                "\t\t<li>\n" +
                "\t\t\t<a href=\"/league/8207/standings\">\n" +
                "\t\t\t\t<span class=\"ll-icon icon-standings\"></span>Standings\n" +
                "\t\t\t</a>\n" +
                "\t\t</li>\n" +
                "\t\t<li>\n" +
                "\t\t\t<a href=\"/league/8207/bracket\">\n" +
                "\t\t\t\t<span class=\"ll-icon icon-bracket\"></span>Playoffs\n" +
                "\t\t\t</a>\n" +
                "\t\t</li>\n" +
                "\t\t\n" +
                "\t\t\n" +
                "\t</ul>\n" +
                "\n" +
                "\n" +
                "\t<div class=\"left\">\n" +
                "\t\t<ul class=\"league-summary\">\n" +
                "\t\t\t\n" +
                "\t<li class=\"sport\">\n" +
                "\t\t<span class=\"label\">Sport:</span>\n" +
                "\t\t<img src=\"/static/1.74.0/icons/traditional/kickball-tiny.png\" alt=\"\">\n" +
                "\t\tKickball\n" +
                "\t</li>\n" +
                "\t\n" +
                "\t<li class=\"locations\">\n" +
                "\t\t<span class=\"label\">Locations:</span>\n" +
                "\t\t<span class=\"ll-icon icon-location\"></span>\n" +
                "\t\t\n" +
                "\t\n" +
                "\t\t<a href=\"/location/1259\">\n" +
                "\t\t\tNorth Shore Park</a>\n" +
                "\t\n" +
                "\t\n" +
                "\n" +
                "\t</li>\n" +
                "\t<li class=\"days\">\n" +
                "\t\t<span class=\"label\">Days of the week:</span>\n" +
                "\t\t<span class=\"ll-icon icon-day\"></span>\n" +
                "\t\tMonday\n" +
                "\t</li>\n" +
                "\t<li class=\"start\">\n" +
                "\t\t<span class=\"label\">Started on :</span>\n" +
                "\t\t\n" +
                "\t\t<span class=\"ll-icon icon-in-progress\"></span>\n" +
                "\t\tStarted on \n" +
                "\t\t1/5\n" +
                "\t</li>\n" +
                "\n" +
                "\t\t</ul>\n" +
                "\t</div>\n" +
                "\t\n" +
                "\t<div class=\"clear\"></div>\n" +
                "</div>\n" +
                "<div class=\"league-listing current closed\" id=\"league_8196\" data-leagueid=\"8638\" data-leaguename=\"Monday Men's Soccer / Shimberg (Winter 2015)\" data-sport=\"Soccer\" data-locationids=\"1252,1253\" data-neighborhoodids=\",\" data-days=\"Monday\">\n" +
                "\t<h4 class=\"league-name\">\n" +
                "\t\tMonday Men's Soccer / Shimberg (Winter 2015)\n" +
                "\t</h4>\n" +
                "\t<a href=\"/league/8196/details\" class=\"btn-sm learn-more-button\">\n" +
                "\t\t<img src=\"/static/1.74.0/graphics/icon-learn-more-button.png\" class=\"learn-more-button-icon ll-icon\"> Learn More\n" +
                "\t</a>\n" +
                "\t\n" +
                "\t\n" +
                "\t\n" +
                "\t<ul class=\"league-links\">\n" +
                "\t\t<li>\n" +
                "\t\t\t<a href=\"/league/8196\">\n" +
                "\t\t\t\t<span class=\"ll-icon icon-home\"></span>Home\n" +
                "\t\t\t</a>\n" +
                "\t\t</li>\n" +
                "\t\t<li class=\"active\">\n" +
                "\t\t\t<a href=\"/league/8196/details\">\n" +
                "\t\t\t\t<span class=\"ll-icon icon-info\"></span>Info\n" +
                "\t\t\t</a>\n" +
                "\t\t</li>\n" +
                "\t\t<li>\n" +
                "\t\t\t<a href=\"/league/8196/schedule\">\n" +
                "\t\t\t\t<span class=\"ll-icon icon-schedule\"></span>Schedule\n" +
                "\t\t\t</a>\n" +
                "\t\t</li>\n" +
                "\t\t<li>\n" +
                "\t\t\t<a href=\"/league/8196/standings\">\n" +
                "\t\t\t\t<span class=\"ll-icon icon-standings\"></span>Standings\n" +
                "\t\t\t</a>\n" +
                "\t\t</li>\n" +
                "\t\t<li>\n" +
                "\t\t\t<a href=\"/league/8196/bracket\">\n" +
                "\t\t\t\t<span class=\"ll-icon icon-bracket\"></span>Playoffs\n" +
                "\t\t\t</a>\n" +
                "\t\t</li>\n" +
                "\t\t\n" +
                "\t\t\n" +
                "\t</ul>\n" +
                "\n" +
                "\n" +
                "\t<div class=\"left\">\n" +
                "\t\t<ul class=\"league-summary\">\n" +
                "\t\t\t\n" +
                "\t<li class=\"sport\">\n" +
                "\t\t<span class=\"label\">Sport:</span>\n" +
                "\t\t<img src=\"/static/1.74.0/icons/traditional/soccer-tiny.png\" alt=\"\">\n" +
                "\t\tSoccer\n" +
                "\t</li>\n" +
                "\t\n" +
                "\t<li class=\"locations\">\n" +
                "\t\t<span class=\"label\">Locations:</span>\n" +
                "\t\t<span class=\"ll-icon icon-location\"></span>\n" +
                "\t\t\n" +
                "\t\n" +
                "\t\t<a href=\"/location/1252\">\n" +
                "\t\t\tShimberg Soccer Complex</a>,\n" +
                "\t\n" +
                "\t\t<a href=\"/location/1253\">\n" +
                "\t\t\tSkyway Park</a>\n" +
                "\t\n" +
                "\t\n" +
                "\n" +
                "\t</li>\n" +
                "\t<li class=\"days\">\n" +
                "\t\t<span class=\"label\">Days of the week:</span>\n" +
                "\t\t<span class=\"ll-icon icon-day\"></span>\n" +
                "\t\tMonday\n" +
                "\t</li>\n" +
                "\t<li class=\"start\">\n" +
                "\t\t<span class=\"label\">Started on :</span>\n" +
                "\t\t\n" +
                "\t\t<span class=\"ll-icon icon-in-progress\"></span>\n" +
                "\t\tStarted on \n" +
                "\t\t1/5\n" +
                "\t</li>\n" +
                "\n" +
                "\t\t</ul>\n" +
                "\t</div>\n" +
                "\t\n" +
                "\t<div class=\"clear\"></div>\n" +
                "</div>\n" +
                "<div class=\"teamProfiles\">\n" +
                "\t<h3><img src=\"/static/1.74.0/graphics/icon-team.png\"> Team Profiles</h3>\n" +
                "\t\n" +
                "\t<ul>\n" +
                "\t\t<li class=\"team\">\n" +
                "\t\t\t<div class=\"teamThumbnail\">\n" +
                "\t\n" +
                "\t\n" +
                "\t\t<div class=\"teamColorStripe\" style=\"background:#ffffff\">&nbsp;</div>\n" +
                "\t\t<img class=\"teamIcon\" src=\"//leaguelab-prod.s3.amazonaws.com/userimages/095663_tny.jpg\" alt=\"Going Yard Team Logo\">\n" +
                "\t\n" +
                "\t\n" +
                "</div>\n" +
                "\t\t\t<div class=\"teamName\"><a href=\"/team/76832/Going-Yard\">Going Yard</a></div>\n" +
                "\t\t\t<div class=\"divisionName\">Open</div>\n" +
                "\t\t</li><li class=\"team\">\n" +
                "\t\t\t<div class=\"teamThumbnail\">\n" +
                "\t\n" +
                "\t\n" +
                "\t<div class=\"teamColorSwatch_Large\" style=\"background:#bbbbbb\">&nbsp;</div>\n" +
                "</div>\n" +
                "\t\t\t<div class=\"teamName\"><a href=\"/team/76833/Jedi-Mind-Kicks\">Jedi Mind Kicks</a></div>\n" +
                "\t\t\t<div class=\"divisionName\">Open</div>\n" +
                "\t\t</li><li class=\"team\">\n" +
                "\t\t\t<div class=\"teamThumbnail\">\n" +
                "\t\n" +
                "\t\n" +
                "\t<div class=\"teamColorSwatch_Large\" style=\"background:#f6f923\">&nbsp;</div>\n" +
                "</div>\n" +
                "\t\t\t<div class=\"teamName\"><a href=\"/team/76834/Alchobalics\">Alchobalics</a></div>\n" +
                "\t\t\t<div class=\"divisionName\">Open</div>\n" +
                "\t\t</li><li class=\"team\">\n" +
                "\t\t\t<div class=\"teamThumbnail\">\n" +
                "\t\n" +
                "\t\n" +
                "\t<div class=\"teamColorSwatch_Large\" style=\"background:#ec6cf8\">&nbsp;</div>\n" +
                "</div>\n" +
                "\t\t\t<div class=\"teamName\"><a href=\"/team/76835/Rolling-Stones\">Rolling Stones</a></div>\n" +
                "\t\t\t<div class=\"divisionName\">Open</div>\n" +
                "\t\t</li><li class=\"team\">\n" +
                "\t\t\t<div class=\"teamThumbnail\">\n" +
                "\t\n" +
                "\t\n" +
                "\t<div class=\"teamColorSwatch_Large\" style=\"background:#308d16\">&nbsp;</div>\n" +
                "</div>\n" +
                "\t\t\t<div class=\"teamName\"><a href=\"/team/76856/Toes-of-Thunder\">Toes of Thunder</a></div>\n" +
                "\t\t\t<div class=\"divisionName\">Open</div>\n" +
                "\t\t</li><li class=\"team\">\n" +
                "\t\t\t<div class=\"teamThumbnail\">\n" +
                "\t\n" +
                "\t\n" +
                "\t<div class=\"teamColorSwatch_Large\" style=\"background:#FFA500\">&nbsp;</div>\n" +
                "</div>\n" +
                "\t\t\t<div class=\"teamName\"><a href=\"/team/76872/Free-Ballers\">Free Ballers</a></div>\n" +
                "\t\t\t<div class=\"divisionName\">Open</div>\n" +
                "\t\t</li><li class=\"team\">\n" +
                "\t\t\t<div class=\"teamThumbnail\">\n" +
                "\t\n" +
                "\t\n" +
                "\t<div class=\"teamColorSwatch_Large\" style=\"background:#000000\">&nbsp;</div>\n" +
                "</div>\n" +
                "\t\t\t<div class=\"teamName\"><a href=\"/team/76942/Always-Sliding-In\">Always Sliding In</a></div>\n" +
                "\t\t\t<div class=\"divisionName\">Open</div>\n" +
                "\t\t</li><li class=\"team\">\n" +
                "\t\t\t<div class=\"teamThumbnail\">\n" +
                "\t\n" +
                "\t\n" +
                "\t<div class=\"teamColorSwatch_Large\" style=\"background:#800000\">&nbsp;</div>\n" +
                "</div>\n" +
                "\t\t\t<div class=\"teamName\"><a href=\"/team/77758/St.-Pete-Young-Professionals-\">St. Pete Young Professionals </a></div>\n" +
                "\t\t\t<div class=\"divisionName\">Open</div>\n" +
                "\t\t</li><li class=\"team\">\n" +
                "\t\t\t<div class=\"teamThumbnail\">\n" +
                "\t\n" +
                "\t\n" +
                "\t<div class=\"teamColorSwatch_Large\" style=\"background:#09b9f6\">&nbsp;</div>\n" +
                "</div>\n" +
                "\t\t\t<div class=\"teamName\"><a href=\"/team/77966/One-Kick-Wonders\">One Kick Wonders</a></div>\n" +
                "\t\t\t<div class=\"divisionName\">Open</div>\n" +
                "\t\t</li>\n" +
                "\t</ul>\n" +
                "</div> \n" +
                "<div class=\"gameDate even Game public\" data-date=\"2015-01-05\">\n" +
                "\t<input type=\"hidden\" name=\"LeagueID\" value=\"8207\" class=\"gameDateHiddenLeagueIDInput\">\n" +
                "\t<h2>\n" +
                "\t\t<a name=\"01_05_15\">Monday, January 5</a>\n" +
                "\t</h2>\n" +
                "\t<div class=\"scheduleBannerContainer\" data-schedulebannerid=\"320\" data-schedulebannerdateid=\"19748\">\n" +
                "\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t<img src=\"//leaguelab-prod.s3.amazonaws.com/userimages/assorted/banner_320-1_std.jpg\" alt=\"First Week - Shirts\" class=\"scheduleBanner\">\n" +
                "\t\t\n" +
                "\t\t\n" +
                "\t</div>\n" +
                "\t\n" +
                "\t\n" +
                "\t\n" +
                "\t\n" +
                "\n" +
                "\t<div class=\"leagueDateNotes\">\n" +
                "\t\t\n" +
                "\n" +
                "\t\t\t\t\n" +
                "\t</div>\n" +
                "\t\n" +
                "\t\n" +
                "\t\t\n" +
                "\t\t\n" +
                "\t\n" +
                "\n" +
                "\t\n" +
                "\t\t<div class=\"byes\" id=\"d_2015-01-05_byes\">\n" +
                "\t\t\t<h6>Byes </h6>\n" +
                "\t\t\tRolling Stones\n" +
                "\t\t\t\n" +
                "\t\t</div>\n" +
                "\t\t\n" +
                "\t\n" +
                "\t\n" +
                "\t<div class=\"leagueDateContent\">\n" +
                "\t\t\n" +
                "\t\t\t\t\n" +
                "\t\t\n" +
                "\t</div>\n" +
                "\t\n" +
                "\t\n" +
                "\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t<table class=\"scheduleTable flippedScheduleTable\" cellspacing=\"0\">\n" +
                "\t<thead>\n" +
                "\t\t<tr>\n" +
                "\t\t\t<th>&nbsp;</th>\n" +
                "\t\t\t<th class=\"gameTime\">\n" +
                "\t\t\t\t6:30 <span>PM</span>\n" +
                "\t\t\t</th><th class=\"gameTime\">\n" +
                "\t\t\t\t7:30 <span>PM</span>\n" +
                "\t\t\t</th><th class=\"gameTime\">\n" +
                "\t\t\t\t8:30 <span>PM</span>\n" +
                "\t\t\t</th><th class=\"gameTime\">\n" +
                "\t\t\t\t9:30 <span>PM</span>\n" +
                "\t\t\t</th>\n" +
                "\t\t</tr>\n" +
                "\t</thead>\n" +
                "\t\n" +
                "\t<tbody class=\"league_8207\">\n" +
                "\t\t\n" +
                "\t\t\t<tr class=\"even\">\n" +
                "\t\t\t\t<td class=\"gameField rowLabel\">\n" +
                "\t\t\t\t\t<div><a href=\"/location/1259\">North Shore Park</a></div>\n" +
                "\t\t\t\t\tField 1\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t\t\n" +
                "\t<td class=\"firstTime firstLastField clickable\" data-teamoneid=\"76833\" data-teamtwoid=\"77966\" id=\"d_2015-01-05_19-30-00_1259_1\">\n" +
                "\t\t<div class=\"gameCellContent\">\n" +
                "\t\t\n" +
                "\t\t\t\n" +
                "\t<div align=\"left\" class=\"teamColorSwatch_Small\" style=\"background:#bbbbbb\">&nbsp;</div>&nbsp; \n" +
                "\n" +
                "\t\t\t<a href=\"/team/76833/Jedi-Mind-Kicks\">\n" +
                "\t\t\t\tJedi Mind Kicks\n" +
                "\t\t\t</a>\n" +
                "\t\t\t<span class=\"scoreDisplay\">[6]</span>\n" +
                "\t\t\t\n" +
                "\t\t\t<div class=\"gameVS\">vs</div>\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t<div align=\"left\" class=\"teamColorSwatch_Small\" style=\"background:#09b9f6\">&nbsp;</div>&nbsp; \n" +
                "\n" +
                "\t\t\t<a href=\"/team/77966/One-Kick-Wonders\">\n" +
                "\t\t\t\tOne Kick Wonders\n" +
                "\t\t\t</a>\n" +
                "\t\t\t<span class=\"scoreDisplay\">[9]</span>\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t</div>\n" +
                "\t</td>\n" +
                "\n" +
                "\t\n" +
                "\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t\t\n" +
                "\t<td class=\"firstLastField clickable\" data-teamoneid=\"76834\" data-teamtwoid=\"76942\" id=\"d_2015-01-05_22-30-00_1259_1\">\n" +
                "\t\t<div class=\"gameCellContent\">\n" +
                "\t\t\n" +
                "\t\t\t\n" +
                "\t<div align=\"left\" class=\"teamColorSwatch_Small\" style=\"background:#f6f923\">&nbsp;</div>&nbsp; \n" +
                "\n" +
                "\t\t\t<a href=\"/team/76834/Alchobalics\">\n" +
                "\t\t\t\tAlchobalics\n" +
                "\t\t\t</a>\n" +
                "\t\t\t<span class=\"scoreDisplay\">[2]</span>\n" +
                "\t\t\t\n" +
                "\t\t\t<div class=\"gameVS\">vs</div>\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t<div align=\"left\" class=\"teamColorSwatch_Small\" style=\"background:#000000\">&nbsp;</div>&nbsp; \n" +
                "\n" +
                "\t\t\t<a href=\"/team/76942/Always-Sliding-In\">\n" +
                "\t\t\t\tAlways Sliding In\n" +
                "\t\t\t</a>\n" +
                "\t\t\t<span class=\"scoreDisplay\">[14]</span>\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t</div>\n" +
                "\t</td>\n" +
                "\n" +
                "\t\n" +
                "\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t\t\n" +
                "\t<td class=\"firstLastField clickable\" data-teamoneid=\"76856\" data-teamtwoid=\"77758\" id=\"d_2015-01-05_20-30-00_1259_1\">\n" +
                "\t\t<div class=\"gameCellContent\">\n" +
                "\t\t\n" +
                "\t\t\t\n" +
                "\t<div align=\"left\" class=\"teamColorSwatch_Small\" style=\"background:#308d16\">&nbsp;</div>&nbsp; \n" +
                "\n" +
                "\t\t\t<a href=\"/team/76856/Toes-of-Thunder\">\n" +
                "\t\t\t\tToes of Thunder\n" +
                "\t\t\t</a>\n" +
                "\t\t\t<span class=\"scoreDisplay\">[22]</span>\n" +
                "\t\t\t\n" +
                "\t\t\t<div class=\"gameVS\">vs</div>\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t<div align=\"left\" class=\"teamColorSwatch_Small\" style=\"background:#800000\">&nbsp;</div>&nbsp; \n" +
                "\n" +
                "\t\t\t<a href=\"/team/77758/St.-Pete-Young-Professionals-\">\n" +
                "\t\t\t\tSt. Pete Young Professionals \n" +
                "\t\t\t</a>\n" +
                "\t\t\t<span class=\"scoreDisplay\">[15]</span>\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t</div>\n" +
                "\t</td>\n" +
                "\n" +
                "\t\n" +
                "\n" +
                "\t\t\t\t\n" +
                "\t\t\t\t\t\n" +
                "\t<td class=\"lastTime firstLastField clickable\" data-teamoneid=\"76832\" data-teamtwoid=\"76872\" id=\"d_2015-01-05_21-30-00_1259_1\">\n" +
                "\t\t<div class=\"gameCellContent\">\n" +
                "\t\t\n" +
                "\t\t\t\n" +
                "\t<div align=\"left\" class=\"teamColorSwatch_Small\" style=\"background:#ffffff\">&nbsp;</div>&nbsp; \n" +
                "\n" +
                "\t\t\t<a href=\"/team/76832/Going-Yard\">\n" +
                "\t\t\t\tGoing Yard\n" +
                "\t\t\t</a>\n" +
                "\t\t\t<span class=\"scoreDisplay\">[19]</span>\n" +
                "\t\t\t\n" +
                "\t\t\t<div class=\"gameVS\">vs</div>\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t<div align=\"left\" class=\"teamColorSwatch_Small\" style=\"background:#FFA500\">&nbsp;</div>&nbsp; \n" +
                "\n" +
                "\t\t\t<a href=\"/team/76872/Free-Ballers\">\n" +
                "\t\t\t\tFree Ballers\n" +
                "\t\t\t</a>\n" +
                "\t\t\t<span class=\"scoreDisplay\">[15]</span>\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t</div>\n" +
                "\t</td>\n" +
                "\n" +
                "\t\n" +
                "\n" +
                "\t\t\t\t\n" +
                "\t\t\t</tr>\n" +
                "\t\t\t\n" +
                "\t\t\n" +
                "\t</tbody>\n" +
                "</table>\n" +
                "\t\t\t<p class=\"label homeAndVisitor\">Visitor (first team) vs Home (second team)</p>\n" +
                "\t\t\n" +
                "\t\t\n" +
                "\t\n" +
                "</div>";
    }
}
