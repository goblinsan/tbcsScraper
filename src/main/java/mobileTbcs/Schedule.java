package mobileTbcs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import static mobileTbcs.CalendarUtils.*;

public class Schedule {
    private Calendar today = Calendar.getInstance();

    private List<String> sports = Arrays.asList("Soccer","Softball","Basketball");

    private List<Calendar> games;

    public Schedule(){
        Calendar firstGame = Calendar.getInstance();
        firstGame.set(2015,Calendar.FEBRUARY,24,19,30);
        games = new ArrayList<Calendar>();
        games.add(firstGame);
    }


    public String showNextGame() {
        return getNextScheduleDay() + " at " + getNextScheduleTime();
    }

    private String getNextScheduleTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("h:m");
        return sdf.format(games.get(0).getTime());
    }

    public String getNextScheduleDay() {
        return isNextGameToday() ? "Tonight" : readableGameDate();
    }

    private boolean isNextGameToday(){
        return theseAreTheSameDay(today, games.get(0));
    }

    private String readableGameDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("d");
        return sdf.format(games.get(0).getTime());
    }

    public void setToday(Calendar today) {
        this.today = today;
    }

    public int getNumberOfGames() {
        return games.size();
    }

    public void setGames(List<Calendar> games) {
        this.games = games;
    }

    public String printNextGames() {
        String output = "";
        for(Calendar game : games){
            if (theseAreTheSameDay(today, game) || dayOneIsBeforeDayTwo(today, game)) output += game.getTime();
        }
        return output;
    }

    public String printAllGames() {
        String output = "";
        for(Calendar game : games){
            output += game.getTime();
        }
        return output;
    }


    public List<String> getSports() {
        return sports;
    }
}
