import java.util.Date;

/**
 * @author Daniela Molina-Najera
 * Represents a ticket purchase transaction in the TicketMiner system.
 * Stores details about the customer, event, ticket type, quantity, pricing,
 * and membership discounts.
 * 
 * Each invoice is uniquely identified and can be printed or serialized
 * for reporting, refunding, or auditing purposes.
 */

public class Invoice {
    // Attributes
    private Customer customer;
    private Event event;
    private TicketType ticketType;
    private int quantity;
    private double unitPrice;
    private double totalCost;
    private Date transactionDate;
    private boolean membershipDiscountApplied;
    private int invoiceId;
    private double finalPrice;
    private boolean isMember;

    // Constructors
    public Invoice() {}

    public Invoice(Customer customer, Event event, TicketType type, int quantity, double finalPrice, double totalCost, boolean isMember) {
        this.customer = customer;
        this.event = event;
        this.ticketType = type;
        this.quantity = quantity;
        this.unitPrice = finalPrice;
        this.totalCost = totalCost;
        this.membershipDiscountApplied = isMember;
        this.transactionDate = new Date();
        this.invoiceId = generateInvoiceId(); // helper method
    }

    public Invoice(Customer customer, Event event, TicketType ticketType, int quantity, double totalCost,
            int invoiceId) {
        this.customer = customer;
        this.event = event;
        this.ticketType = ticketType;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.invoiceId = invoiceId;
    }

    public Invoice(Customer customer2, Event event2, TicketType type, int quantity2, double finalPrice2,
            double totalCost2, boolean isMember2, int invoiceId2) {
        this.customer = customer2;
        this.event = event2;
        this.ticketType = type;
        this.quantity =  quantity2;
        this.finalPrice = finalPrice2;
        this.totalCost = totalCost2;
        this.isMember = isMember2;
        this.invoiceId = invoiceId2; 
    }

    private static int nextInvoiceId = 1; // keeping private so only Invoice class can control
    public static int generateInvoiceId() { // will generate unique Invoice ID's
        return nextInvoiceId++;
    }

    public void printInvoice() {
        System.out.println("/////////////////////// INVOICE ///////////////////////");
        System.out.println("Invoice ID: " + invoiceId);
        System.out.println("Customer: " + customer.getUsername());
        System.out.println("Event: " + event.getName() + " (ID: " + event.getEventId() + ")");
        System.out.println("Ticket Type: " + ticketType);
        System.out.println("Quantity: " + quantity);
        System.out.println("Unit Price: $" + String.format("%.2f", unitPrice));
        System.out.println("Total Cost: $" + String.format("%.2f", totalCost));
        System.out.println("Date: " + transactionDate);
        if (membershipDiscountApplied) {
            System.out.println("Miner Membership Discount Applied: 10%");
        }
        System.out.println("///////////////////////////////////////////////////////");
    }

    
    public String toString() {
        return "Invoice ID: " + invoiceId +
            "\nCustomer: " + customer.getUsername() +
            "\nEvent: " + event.getName() + " (ID: " + event.getEventId() + ")" +
            "\nTicket Type: " + ticketType +
            "\nQuantity: " + quantity +
            "\nUnit Price: $" + String.format("%.2f", unitPrice) +
            "\nTotal Cost: $" + String.format("%.2f", totalCost);
    }

    public Object getEvent() {
        return event.getName();
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public String getEventType(){ 
        return event.getEventType(); 
    }

    public String getEventName(){
        return event.getName(); 
    }
    public String getEventDateStr(){ 
        return event.getFormattedDate(); 
    }
   
}