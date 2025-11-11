public class OpenAir extends Venue {
    private boolean shadeCoverage;

    public OpenAir(String venueName, int capacity, double cost, boolean pctSeatUnavailable,
                   int vipPct, int goldPct, int silverPct, int bronzePct, int generalAdmissionPct,
                   int reservedExtraPct, boolean fireworksPlanned, double fireworksCost,
                   boolean shadeCoverage) {
        super(venueName, capacity, cost, pctSeatUnavailable, vipPct, goldPct, silverPct, bronzePct,
              generalAdmissionPct, reservedExtraPct, fireworksPlanned, fireworksCost);
        this.shadeCoverage = shadeCoverage;
    }

    @Override
    public String getVenueType() {
        return "OpenAir";
    }

    public boolean hasShadeCoverage() { return shadeCoverage; }
}