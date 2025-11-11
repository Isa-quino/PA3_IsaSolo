public enum TicketType {
    VIP(5.0), GOLD(3.0), SILVER(2.5), BRONZE(1.5), GENERAL_ADMISSION(1.0);

    private final double priceMultiplier;

    TicketType(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public double getMultipler() {
        return this.priceMultiplier;
    }

    public static TicketType fromInt(int choice) {
        switch (choice) {
            case 1:
                return VIP;
            case 2:
                return GOLD;
            case 3:
                return SILVER;
            case 4:
                return BRONZE;
            case 5:
                return GENERAL_ADMISSION;
            default:
                throw new IllegalArgumentException("Invalid ticket type: " + choice);
        }
    }

    /**
     * 
     * @param type
     * @param gaBasePrice
     * @return gaBasePrice * type.getMultiplier
     */
    public static double computePrice(TicketType type, double gaBasePrice) {
        return gaBasePrice * type.getMultipler();
    }
}