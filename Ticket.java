public class Ticket {
    // attributes
    private Event event;
    private Customer customer;
    private TicketType type;
    private double price;
    private int quantity;
    private double totalCost;

    // constructors
    public Ticket() {}

    public Ticket(Event event, Customer customer, TicketType type, double price, int quantity) {
        this.event = event;
        this.customer = customer;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.totalCost = price * quantity;
    }

    public Ticket(Event event, TicketType type, double price) {
        this.event = event;
        this.type = type;
        this.price = price;
        this.quantity = 1;
        this.totalCost = price;
    }

    public Ticket(Event event, TicketType type, int quantity, double totalCost) {
        this.event = event;
        this.type = type;
        this.quantity = quantity;
        this.totalCost = totalCost;
        this.price = totalCost / quantity;
    }

    // Getters
    public Event getEvent() {
        return event;
    }

    public Customer getCustomer() {
        return customer;
    }

    public TicketType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void printTicketInfo() {
        System.out.println("Ticket for: " + event.getName());
        System.out.println("Customer: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("Type: " + type);
        System.out.println("Quantity: " + quantity);
        System.out.println("Total Cost: $" + getTotalCost());
    }
 
}
