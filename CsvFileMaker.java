import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Daniela Molina-Najera
 * The class will export system data to a csv file.
 * It provides static methods to write event and customer lists to disk
 * in a structured, readable format.
 */

public class CsvFileMaker {

    /**
     * Will write a list of Event objects to a csv file.
     * @param events list of events to export
     * @param filename name of csv file to write to
     */
    public static void saveEventsToCSV(List<Event> events, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("EventID,Type,Name,Date,Time,VIP,Gold,Silver,Bronze,General,VenueType");

            for (Event e : events) {
                writer.printf("%d,%s,%s,%s,%s,%.2f,%.2f,%.2f,%.2f,%.2f,%s%n",
                    e.getEventId(),
                    e.getEventType(),
                    e.getName(),
                    e.getFormattedDate(),
                    e.getFormattedTime(),
                    e.getVipPrice(),
                    e.getGoldPrice(),
                    e.getSilverPrice(),
                    e.getBronzePrice(),
                    e.getGeneralAdmissionPrice(),
                    e.getVenue()
                );
            }
            System.out.println("Events saved to " + filename);
        } catch (IOException ex) {
            System.out.println("Error writing events to CSV: " + ex.getMessage());
        }
    }

    /**
     * Writes a list of Customer objects to a csv file.
     * @param customerList list of customers to export
     * @param filename name of file to export to
     */
    public static void saveCustomersToCSV(List<Customer> customerList, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Writing the column names
            writer.println("ID,First Name,Last Name,Money Available,Concerts Purchased,TicketMiner Membership,Username,Password");

            // Writimg each customer
            for (Customer c : customerList) {
                writer.printf("%d,%s,%s,%.2f,%d,%b,%s,%s%n",
                    c.getCustomerId(),                     // int
                    c.getFirstName(),              // String
                    c.getLastName(),               // String
                    c.getMoneyAvailable(),         // double
                    c.getConcertsPurchased(),      // int
                    c.getHasMembership(),                  // boolean
                    c.getUsername(),               // String
                    c.getPassword()                // String
                );
            }
            System.out.println("Customers saved to " + filename);
        } catch (IOException ex) {
            System.out.println("Error writing customers to CSV: " + ex.getMessage());
        }
    }
}