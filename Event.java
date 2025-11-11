import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniela Molina-Najera
 * Represents a scheduled event in the ticketing system.
 * Encapsulates details such as event type, pricing tiers, venue information,
 * ticket availability, and purchase history.
 * 
 * Supports ticket transactions, availability checks, and formatted output
 * for display and CSV export.
 */

public class Event {
    // Attributes
    private int eventId;
    private String eventType;
    private String name;
    private LocalDate date;
    private LocalTime time;

    private double vipPrice;
    private double goldPrice;
    private double silverPrice;
    private double bronzePrice;
    private double generalAdmissionPrice;
    private Venue venueType;

    private int vipAvailable;
    private int goldAvailable;
    private int silverAvailable;
    private int bronzeAvailable;
    private int generalAdmissionAvailable;

    private List<Ticket> purchasedTickets = new ArrayList<>();
    private String venueName;
    private int capacity;
    private double cost;
    private boolean fireworksPlanned;
    private double fireworksCost;
    private Venue venue;

    // accounting per event (admin)
    private double totalTaxCollected;
    private double totalDiscountGiven;

    public Event() {}

    public Event(int id, String type, String name2, LocalDate date2, LocalTime time2, double vip, double gold,
                 double silver, double bronze, double general, int vipAvailable2, int goldAvailable2, int silverAvailable2,
                 int bronzeAvailable2, int generalAvailable, String venueName2, Venue venueType2, int capacity2,
                 double cost2, boolean fireworksPlanned2, double fireworksCost2) {
        this.eventId = id;
        this.eventType = type;
        this.name = name2;
        this.date = date2;
        this.time = time2;
        this.vipPrice = vip;
        this.goldPrice = gold;
        this.silverPrice = silver;
        this.bronzePrice = bronze;
        this.generalAdmissionPrice = general;
        this.vipAvailable = vipAvailable2;
        this.goldAvailable = goldAvailable2;
        this.silverAvailable = silverAvailable2;
        this.bronzeAvailable = bronzeAvailable2;
        this.generalAdmissionAvailable = generalAvailable;
        this.venueName = venueName2;
        this.venueType = venueType2;
        this.capacity = capacity2;
        this.cost = cost2;
        this.fireworksPlanned = fireworksPlanned2;
        this.fireworksCost = fireworksCost2;
    }

    public Event(int newEventId, String eType, String eName, LocalDate eDate, LocalTime eTime, double vipPrice2,
                 double goldPrice2, double silverPrice2, double bronzePrice2, double generalAdmissionPrice2, Venue venue) {
        this.eventId = newEventId;
        this.eventType = eType;
        this.name = eName;
        this.date = eDate;
        this.time = eTime;
        this.vipPrice = vipPrice2;
        this.goldPrice = goldPrice2;
        this.silverPrice = silverPrice2;
        this.bronzePrice = bronzePrice2;
        this.generalAdmissionPrice = generalAdmissionPrice2;
        this.venue = venue;
    }

    // Getters & Setters
    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFormattedDate() { return date.toString(); }
    public void setDate(LocalDate date) { this.date = date; }
    public String getFormattedTime() { return time.toString(); }
    public void setTime(LocalTime time) { this.time = time; }
    public double getVipPrice() { return vipPrice; }
    public void setVipPrice(double vipPrice) { this.vipPrice = vipPrice; }
    public double getGoldPrice() { return goldPrice; }
    public void setGoldPrice(double goldPrice) { this.goldPrice = goldPrice; }
    public double getSilverPrice() { return silverPrice; }
    public void setSilverPrice(double silverPrice) { this.silverPrice = silverPrice; }
    public double getBronzePrice() { return bronzePrice; }
    public void setBronzePrice(double bronzePrice) { this.bronzePrice = bronzePrice; }
    public double getGeneralAdmissionPrice() { return generalAdmissionPrice; }
    public void setGeneralAdmissionPrice(double generalAdmissionPrice) { this.generalAdmissionPrice = generalAdmissionPrice; }
    public Venue getVenueType() { return venueType; }
    public Venue getVenue() { return this.venue; }
    public void setVenue(Venue venue) { this.venue = venue; }

    /** Admin-facing full details */
    public void printEventInfo() {
        System.out.println("///////////////////////////////////////////////////////");
        System.out.println("Event ID: " + eventId);
        System.out.println("Event Type: " + eventType);
        System.out.println("Name: " + name);
        System.out.println("Time of Event: " + time);
        System.out.println("VIP Price: $" + vipPrice);
        System.out.println("Gold Price: $" + goldPrice);
        System.out.println("Silver Price: $" + silverPrice);
        System.out.println("Bronze Price: $" + bronzePrice);
        System.out.println("General Admission Price: $" + generalAdmissionPrice);
        String venueLabel = (venue != null ? venue.getVenueType()
                                           : (venueType != null ? venueType.getVenueType() : "Unknown"));
        System.out.println("Venue Type: " + venueLabel);
        System.out.println("Fireworks Included: " + (fireworksPlanned ? "Yes" : "No"));
        System.out.println("Total Event Cost: $" + String.format("%.2f", cost));
        System.out.println("Total Tax Collected (to date): $" + String.format("%.2f", totalTaxCollected));
        System.out.println("Total Member Discounts Given (to date): $" + String.format("%.2f", totalDiscountGiven));
        System.out.println("///////////////////////////////////////////////////////");
    }

    /** Customer-facing concise summary (no internal totals) */
    public void printEventInfoCustomer() {
        System.out.println("-------------------------------------------------------");
        System.out.println("Event ID: " + eventId);
        System.out.println("Type: " + eventType);
        System.out.println("Name: " + name);
        System.out.println("Date: " + getFormattedDate());
        System.out.println("Time: " + getFormattedTime());
        String venueLabel = (venue != null ? venue.getVenueType()
                                           : (venueType != null ? venueType.getVenueType() : "TBA"));
        System.out.println("Venue: " + venueLabel);
        System.out.println("Ticket Prices:");
        System.out.println("  VIP: $" + vipPrice +
                           " | Gold: $" + goldPrice +
                           " | Silver: $" + silverPrice +
                           " | Bronze: $" + bronzePrice +
                           " | GA: $" + generalAdmissionPrice);
        System.out.println("-------------------------------------------------------");
    }

    @Override
    public String toString() {
        return "Event ID: " + eventId +
               "\nEvent Type: " + eventType +
               "\nName: " + name +
               "\nTime of Event: " + time +
               "\nVIP Price: $" + vipPrice +
               "\nGOLD Price: $" + goldPrice +
               "\nSILVER Price: $" + silverPrice +
               "\nBRONZE Price: $" + bronzePrice +
               "\nGENERAL Price: $" + generalAdmissionPrice +
               "\nVenue Type: " + (venue != null ? venue.getVenueType()
                                                : (venueType != null ? venueType.getVenueType() : "Unknown"));
    }

    public void addTickets(List<Ticket> tickets) {
        if (tickets != null) {
            purchasedTickets.addAll(tickets);
        }
    }

    public double getPriceByType(TicketType type) {
        switch (type) {
            case VIP: return vipPrice;
            case GOLD: return goldPrice;
            case SILVER: return silverPrice;
            case BRONZE: return bronzePrice;
            case GENERAL_ADMISSION: return generalAdmissionPrice;
            default: throw new IllegalArgumentException("Unknow Ticket Type: " + type);
        }
    }

    public boolean hasAvailableTickets(TicketType type, int quantity) {
        switch (type) {
            case VIP: return vipAvailable >= quantity;
            case GOLD: return goldAvailable >= quantity;
            case SILVER: return silverAvailable >= quantity;
            case BRONZE: return bronzeAvailable >= quantity;
            case GENERAL_ADMISSION: return generalAdmissionAvailable >= quantity;
            default: return false;
        }
    }

    private static int largestEventId = 1000;
    public static int generateNextEventId() { return largestEventId++; }

    public void computePricesFromGA(double gaPrice) {
        this.generalAdmissionPrice = Math.min(gaPrice, 4000);
        this.vipPrice = TicketType.VIP.getMultipler() * generalAdmissionPrice;
        this.goldPrice = TicketType.GOLD.getMultipler() * generalAdmissionPrice;
        this.silverPrice = TicketType.SILVER.getMultipler() * generalAdmissionPrice;
        this.bronzePrice = TicketType.BRONZE.getMultipler() * generalAdmissionPrice;
    }

    public void computeAvailabilityFromVenue() {
        this.capacity = venue.getCapacity();
        this.vipAvailable = (int)(capacity * venue.vipPct / 100.0);
        this.goldAvailable = (int)(capacity * venue.goldPct / 100.0);
        this.silverAvailable = (int)(capacity * venue.silverPct / 100.0);
        this.bronzeAvailable = (int)(capacity * venue.bronzePct / 100.0);
        this.generalAdmissionAvailable = (int)(capacity * venue.generalAdmissionPct / 100.0);
    }

    public void setFireworks(boolean hasFireworks) {
        this.fireworksPlanned = hasFireworks;
        this.fireworksCost = hasFireworks ? venue.fireworksCost : 0.0;
        this.cost = venue.cost + fireworksCost;
    }

    public void decrementAvailability(TicketType type, int qty) {
        switch (type) {
            case VIP:               vipAvailable -= qty; break;
            case GOLD:              goldAvailable -= qty; break;
            case SILVER:            silverAvailable -= qty; break;
            case BRONZE:            bronzeAvailable -= qty; break;
            case GENERAL_ADMISSION: generalAdmissionAvailable -= qty; break;
        }
        if (vipAvailable < 0) vipAvailable = 0;
        if (goldAvailable < 0) goldAvailable = 0;
        if (silverAvailable < 0) silverAvailable = 0;
        if (bronzeAvailable < 0) bronzeAvailable = 0;
        if (generalAdmissionAvailable < 0) generalAdmissionAvailable = 0;
    }

    public void addTax(double amount){ totalTaxCollected += amount; }
    public void addDiscount(double amount){ totalDiscountGiven += amount; }
    public double getTotalTaxCollected(){ return totalTaxCollected; }
    public double getTotalDiscountGiven(){ return totalDiscountGiven; }
}