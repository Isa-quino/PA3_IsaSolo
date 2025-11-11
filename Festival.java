import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Festival {
    // Attributes
    private String themeName;
    private Date startDate;
    private Date endDate;
    private List artistList = new ArrayList<String>();
    private List vendorList = new ArrayList<String>();
    private boolean campingAvailable;
    private boolean adultsOnly;

    public Festival() {}
    
    public Festival(String themeName, Date startDate, Date endDate, List artistList, List vendorList,
            boolean campingAvailable, boolean adultsOnly) {
        this.themeName = themeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.artistList = artistList;
        this.vendorList = vendorList;
        this.campingAvailable = campingAvailable;
        this.adultsOnly = adultsOnly;
    }

    // Getters & Setters
    public String getThemeName() {
        return themeName;
    }
    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public List getArtistList() {
        return artistList;
    }
    public void setArtistList(List artistList) {
        this.artistList = artistList;
    }
    public List getVendorList() {
        return vendorList;
    }
    public void setVendorList(List vendorList) {
        this.vendorList = vendorList;
    }
    public boolean isCampingAvailable() {
        return campingAvailable;
    }
    public void setCampingAvailable(boolean campingAvailable) {
        this.campingAvailable = campingAvailable;
    }
    public boolean isAdultsOnly() {
        return adultsOnly;
    }
    public void setAdultsOnly(boolean adultsOnly) {
        this.adultsOnly = adultsOnly;
    }
}
