package mobileTbcs.core;

import mobileTbcs.SiteScraper;

import java.io.IOException;
import java.util.List;

public class League {
    String name;
    String id;
    String day;
    String sport;
    List<Team> teams;
    List<Game> schedule;
    SiteScraper siteScraper;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Game> getSchedule() {
        if (schedule == null){
            try {
                schedule = siteScraper.getSchedule(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return schedule;
    }

    public void setSchedule(List<Game> schedule) {
        this.schedule = schedule;
    }

    public String toString(){
        return name;
    }

    public void setSiteScraper(SiteScraper siteScraper) {
        this.siteScraper = siteScraper;
    }
}
