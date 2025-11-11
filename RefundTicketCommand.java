import java.util.List;
import java.util.Scanner;

/**
 * This command processes a ticket refund request for a customer
 * It implements the Command interface and interacts with the customer's
 * invoice history to indentify and refund a selected ticket purchase.
 * 
 * It handles interactive input with Scanner and handles validation,
 * refund logic, and "wallet crediting".
 */

public class RefundTicketCommand implements Command {
    private Customer customer;
    private Scanner scanner;

    public RefundTicketCommand(Customer customer, Scanner scanner) {
        this.customer = customer;
        this.scanner = scanner;
    }

    /**
     * Executes the refund process.
     * It displays the customers invoices, prompts for an invoice ID,
     * validates the selection, and credits the refund amount to the
     * customer's wallet (bank account).
     * If there are no invoices or the input is not valid, the refund gets cancelled
     */
    @Override
    public void execute() {
        List<Invoice> invoices = customer.getInvoices();
        if (invoices.isEmpty()) {
            System.out.println("You have no invoices to refund.");
            return;
        }

        System.out.println("Your Invoices:");
        for (Invoice invoice : invoices) {
            System.out.println("ID: " + invoice.getInvoiceId() +
                               " | Event: " + invoice.getEvent() +
                               " | Type: " + invoice.getTicketType() +
                               " | Qty: " + invoice.getQuantity() +
                               " | Total: $" + String.format("%.2f", invoice.getTotalCost()));
        }

        System.out.print("Enter the Invoice ID to refund: ");
        String input = scanner.nextLine().trim();

        int invoiceId;
        try {
            invoiceId = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Refund cancelled.");
            return;
        }

        Invoice selected = null;
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceId() == invoiceId) {
                selected = invoice;
                break;
            }
        }

        if (selected == null) {
            System.out.println("Invoice not found.");
            return;
        }

        double refundAmount = selected.getTotalCost();
        customer.getBankAccount().deposit(refundAmount);
        customer.getInvoices().remove(selected);
        // keep CSV-visible balance in sync with wallet
        customer.setMoneyAvailable(customer.getBankAccount().getBalance());

        System.out.println("Refund successful! $" + String.format("%.2f", refundAmount) + " credited to your wallet.");
    }
}