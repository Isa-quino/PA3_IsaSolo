import java.util.*;

public class VenueFactory {
    public static final List<String> venueNames = Arrays.asList(
        "Sun Bowl Stadium",
        "Don Haskins Center",
        "Magoffin Auditorium",
        "San Jacinto Plaza",
        "Centennial Plaza"
    );

    /**
     * Prints the list of venue names for the user to choose from
     */
    public static void printVenueOptions() {
        System.out.println("Choose a Venue: ");
        for (int i = 0; i < venueNames.size(); i++) {
            System.out.println("(" + (i + 1) + ") " + venueNames.get(i));
        }
    }

    /**
     * Create a venue instance by name (tolerant to case and minor spelling).
     */
    public static Venue createVenue(String venueName) {
        if (venueName == null) throw new IllegalArgumentException("Unknown venue name: null");
        String key = venueName.trim().toLowerCase(Locale.ROOT);

        switch (key) {
            case "sun bowl stadium":
                return new Stadium("Sun Bowl Stadium", 50000, 100000, false,
                        5, 10, 20, 30, 30, 5, true, 10000,
                        "Retractable", "Grass");

            case "don haskins center":
                return new Auditorium("Don Haskins Center", 12000, 60000, false,
                        5, 10, 20, 30, 35, 0, false, 0, "Large");

            case "magoffin auditorium":
                return new Auditorium("Magoffin Auditorium", 2000, 15000, false,
                        5, 10, 15, 20, 45, 5, false, 0, "Big");

            case "san jacinto plaza":
                return new OpenAir("San Jacinto Plaza", 5000, 0, false,
                        0, 0, 0, 0, 0, 0, false, 0, false);

            case "centennial plaza":
            case "centenial plaza": // accept typo
                return new OpenAir("Centennial Plaza", 8000, 0, false,
                        0, 0, 0, 0, 0, 0, false, 0, false);

            default:
                // Be tolerant: fallback to an open-air venue to avoid crashing on unfamiliar inputs
                return new OpenAir(venueName.trim(), 5000, 0, false,
                        0, 0, 0, 0, 0, 0, false, 0, false);
        }
    }
}
