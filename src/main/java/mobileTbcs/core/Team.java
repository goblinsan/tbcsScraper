package mobileTbcs.core;

import java.util.List;

public class Team {
    String name;
    String id;
    String url;
    String color;
    List<Game> schedule;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Game> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Game> schedule) {
        this.schedule = schedule;
    }
}
