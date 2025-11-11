public abstract class Venue {

    protected String venueName;
    protected int capacity;
    protected double cost;
    protected boolean pctSeatUnavailable;
    protected int vipPct, goldPct, silverPct, bronzePct, generalAdmissionPct, reservedExtraPct;
    protected boolean fireworksPlanned;
    protected double fireworksCost;

    public Venue(String venueName, int capacity, double cost, boolean pctSeatUnavailable,
                 int vipPct, int goldPct, int silverPct, int bronzePct, int generalAdmissionPct,
                 int reservedExtraPct, boolean fireworksPlanned, double fireworksCost) {
        this.venueName = venueName;
        this.capacity = capacity;
        this.cost = cost;
        this.pctSeatUnavailable = pctSeatUnavailable;
        this.vipPct = vipPct;
        this.goldPct = goldPct;
        this.silverPct = silverPct;
        this.bronzePct = bronzePct;
        this.generalAdmissionPct = generalAdmissionPct;
        this.reservedExtraPct = reservedExtraPct;
        this.fireworksPlanned = fireworksPlanned;
        this.fireworksCost = fireworksCost;
    }

    public abstract String getVenueType();

    public void venueDetails() {
        System.out.println("Venue Type: " + getVenueType());
        System.out.println("Name: " + venueName);
        System.out.println("Capacity: " + capacity);
        System.out.println("Cost: $" + cost);
        System.out.println("VIP Pct: " + vipPct);
        System.out.println("Gold Pct: " + goldPct);
        System.out.println("Silver Pct: " + silverPct);
        System.out.println("Bronze Pct: " + bronzePct);
        System.out.println("General Admission Pct: " + generalAdmissionPct);
        System.out.println("Fireworks Planned: " + fireworksPlanned);
        System.out.println("Fireworks Cost: $" + fireworksCost);
    }

    public int getCapacity() {
        return this.capacity;
    }
}