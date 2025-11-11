import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReceiptUtil {
    private static final SimpleDateFormat TS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void writePurchaseReceipt(Customer c, Invoice inv) {
        String dir = "receipts";
        new java.io.File(dir).mkdirs();
        String file = String.format("%s/%s-%d.txt", dir, c.getUsername(), inv.getInvoiceId());
        try (FileWriter w = new FileWriter(file)) {
            w.write("/////////////////////// RECEIPT ///////////////////////\n");
            w.write("Confirmation #: " + inv.getInvoiceId() + "\n");
            w.write("Customer: " + c.getUsername() + "\n");
            w.write("Event Type: " + inv.getEventType() + "\n");
            w.write("Event Name: " + inv.getEventName() + "\n");
            w.write("Event Date: " + inv.getEventDateStr() + "\n");
            w.write("Ticket Type: " + inv.getTicketType() + "\n");
            w.write("Number of Tickets: " + inv.getQuantity() + "\n");
            w.write(String.format("Total Price: $%.2f\n", inv.getTotalCost()));
            w.write("Purchase Completed: " + TS.format(new Date()) + "\n");
            w.write("///////////////////////////////////////////////////////\n");
        } catch (IOException ex) {
            System.out.println("ERROR writing receipt: " + ex.getMessage());
        }
    }

    public static void writeCustomerReceiptSummary(Customer c) {
        String dir = "summaries";
        new java.io.File(dir).mkdirs();
        String file = String.format("%s/%s-summary.txt", dir, c.getUsername());
        try (FileWriter w = new FileWriter(file)) {
            w.write("//////////////// CUSTOMER RECEIPT SUMMARY ////////////////\n");
            List<Invoice> list = new ArrayList<>(c.getInvoices());
            // newest first by invoice id (proxy for time in this app)
            list.sort(Comparator.comparingInt(Invoice::getInvoiceId).reversed());
            for (Invoice inv : list) {
                w.write("\n---\n");
                w.write("Confirmation #: " + inv.getInvoiceId() + "\n");
                w.write("Event Type: " + inv.getEventType() + "\n");
                w.write("Event Name: " + inv.getEventName() + "\n");
                w.write("Event Date: " + inv.getEventDateStr() + "\n");
                w.write("Ticket Type: " + inv.getTicketType() + "\n");
                w.write("Number of Tickets: " + inv.getQuantity() + "\n");
                w.write(String.format("Total Price: $%.2f\n", inv.getTotalCost()));
            }
            w.write("\n//////////////////////////////////////////////////////////\n");
        } catch (IOException ex) {
            System.out.println("ERROR writing customer summary: " + ex.getMessage());
        }
    }
}
