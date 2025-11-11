import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer in the TicketMiner system.
 * Stores personal information, account balance, membership status,
 * login credentials, and ticket purchase history.
 * 
 * Provides methods for validating credentials, managing invoices,
 * handling ticket transactions, and printing customer details.
 */

public class Customer {
    // Attributes
    private int customerId;
    private String firstName;
    private String lastName;
    private double moneyAvailable;
    private int concertsPurchased;
    private boolean hasMembership;
    private String username;
    private String password;
    private BankAccount bankAccount;

    private List<Invoice> invoices = new ArrayList<>();
    private List<Ticket> purchasedTickets = new ArrayList<>();
    private double totalSaved; // NEW: track member savings

    // Constructors
    public Customer() {}

    public Customer(int customerId, String firstName, String lastName, double moneyAvailable, int concertsPurchased,
            boolean hasMembership, String username, String password) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.moneyAvailable = moneyAvailable;
        this.concertsPurchased = concertsPurchased;
        this.hasMembership = hasMembership;
        this.username = username;
        this.password = password;
    }

    // Getters & Setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public double getMoneyAvailable() { return moneyAvailable; }
    public void setMoneyAvailable(double moneyAvailable) { this.moneyAvailable = moneyAvailable; }
    public int getConcertsPurchased() { return concertsPurchased; }
    public void setConcertsPurchased(int concertsPurchased) { this.concertsPurchased = concertsPurchased; }
    public boolean getHasMembership() { return hasMembership; }
    public void setHasMembership(boolean hasMembership) { this.hasMembership = hasMembership; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public BankAccount getBankAccount() { return this.bankAccount; }
    public void setBankAccount(BankAccount account) { this.bankAccount = account; }

    public boolean checkValidUsername(String username) {
        if (username == null || username.length() < 6 || username.contains(" ")) return false;
        return true;
    }
    public boolean passwordValidation(String pw) {
        if (pw == null || pw.length() < 8) return false;
        return true;
    }

    public void printMembershipInfo() {
        System.out.println("//////////////////////////// MINER MEMBERSHIP DETAILS ////////////////////////////");
        System.out.println("If you purchase a Miner Membership with us, you will have the following perks: ");
        System.out.println("  * Getting 10% off on ALL tickets");
        System.out.println("  * Getting 5% off on food");
        System.out.println("//////////////////////////////////////////////////////////////////////////////////");
    }

    public void addInvoice(Invoice invoice) { invoices.add(invoice); }
    public List<Ticket> getPurchasedTickets() { return purchasedTickets; }
    public void setPurchasedTickets(List<Ticket> tickets) { this.purchasedTickets = tickets; }

    // Used in PurchaseTicketCommand
    public void purchaseTicket(Event event, TicketType type, int quantity, double totalCost) {
        moneyAvailable -= totalCost;
        concertsPurchased += quantity;

        Ticket ticket = new Ticket(event, type, quantity, totalCost);
        purchasedTickets.add(ticket);
        event.addTickets(purchasedTickets);
    }

    // Used in RefundTicketCommand
    public void refundTicket(Ticket ticket) {
        moneyAvailable += ticket.getTotalCost();
        concertsPurchased -= ticket.getQuantity();
        purchasedTickets.remove(ticket);
    }

    // FOR TESTING ONLY
    public void printCustomerInfo() {
        System.out.println("ID: " + customerId);
        System.out.println("First name: " + firstName);
        System.out.println("Last name: " + lastName);
        System.out.println("Money Available: " + moneyAvailable);
        System.out.println("Concerts Purchased: " + concertsPurchased);
        System.out.println("Has Membership?: " + hasMembership);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
    }

    public List<Invoice> getInvoices() { return this.invoices; }

    // NEW: savings tracking
    public void addSavings(double amount){
        if (amount > 0) totalSaved += amount;
    }
    public double getTotalSaved(){
        return totalSaved;
    }
}