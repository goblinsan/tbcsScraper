package mobileTbcs.core;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class Game implements Comparable {
    String location;
    List<Team> teams = new ArrayList<Team>();
    DateTime dateTime;
    Team opponent;

    @Override
    public int compareTo(Object o) {
        return dateTime.compareTo(((Game) o).getDateTime());
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Team getOpponent() {
        return opponent;
    }

    public void setOpponent(Team opponent) {
        this.opponent = opponent;
    }
}
