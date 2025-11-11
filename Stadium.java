public class Stadium extends Venue {
    private String roofType;
    private String fieldType;

    public Stadium(String venueName, int capacity, double cost, boolean pctSeatUnavailable,
                   int vipPct, int goldPct, int silverPct, int bronzePct, int generalAdmissionPct,
                   int reservedExtraPct, boolean fireworksPlanned, double fireworksCost,
                   String roofType, String fieldType) {
        super(venueName, capacity, cost, pctSeatUnavailable, vipPct, goldPct, silverPct, bronzePct,
              generalAdmissionPct, reservedExtraPct, fireworksPlanned, fireworksCost);
        this.roofType = roofType;
        this.fieldType = fieldType;
    }

    @Override
    public String getVenueType() {
        return "Stadium";
    }

    public String getRoofType() { return roofType; }
    public String getFieldType() { return fieldType; }
}