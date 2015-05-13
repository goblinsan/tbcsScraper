package mobileTbcs.core;

import org.joda.time.DateTime;

import java.util.List;

public class TeamIndex implements Indexed {
    DateTime age;
    List<Team> teams;

    public TeamIndex() {
        age = new DateTime();
    }

    public DateTime getAge() {
        return age;
    }

    public void setAge(DateTime age) {
        this.age = age;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
