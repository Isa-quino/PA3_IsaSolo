import java.sql.Time;

public class Concert {
    // Attributes
    private String artistName;
    private boolean isBand;
    private String genre;
    private Time duration;
    private boolean hasMerch;

    // Constructors
    public Concert() {}
    
    public Concert(String artistName, boolean isBand, String genre, Time duration, boolean hasMerch) {
        this.artistName = artistName;
        this.isBand = isBand;
        this.genre = genre;
        this.duration = duration;
        this.hasMerch = hasMerch;
    }

    // Getters & Setters
    public String getArtistName() {
        return artistName;
    }
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
    public boolean isBand() {
        return isBand;
    }
    public void setBand(boolean isBand) {
        this.isBand = isBand;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public Time getDuration() {
        return duration;
    }
    public void setDuration(Time duration) {
        this.duration = duration;
    }
    public boolean isHasMerch() {
        return hasMerch;
    }
    public void setHasMerch(boolean hasMerch) {
        this.hasMerch = hasMerch;
    }
}
