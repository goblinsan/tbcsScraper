package mobileTbcs.core;

import org.joda.time.DateTime;

import java.util.List;

public class LeagueIndex implements Indexed{
    List<League> leagues;
    DateTime age;

    public LeagueIndex() {
        age = new DateTime();
    }

    public List<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<League> leagues) {
        this.leagues = leagues;
    }

    public DateTime getAge() {
        return age;
    }

    public void setAge(DateTime age) {
        this.age = age;
    }
}
