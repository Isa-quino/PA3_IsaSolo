import java.util.Scanner;
/**
 * Command that initiates the ticket purchasing process for a customer.
 * It implements the Command interface and delegates the purchase logic
 * to the system controller through execution.
 */
public class PurchaseTicketCommand implements Command{
    // attributes
    private Customer customer;
    private Event event;
    private TicketType type;
    private int quantity;
    private Scanner scanner;

    // constructors
    public PurchaseTicketCommand() {}

    public PurchaseTicketCommand(Customer customer, Event event, TicketType type, int quantity) {
        this.customer = customer;
        this.event = event;
        this.type = type;
        this.quantity = quantity;
    }

    public PurchaseTicketCommand(Customer customer, Scanner scanner) {
        this.customer = customer;
        this.scanner = scanner;
    }

    // Executes the purchase ticket command.
    @Override
    public void execute() {
        RunTicket.purchaseTicket(customer, scanner);
    }
}
