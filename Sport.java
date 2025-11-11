import java.sql.Time;

public class Sport {
    // Attributes
    private String sportType;
    private String homeTeam;
    private String awayTeam;
    private boolean isTournament;
    private Time duration;
    private Time halftimeDuration;

    // Constructors
    public Sport() {}
    
    public Sport(String sportType, String homeTeam, String awayTeam, boolean isTournament, Time duration,
            Time halftimeDuration) {
        this.sportType = sportType;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.isTournament = isTournament;
        this.duration = duration;
        this.halftimeDuration = halftimeDuration;
    }

    // Getters & Setters
    public String getSportType() {
        return sportType;
    }
    public void setSportType(String sportType) {
        this.sportType = sportType;
    }
    public String getHomeTeam() {
        return homeTeam;
    }
    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }
    public String getAwayTeam() {
        return awayTeam;
    }
    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }
    public boolean isTournament() {
        return isTournament;
    }
    public void setTournament(boolean isTournament) {
        this.isTournament = isTournament;
    }
    public Time getDuration() {
        return duration;
    }
    public void setDuration(Time duration) {
        this.duration = duration;
    }
    public Time getHalftimeDuration() {
        return halftimeDuration;
    }
    public void setHalftimeDuration(Time halftimeDuration) {
        this.halftimeDuration = halftimeDuration;
    }
}
