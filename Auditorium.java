public class Auditorium extends Venue {
    private String stageSize;

    public Auditorium(String venueName, int capacity, double cost, boolean pctSeatUnavailable,
                      int vipPct, int goldPct, int silverPct, int bronzePct, int generalAdmissionPct,
                      int reservedExtraPct, boolean fireworksPlanned, double fireworksCost,
                      String stageSize) {
        super(venueName, capacity, cost, pctSeatUnavailable, vipPct, goldPct, silverPct, bronzePct,
              generalAdmissionPct, reservedExtraPct, fireworksPlanned, fireworksCost);
        this.stageSize = stageSize;
    }

    @Override
    public String getVenueType() {
        return "Auditorium";
    }

    public String getStageSize() { return stageSize; }
}