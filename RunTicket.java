import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/** JAVADOC
 * Main controller class for the TicketMiner system.
 */
public class RunTicket {

    // FOR COLORS //
    public static final String reset = "\u001B[0m"; //RESET (clears formatting)
    public static final String bold = "\u001B[1m"; //BOLD
    public static final String orange = "\u001B[38;5;208m"; //ORANGE
    public static final String blue = "\u001B[34m"; //BLUE

    // LISTS //
    public static List<Customer> customerList = new ArrayList<>();
    public static List<Event> eventList = new ArrayList<>();
    public static List<String> logList = new ArrayList<>();

    // SCANNER //
    private static Scanner scanner = new Scanner(System.in);

    ////////////////////////////////////////// MAIN //////////////////////////////////////////
    public static void main(String[] args) {
        readEventList("EventListPA3(Sheet1).csv");
        readCustomerList("CustomerListPA3(Sheet1).csv");

        boolean active = true;
        boolean entireExit = false;

        System.out.println(bold + "\n                 Welcome to " + blue + "Ticket " + orange + "Miner\n" + reset);

        while (active && !entireExit) {
            System.out.println("\n/////////////////////////////////////////////////////////");
            System.out.println("     Are you a System Administrator or a Customer?\n");
            System.out.println("(1) Customer");
            System.out.println("(2) System Administrator");
            System.out.println("(3) EXIT\n");
            System.out.println("/////////////////////////////////////////////////////////");

            System.out.print("Enter your choice (or type EXIT): ");
            String raw = scanner.nextLine().trim();
            if (raw.equalsIgnoreCase("EXIT")) { entireExit = true; break; }
            int roleChoice;
            try { roleChoice = Integer.parseInt(raw); }
            catch (NumberFormatException nfe){ System.out.println("Invalid input."); continue; }

            switch (roleChoice) {
                case 1: // CUSTOMER
                    boolean customerMenuActive = true;
                    while (customerMenuActive && !entireExit) {
                        System.out.println("\n/////////////////////////////////////////////////////////");
                        System.out.println("                   Welcome Customer!\n");
                        System.out.println("(1) Register");
                        System.out.println("(2) Log in");
                        System.out.println("(3) Back to Main Menu");
                        System.out.println("(4) Exit Entire Program");
                        System.out.println("\n/////////////////////////////////////////////////////////");

                        int custChoice = inputMismatchInt(scanner, "Enter your choice: ");
                        scanner.nextLine();

                        switch (custChoice) {
                            case 1: // REGISTER
                                registerCustomer(scanner);
                                break;

                            case 2: // LOG IN
                                Customer c = customerLogIn();
                                if (c != null) {
                                    boolean loggedInMenuActive = true;
                                    while (loggedInMenuActive && !entireExit) {
                                        showCustomerMenu(c);
                                        // showCustomerMenu returns after logout/exit
                                        loggedInMenuActive = false;
                                    }
                                }
                                break;

                            case 3: // BACK TO MAIN MENU
                                customerMenuActive = false;
                                break;

                            case 4: // EXIT ENTIRE PROGRAM
                                entireExit = true;
                                break;

                            default:
                                System.out.println("ERROR: Invalid Input\nPlease choose an option (1-4)");
                                break;
                        }
                    }
                    break; // break case 1 (customer)

                case 2: // SYSTEM ADMIN (no login required)
                    boolean adminMenuActive = true;
                    while (adminMenuActive && !entireExit) {
                        showAdminMenu();
                        int adminChoice = inputMismatchInt(scanner, "Enter your choice: ");
                        scanner.nextLine();

                        switch (adminChoice) {
                            case 1: // VIEW ALL EVENTS
                                viewAllEvents();
                                break;

                            case 2: // SEARCH BY ID (concise for customer-style view)
                                searchEventByIdCustomer(scanner);
                                break;
                            
                            case 3: // ADD / EDIT / DELETE
                                System.out.println("\n//////////////////// EVENT MANAGEMENT /////////////////////");
                                System.out.println("(1) Add Event");
                                System.out.println("(2) Edit Event");
                                System.out.println("(3) Delete Event");
                                System.out.println("(4) Back to Admin Menu");
                                System.out.println("///////////////////////////////////////////////////////////");

                                int subChoice = inputMismatchInt(scanner, "Enter your choice: ");
                                scanner.nextLine();

                                switch (subChoice) {
                                    case 1: // ADDING EVENT
                                        Command addEvent = new AddEventCommand(scanner);
                                        new MenuController().handleCommand(addEvent);
                                        break;
                                    case 2: //EDITING EVENT (not using command)
                                        editEvent(scanner);
                                        break;
                                    case 3: // DELETING EVENT
                                        Command deleteEvent = new DeleteEventCommand(scanner);
                                        new MenuController().handleCommand(deleteEvent);
                                        break;
                                    case 4:
                                        break;
                                    default:
                                        System.out.println("ERROR: Invalid Input\nPlease choose an option (1-4)");
                                }

                                break;
                            
                            case 4: // BACK TO MAIN MENU
                                adminMenuActive = false;
                                break;

                            case 5: // PROCESS AUTO-PURCHASE CSV
                                // runs "Autopurchase.csv" in the same folder by default
                                processAutoPurchaseCsv("Autopurchase.csv");
                                break;
                            
                            case 6: // ENTIRE EXIT
                                entireExit = true;
                                break;
                            
                            default:
                                System.out.println("ERROR: Invalid Input\nPlease choose an option (1-6)");
                                break;
                        }
                        
                    }
                    break; // break case 2 (admin)

                case 3: // EXIT
                    active = false;
                    break;

                default:
                    System.out.println("ERROR: Invalid Input\nPlease choose an option (1-3)");
            }
        }

        // write updated data on exit per spec
        CsvFileMaker.saveEventsToCSV(eventList, "UpdatedEventList.csv");
        CsvFileMaker.saveCustomersToCSV(customerList, "UpdatedCustomerList.csv");
        saveLogToList();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    // Flexible CSV reader for Event list (any column order)
    public static void readEventList(String fileName) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mm a");
        try {
            List<CsvFlex.Row> rows = CsvFlex.read(fileName);
            for (CsvFlex.Row r : rows) {
                try {
                    String timeStr  = firstNonEmpty(r, "Time","Event Time");
                    String dateStr  = firstNonEmpty(r, "Date","Event Date");
                    String eType    = firstNonEmpty(r, "Type","Event Type");
                    String name     = firstNonEmpty(r, "Name","Event Name");
                    String venueStr = firstNonEmpty(r, "Venue","Venue Name");

                    int eventId = CsvFlex.toInt(firstNonEmpty(r,"EventID","Event ID","ID"), Event.generateNextEventId());

                    double ga   = CsvFlex.toDouble(firstNonEmpty(r,"General","General Admission Price","GA Price"), 0);
                    double vip  = CsvFlex.toDouble(firstNonEmpty(r,"VIP","VIP Price"), 0);
                    double gold = CsvFlex.toDouble(firstNonEmpty(r,"Gold","Gold Price"), 0);
                    double silv = CsvFlex.toDouble(firstNonEmpty(r,"Silver","Silver Price"), 0);
                    double bron = CsvFlex.toDouble(firstNonEmpty(r,"Bronze","Bronze Price"), 0);

                    int cap     = CsvFlex.toInt(firstNonEmpty(r,"Capacity"), 0);
                    int vipPct  = CsvFlex.toInt(firstNonEmpty(r,"VIP %","VIP Pct"), 5);
                    int goldPct = CsvFlex.toInt(firstNonEmpty(r,"Gold %","Gold Pct"), 10);
                    int silPct  = CsvFlex.toInt(firstNonEmpty(r,"Silver %","Silver Pct"), 15);
                    int bronPct = CsvFlex.toInt(firstNonEmpty(r,"Bronze %","Bronze Pct"), 20);
                    int gaPct   = CsvFlex.toInt(firstNonEmpty(r,"General %","General Pct"), 45);
                    int resPct  = CsvFlex.toInt(firstNonEmpty(r,"Reserved Extra %","Reserved Extra Pct"), 5);

                    boolean fireworks = CsvFlex.toBool(firstNonEmpty(r,"Fireworks","Fireworks Planned"), false);
                    double fireworksCost = CsvFlex.toDouble(firstNonEmpty(r,"Fireworks Cost"), 0);
                    double venueCost     = CsvFlex.toDouble(firstNonEmpty(r,"Cost","Venue Cost"), 0);

                    LocalTime time = (timeStr.isEmpty()? LocalTime.of(19,0) : LocalTime.parse(timeStr, timeFormat));
                    LocalDate date = (dateStr.isEmpty()? LocalDate.now() : LocalDate.parse(dateStr, dateFormat));

                    Venue venue = VenueFactory.createVenue(venueStr);

                    int vipAvail = (int)(cap * vipPct / 100.0);
                    int goldAvail = (int)(cap * goldPct / 100.0);
                    int silAvail  = (int)(cap * silPct / 100.0);
                    int bronAvail = (int)(cap * bronPct / 100.0);
                    int gaAvail   = (int)(cap * gaPct / 100.0);

                    Event ev = new Event(eventId, eType, name, date, time,
                            vip, gold, silv, bron, ga,
                            vipAvail, goldAvail, silAvail, bronAvail, gaAvail,
                            venueStr, venue, cap, venueCost, fireworks, fireworksCost);
                    eventList.add(ev);
                    System.out.println("Loaded event: " + eventId + " - " + name);
                } catch (Exception perRow) {
                    System.out.println("ERROR parsing row (skipped): " + perRow.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File " + fileName + " not found!");
        }
    } // end readEventList

    // Reads Customer List csv File (flexible two layouts)
    public static void readCustomerList(String fileName) {
        try (Scanner scanner = new Scanner(new File(fileName), "UTF-8")) {
            boolean firstLine = true;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                if (firstLine) { firstLine = false; continue; }

                String[] p = line.split(",", -1);
                if (p.length < 8) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }

                try {
                    int id = Integer.parseInt(p[0].trim());
                    String first = p[1].trim();
                    String last  = p[2].trim();
                    double money = Double.parseDouble(p[3].trim());
                    int concerts = Integer.parseInt(p[4].trim());
                    boolean hasMem = Boolean.parseBoolean(p[5].trim());
                    String user = p[6].trim();
                    String pass = p[7].trim();

                    Customer c = new Customer(id, first, last, money, concerts, hasMem, user, pass);
                    c.setPurchasedTickets(new ArrayList<>());
                    customerList.add(c);
                    continue;
                } catch (Exception ignore) { }

                try {
                    String last  = p[0].trim();
                    double money = Double.parseDouble(p[1].trim());
                    String user  = p[2].trim();
                    int id       = Integer.parseInt(p[3].trim());
                    String pass  = p[4].trim();
                    boolean hasMem = Boolean.parseBoolean(p[5].trim());
                    int concerts = Integer.parseInt(p[6].trim());
                    String first = p[7].trim();

                    Customer c = new Customer(id, first, last, money, concerts, hasMem, user, pass);
                    c.setPurchasedTickets(new ArrayList<>());
                    customerList.add(c);
                } catch (Exception e2) {
                    System.out.println("ERROR: Bad number/format in line: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File " + fileName + " not found!");
        }
    }

    // CASE 1 - Views all events
    private static void viewAllEvents() {
        if(eventList.isEmpty()) {
            System.out.println("There are no current events.");
            return;
        }
        for(Event e : eventList) {
            e.printEventInfo();
        }
    }

    // CASE 2 - Search by EventId (admin style)
    private static void searchEventById(Scanner scanner) {
        int id = inputMismatchInt(scanner, "Enter an event ID to search: ");
        scanner.nextLine();
        boolean found = false;

        for(int i = 0; i < eventList.size(); i++) {
            Event e = eventList.get(i);
            if(e.getEventId() == id) {
                System.out.println("///////////////////////// EVENT (" + id + ") ////////////////////////");
                e.printEventInfo();
                logAction("Event ID " + id + " was searched.");
                found = true;
                break;
            }
        }
        if(!found) {
            System.out.println("Event NOT found.");
            logAction("Event ID " + id + " was not found when searched.");
        }
    }

    // Customer-facing: search and print concise event info
    private static void searchEventByIdCustomer(Scanner scanner) {
        int id = inputMismatchInt(scanner, "Enter an event ID to view: ");
        scanner.nextLine();
        Event found = null;
        for (Event e : eventList) {
            if (e.getEventId() == id) { found = e; break; }
        }
        if (found == null) {
            System.out.println("Event NOT found.");
            logAction("Customer viewed: Event ID " + id + " not found.");
            return;
        }
        System.out.println("//////////////////// EVENT (" + id + ") ////////////////////");
        try {
            // if you added a concise method
            found.printEventInfoCustomer();
        } catch (Throwable t) {
            // fallback to full info if concise not implemented
            found.printEventInfo();
        }
        logAction("Customer viewed event ID " + id + ".");
    }

    /**
     * Create a new event (console)
     */
    public static Event addEvent(Scanner scanner) {

        int newEventId = Event.generateNextEventId(); // auto-generated ID

        System.out.println("Enter the type of Event (Concert / Festival / Sport): ");
        String eType;
        List<String> validETypes = Arrays.asList("Concert", "Festival", "Sport");
        while (true) {
            eType = scanner.nextLine().trim();
            boolean matchFound = false;
            for (String type : validETypes) {
                if (type.equalsIgnoreCase(eType)) {
                    eType = type; // normalize
                    matchFound = true;
                    break;
                }
            }
            if (matchFound) break;
            System.out.println("Invalid Event type. Please choose from Concert, Festival, Sport.");
        }

        System.out.println("Enter the name: ");
        String eName = scanner.nextLine();

        System.out.println("Enter the date in the following format (MM/DD/YYYY): ");
        LocalDate eDate = inputValidDate(scanner);

        System.out.println("Enter the time of the event in the following format (4:00 PM): ");
        LocalTime eTime = inputValidTime(scanner);

        double gaPrice;
        while (true) {
            System.out.println("Enter the General Admission Price (MAX: $4000): ");
            gaPrice = inputMismatchDOuble(scanner, " ");
            if (gaPrice <= 4000) break;
            System.out.println("Price exceeds MAX.");
        }

        VenueFactory.printVenueOptions();

        String venueName = null;
        while (venueName == null) {
            int userChoice = inputMismatchInt(scanner, "Enter your choice (1-5): ");
            scanner.nextLine();
            if (userChoice >= 1 && userChoice <= VenueFactory.venueNames.size()) {
                venueName = VenueFactory.venueNames.get(userChoice - 1);
            } else {
                System.out.println("Invalid Choice.");
            }
        }
        Venue venue = VenueFactory.createVenue(venueName);

        System.out.println("Will the event include Fireworks? (yes/no): ");
        boolean hasFireworks = scanner.nextLine().equalsIgnoreCase("yes");

        Event newEvent = new Event();
        newEvent.setEventId(newEventId);
        newEvent.setEventType(eType);
        newEvent.setName(eName);
        newEvent.setDate(eDate);
        newEvent.setTime(eTime);
        newEvent.setVenue(venue);
        newEvent.computePricesFromGA(gaPrice);
        newEvent.computeAvailabilityFromVenue();
        newEvent.setFireworks(hasFireworks);

        eventList.add(newEvent);
        CsvFileMaker.saveEventsToCSV(eventList, "UpdatedEventList.csv");

        System.out.println("New Event Added. See you soon!");
        logAction("Event [" + newEventId + "] [" + eName + "] was added to the list.");

        return newEvent;
    }

    //Will be used to edit an event
    private static void editEvent(Scanner scanner) {
        int id = inputMismatchInt(scanner, "Enter the Event ID you want to edit: ");

        Event event = null;
        for(int i = 0; i < eventList.size(); i++) {
            Event e = eventList.get(i);
            if(e.getEventId() == id) {
                event = e;
                break;
            }
        }

        if(event == null) {
            System.out.println("Event not found.");
            return;
        }

        System.out.println("Editing event: ");
        event.printEventInfo();

        System.out.println("///////////////////////////////////////////////////////");
        System.out.println("What would you like to edit?");
        System.out.println("(1) Event Name");
        System.out.println("(2) Event Date");
        System.out.println("(3) Event Time");
        System.out.println("(4) Ticket Price");
        System.out.println("(5) Cancel");
        System.out.println("///////////////////////////////////////////////////////");

        int choice = inputMismatchInt(scanner, " ");

        switch(choice) {
            case 1:
                scanner.nextLine();
                System.out.println("Enter the new event name: ");
                String newName = scanner.nextLine();
                event.setName(newName);
                System.out.println("Event name updated.");
                break;
            case 2:
                scanner.nextLine();
                System.out.println("Enter a new date (MM/DD/YYYY): ");
                String dateInput = scanner.nextLine().trim();
                try {
                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yyyy");
                    LocalDate newDate = LocalDate.parse(dateInput, dateFormat);
                    event.setDate(newDate);
                    System.out.println("Event date updated to " + newDate.format(dateFormat));
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please use MM/DD/YYYY (e.g., 9/4/2025).");
                }
                break;
            case 3:
                scanner.nextLine();
                System.out.print("Enter new time (e.g., 6:30 PM): ");
                String timeInput = scanner.nextLine().trim();
                try {
                    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mm a");
                    LocalTime newTime = LocalTime.parse(timeInput, timeFormat);
                    event.setTime(newTime);
                    System.out.println("Event time updated to " + newTime.format(timeFormat));
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid time format. Please use h:mm AM/PM (e.g., 6:30 PM).");
                }
                break;
            case 4:
                System.out.println("Chose a Ticket Price to edit: ");
                System.out.println("(1) VIP Price");
                System.out.println("(2) GOLD Price");
                System.out.println("(3) SILVER Price");
                System.out.println("(4) BRONZE Price");
                System.out.println("(5) General Admission Price");
                System.out.println("(6) Cancel");
                int priceChoice = inputMismatchInt(scanner, " ");
                switch(priceChoice) {
                    case 1:
                        scanner.nextLine();
                        double newVIPPrice = inputMismatchDOuble(scanner, "Enter the new ticket price for VIP: ");
                        event.setVipPrice(newVIPPrice);
                        System.out.println("VIP ticket price updated.");
                        break;
                    case 2:
                        scanner.nextLine();
                        double newGoldPrice = inputMismatchDOuble(scanner, "Enter the new ticket price for GOLD: ");
                        event.setGoldPrice(newGoldPrice);
                        System.out.println("GOLD ticket price updated.");
                        break;
                    case 3:
                        scanner.nextLine();
                        double newSilverPrice = inputMismatchDOuble(scanner, "Enter the new ticket price for SILVER: ");
                        event.setSilverPrice(newSilverPrice);
                        System.out.println("Silver ticket price updated.");
                        break;
                    case 4:
                        scanner.nextLine();
                        double newBronzePrice = inputMismatchDOuble(scanner, "Enter the new ticket price for BRONZE: ");
                        event.setBronzePrice(newBronzePrice);
                        System.out.println("VIP ticket price updated.");
                        break;
                    case 5:
                        scanner.nextLine();
                        double newGeneralAdmissionPrice = inputMismatchDOuble(scanner, "Enter the new ticket price for General Admission: ");
                        event.setGeneralAdmissionPrice(newGeneralAdmissionPrice);
                        System.out.println("General Admission ticket price updated.");
                        break;
                    case 6:
                        System.out.println("Edit cancelled.");
                        break;
                    default:
                        System.out.println("Invalid choice");
                        System.out.println();
                }
            case 5:
                System.out.println("Edit cancelled");
                break;
            default:
                System.out.println("Invalid choice");
                System.out.println();
        }

        logAction("Event [" + event.getName() + "] was edited at " + event.getFormattedDate());
    }

    // CASE 3 - Delete an event
    public static void deleteEventByID(Scanner scanner) {
        int id = inputMismatchInt(scanner, "Enter an event ID to delete the event: ");
        boolean found = false;

        for(int i = 0; i < eventList.size(); i++) {
            Event e = eventList.get(i);

            if(e.getEventId() == id) {
                eventList.remove(i);
                System.out.println("Event deleted from system!");
                logAction("Event " + id + " was deleted from the system.");
                found = true;
                break;
            }
        }
        if(!found) {
            System.out.println("Event " + id + " not found.");
            logAction("Event " + id + " was not found");
        }
    }

    // upgrades the user to have membership
    public static void upgradeToMembership(Customer customer, Scanner scanner) {
        if (customer.getHasMembership()) {
            System.out.println("You are already a member. You can't upgrade again.");
            return;
        }

        System.out.println("Would you like to view the perks of having a Miner Membership with us? (yes/no)");
        String viewMembershipInfo = scanner.nextLine().trim().toLowerCase();

        if (viewMembershipInfo.equals("yes")) {
            customer.printMembershipInfo();

            System.out.println("\nWould you like to buy the Miner Membership for only $25 extra? (yes/no): ");
            String membershipInput = scanner.nextLine().trim().toLowerCase();

            if (membershipInput.equals("yes")) {
                try {
                    ensureBankAccount(customer);
                    customer.getBankAccount().withdraw(25);
                    customer.setHasMembership(true);
                    System.out.println("You are now a Miner Member! Enjoy your perks.");
                    logAction("Customer [" + customer.getUsername() + "] upgraded to Miner Membership.");
                } catch (InsufficientFundsException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Membership upgrade failed due to insufficient funds.");
                } catch (Exception e) {
                    System.out.println("Unexpected error: " + e.getMessage());
                }
            } else {
                System.out.println("No problem! You can upgrade anytime :)");
            }
        }
    }

    // ensure a wallet exists before any purchase/upgrade
    private static void ensureBankAccount(Customer c) {
        if (c.getBankAccount() == null) {
            c.setBankAccount(new BankAccount(c.getUsername() + "_wallet", c.getMoneyAvailable()));
        }
    }

    // Registers a customer
    private static void registerCustomer(Scanner scanner) {
        String username;
        String pw;
        Customer temp = new Customer();

        // Username validation
        while (true) {
            System.out.println("Enter a Username (Must be 6 characters or longer): ");
            username = scanner.nextLine().trim();

            if (!temp.checkValidUsername(username)) {
                System.out.println("Invalid format, please try again.");
            } else if (isUsernameTaken(username)) {
                System.out.println("Username already taken.");
            } else {
                break;
            }
        }

        // Password validation
        while (true) {
            System.out.println("Enter a password (Must be 8 characters or longer): ");
            pw = scanner.nextLine();

            if (!temp.passwordValidation(pw)) {
                System.out.println("Invalid password, please try again.");
            } else {
                break;
            }
        }

        temp.setCustomerId(0);

        System.out.println("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.println("Enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.println("Enter bank account name: ");
        String accountName = scanner.nextLine();

        double initialBalance = inputMismatchDOuble(scanner, "Enter initial deposit amount: ");
        BankAccount account = new BankAccount(accountName, initialBalance);

        Customer newCustomer = new Customer(0, firstName, lastName, initialBalance, 0, false, username, pw);
        newCustomer.setBankAccount(account);

        // Offer Miner Membership
        upgradeToMembership(newCustomer, scanner);

        // Add to list
        customerList.add(newCustomer);
        CsvFileMaker.saveCustomersToCSV(customerList, "UpdatedCustomerList.csv");

        System.out.println("Registration Complete. Welcome user " + username + "!");
        logAction("Customer registered: [ " + username + " ] at " + new Date());
    }

    private static Customer customerLogIn() {
        final int MAX_TRIES = 5; // safety cap + allow "BACK"
        int tries = 0;
        try {
            while(tries < MAX_TRIES) {
                System.out.println("Enter your username (or type BACK to return): ");
                String userInput = scanner.nextLine().trim();
                if (userInput.equalsIgnoreCase("BACK")) {
                    logAction("Login cancelled by user.");
                    return null;
                }

                System.out.println("Enter your password: ");
                String pwInput = scanner.nextLine();

                for (Customer c: customerList) {
                    if (c.getUsername().equalsIgnoreCase(userInput.trim()) && c.getPassword().equals(pwInput.trim())) {
                        System.out.println("Log in successful! Welcome O . O ");
                        logAction("Customer: [" + c.getUsername() + "] logged in at " + new Date());
                        ensureBankAccount(c);
                        return c;
                    }
                }
                tries++;
                System.out.println("Log in failed. Try again or type BACK to return.");
            }
            System.out.println("Too many failed attempts. Returning to previous menu.");
            logAction("Login failed too many times.");
        } catch (InputMismatchException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * User will be able to purchase tickets (used by PurchaseTicketCommand)
     */
    public static void purchaseTicket(Customer customer, Scanner scanner) {
        viewAllEvents(); // Show available events

        System.out.print("Enter Event ID to purchase tickets for: ");
        int eventId = scanner.nextInt();
        scanner.nextLine();

        Event event = findEventById(eventId);
        if (event == null) {
            System.out.println("Event not found.");
            logAction("Failed ticket purchase: Event ID " + eventId + " not found.");
            return;
        }

        System.out.println("Available Ticket Types:");
        System.out.println("(1) VIP - $" + event.getVipPrice());
        System.out.println("(2) Gold - $" + event.getGoldPrice());
        System.out.println("(3) Silver - $" + event.getSilverPrice());
        System.out.println("(4) Bronze - $" + event.getBronzePrice());
        System.out.println("(5) General Admission - $" + event.getGeneralAdmissionPrice());

        System.out.print("Choose ticket type (1 - 5): ");
        int typeChoice = scanner.nextInt();
        scanner.nextLine();

        TicketType type;
        try {
            type = TicketType.fromInt(typeChoice);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid ticket type.");
            logAction("Failed ticket purchase: Invalid ticket type selected.");
            return;
        }

        System.out.print("How many tickets? (1 - 6): ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        if (quantity < 1 || quantity > 6) {
            System.out.println("Invalid quantity. Must be between 1 and 6.");
            logAction("Failed ticket purchase: Invalid quantity " + quantity);
            return;
        }

        // hand off to shared purchase flow
        checkoutAndRecord(customer, event, type, quantity);
    }

    // --- shared purchase flow (manual + autopurchase) ---
    private static void checkoutAndRecord(Customer customer, Event event, TicketType type, int quantity) {
        final double texasTaxRate = 0.0825;

        // always require wallet for purchases in this system
        ensureBankAccount(customer);

        double basePrice = event.getPriceByType(type);
        boolean isMember = customer.getHasMembership();
        double discountedPrice = isMember ? basePrice * 0.90 : basePrice; // 10% discount for members only

        double subTotal = discountedPrice * quantity;
        double taxAmount = subTotal * texasTaxRate;
        double totalCost = Math.floor((subTotal + taxAmount) * 100.0) / 100.0; // round down to nearest cent

        BankAccount account = customer.getBankAccount();
        try {
            account.withdraw(totalCost);
            // create & attach invoice
            int invoiceId = Invoice.generateInvoiceId();
            Invoice invoice = new Invoice(customer, event, type, quantity, discountedPrice, totalCost, isMember, invoiceId);
            customer.addInvoice(invoice);

            // create tickets and attach to event
            List<Ticket> tickets = new ArrayList<>();
            for (int i = 0; i < quantity; i++) {
                tickets.add(new Ticket(event, type, discountedPrice));
            }
            event.addTickets(tickets);

            // availability + accounting (wrapped in try in case methods not present)
            try { event.decrementAvailability(type, quantity); } catch (Throwable ignored) {}
            double discountGiven = (basePrice - discountedPrice) * quantity;
            if (discountGiven > 0) {
                try { event.addDiscount(discountGiven); } catch (Throwable ignored) {}
                try { customer.addSavings(discountGiven); } catch (Throwable ignored) {}
            }
            try { event.addTax(taxAmount); } catch (Throwable ignored) {}

            // keep CSV-visible fields current
            try { customer.setMoneyAvailable(account.getBalance()); } catch (Throwable ignored) {}
            try { customer.setConcertsPurchased(customer.getConcertsPurchased() + quantity); } catch (Throwable ignored) {}

            System.out.println("\n//////////////////////// INVOICE ///////////////////////");
            System.out.println("Purchase successful! Invoice generated:");
            System.out.println(invoice);
            System.out.println("///////////////////////////////////////////////////////");

            // Receipt on success
            try { ReceiptUtil.writePurchaseReceipt(customer, invoice); } catch (Throwable ignored) {}

            logAction("Invoice #" + invoiceId + " created for user " + customer.getUsername());
        } catch (InsufficientFundsException e) {
            System.out.println("Transaction failed: " + e.getMessage());
            logAction("Failed ticket purchase for user [" + customer.getUsername() + "]: " + e.getMessage());
        }
    }

    ////////////////////////////////////////// HELPER METHODS //////////////////////////////////////////

    private static Event findEventById(int eventId) {
        for (Event e : eventList) {
            if (e.getEventId() == eventId) return e;
        }
        return null;
    }

    public static boolean eventIdExists(int eventId) {
        for (Event e : eventList) {
            if (e.getEventId() == eventId) return true;
        }
        return false;
    }

    public static int inputMismatchInt(Scanner scanner, String message) {
        while(true) {
            try {
                System.out.println(message);
                return scanner.nextInt();
            } catch(InputMismatchException e) {
                System.out.println("ERROR: Invalid input, Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    public static double inputMismatchDOuble(Scanner scanner, String message) {
        while(true) {
            try {
                System.out.println(message);
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e){
                System.out.println("ERROR: Invalid input, Please enter a number with decimal.");
                scanner.nextLine();
            }
        }
    }

    public static boolean isUsernameTaken(String newUsername) {
        for (Customer c : customerList) {
            if (c.getUsername().equalsIgnoreCase(newUsername)) return true;
        }
        return false;
    }

    // FOR CUSTOMERS ONLY
    public static void showCustomerMenu(Customer c) {
        boolean loggedIn = true;
        while(loggedIn) {
            System.out.println("\n///////////////////////// CUSTOMER MENU ////////////////////////");
            System.out.println("(1) View All Events");
            System.out.println("(2) Search Event By ID");
            System.out.println("(3) Purchase Tickets");
            System.out.println("(4) Refund Ticket(s)");
            System.out.println("(5) Become a Member");
            System.out.println("(6) Log Out");
            System.out.println("(7) Exit Entire Program");
            System.out.println("(8) Write My Receipt Summary");
            System.out.println("////////////////////////////////////////////////////////////////");

            int choice = inputMismatchInt(scanner, "Enter your choice: ");
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAllEvents();
                    break;
                case 2:
                    searchEventById(scanner);
                    break;
                case 3:
                    Command purchase = new PurchaseTicketCommand(c, scanner);
                    new MenuController().handleCommand(purchase);
                    break;
                case 4:
                    Command refund = new RefundTicketCommand(c, scanner);
                    new MenuController().handleCommand(refund);
                    break;
                case 5:
                    upgradeToMembership(c, scanner);
                    break;
                case 6:
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;
                case 7:
                    System.out.println("Exiting program...");
                    System.exit(0);
                    break;
                case 8:
                    ReceiptUtil.writeCustomerReceiptSummary(c);
                    System.out.println("Customer Receipt Summary written.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // FOR ADMINS ONLY
    public static void showAdminMenu() {
        System.out.println("\n/////////////////////// MAIN MENU ///////////////////////");
        System.out.println("(1) View All Events");
        System.out.println("(2) Search Event By ID");
        System.out.println("(3) Add Event / Edit Event / Delete Event");
        System.out.println("(4) Back to Main Menu");
        System.out.println("(5) Process Auto-Purchase (Autopurchase.csv)");
        System.out.println("(6) EXIT");
        System.out.println("/////////////////////////////////////////////////////////");
    }

    /////////////////////////////////////////////////////// LOGGING METHODS ///////////////////////////////////////////////////////
    private static void logAction(String message) { logList.add(message); }

    private static void saveLogToList() {
        try {
            FileWriter writer = new FileWriter("TicketMinerActionLog.txt");
            System.out.println("/////////////// Ticket Miner Action Log ///////////////\n");
            for(int i = 0; i < logList.size(); i++) {
                String log = logList.get(i);
                writer.write(log + "\n");
                System.out.println(log);
            }
            System.out.println("\n///////////////////////////////////////////////////////");
            writer.close();
        } catch(IOException e) {
            System.out.println("ERROR: Could not write logs: " + e.getMessage());
        }
    }

    /////////////////////////////////////////////////////// DATE & TIME ///////////////////////////////////////////////////////
    private static LocalDate inputValidDate(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Enter the date:");
                System.out.print("Month (1 - 12): ");
                int month = scanner.nextInt();

                System.out.print("Day (1 - 31): ");
                int day = scanner.nextInt();

                System.out.print("Year (e.g., 2025): ");
                int year = scanner.nextInt();
                scanner.nextLine(); // clear buffer

                return LocalDate.of(year, month, day);
            } catch (Exception e) {
                System.out.println("Invalid date. Please try again.");
                scanner.nextLine(); // clear bad input
            }
        }
    }

    private static LocalTime inputValidTime(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Enter the time:");
                System.out.print("Hour (1 - 12): ");
                int hour = scanner.nextInt();

                System.out.print("Minute (0 - 59): ");
                int minute = scanner.nextInt();

                System.out.print("AM or PM (1 for AM, 2 for PM): ");
                int amPm = scanner.nextInt();
                scanner.nextLine(); // clear buffer

                if (hour < 1 || hour > 12 || minute < 0 || minute > 59 || (amPm != 1 && amPm != 2)) {
                    throw new IllegalArgumentException();
                }

                if (amPm == 2 && hour != 12) hour += 12;
                if (amPm == 1 && hour == 12) hour = 0;

                return LocalTime.of(hour, minute);
            } catch (Exception e) {
                System.out.println("Invalid time. Please try again.");
                scanner.nextLine(); // clear bad input
            }
        }
    }

    // =========================
    // Auto-Purchase (CSV) — FLEXIBLE HEADERS, NAME-BASED MATCH ONLY
    // =========================
    private static void processAutoPurchaseCsv(String fileName) {
        File f = new File(fileName);
        if (!f.exists()) {
            System.out.println("Auto-purchase file not found: " + fileName);
            return;
        }

        int ok = 0, fail = 0, skipped = 0;

        try {
            List<CsvFlex.Row> rows = CsvFlex.read(fileName);
            if (rows.isEmpty()) {
                System.out.println("Empty autopurchase file.");
                return;
            }

            for (CsvFlex.Row row : rows) {
                try {
                    String action = firstNonEmpty(row, "Action","action","ACTION","Operation","Op");
                    if (action.isEmpty()) action = "Purchase";
                    if (!action.equalsIgnoreCase("Purchase")) { skipped++; continue; }

                    // Resolve an existing customer strictly by username/email/First+Last
                    Customer c = resolveCustomerFromRow(row);
                    if (c == null) {
                        System.out.println("AutoPurchase SKIP (user not found)");
                        skipped++;
                        continue;
                    }
                    ensureBankAccount(c);

                    int eventId = CsvFlex.toInt(firstNonEmpty(row,
                            "Event ID","event id","EventId","EventID","ID","Event"), -1);
                    if (eventId <= 0) { System.out.println("AutoPurchase SKIP (bad event id)"); skipped++; continue; }
                    Event e = findEventById(eventId);
                    if (e == null) { System.out.println("AutoPurchase SKIP (event not found): " + eventId); skipped++; continue; }

                    int qty = CsvFlex.toInt(firstNonEmpty(row,
                            "Ticket Quantity","ticket quantity","Qty","Quantity","Count","#"), -1);
                    if (qty < 1 || qty > 6) { System.out.println("AutoPurchase SKIP (qty out of range 1-6): " + qty); skipped++; continue; }

                    String typeRaw = firstNonEmpty(row, "Ticket Type","ticket type","Type","Seat","Level","Category");
                    TicketType tt = parseTicketTypeFlexible(typeRaw);

                    try {
                        if (!e.hasAvailableTickets(tt, qty)) {
                            System.out.println("AutoPurchase FAIL (insufficient availability)");
                            fail++;
                            continue;
                        }
                    } catch (Throwable ignored) {}

                    // Funds pre-check (wallet required)
                    final double texasTaxRate = 0.0825;
                    double base = e.getPriceByType(tt);
                    boolean member = c.getHasMembership();
                    double unit = member ? base * 0.90 : base;
                    double sub = unit * qty;
                    double tax = sub * texasTaxRate;
                    double total = Math.floor((sub + tax) * 100.0) / 100.0;

                    if (c.getBankAccount().getBalance() + 1e-9 < total) {
                        System.out.println("AutoPurchase FAIL (insufficient funds) for " + c.getUsername());
                        fail++;
                        continue;
                    }

                    // Do the purchase (updates accounting + writes receipt)
                    checkoutAndRecord(c, e, tt, qty);
                    ok++;

                } catch (Exception ex) {
                    System.out.println("AutoPurchase ROW SKIPPED due to error: " + ex.getMessage());
                    skipped++;
                }
            }

            System.out.println("AutoPurchase done. OK=" + ok + " FAIL=" + fail + " SKIPPED=" + skipped);
            logAction("AutoPurchase run complete. OK=" + ok + " FAIL=" + fail + " SKIPPED=" + skipped);

        } catch (Exception e) {
            System.out.println("Auto-purchase error: " + e.getMessage());
        }
    }

    // Helper to pick the first non-empty field name from a row
    private static String firstNonEmpty(CsvFlex.Row row, String... candidates) {
        for (String c : candidates) {
            if (row.has(c)) {
                String v = row.get(c);
                if (v != null && !v.trim().isEmpty()) return v.trim();
            }
        }
        return "";
    }

    // Resolve a Customer using username/email if present, else by First+Last (no guest creation)
    private static Customer resolveCustomerFromRow(CsvFlex.Row row) {
        // 1) Exact username
        String uname = firstNonEmpty(row, "Username", "username", "User", "user", "Login", "login");
        if (!uname.isEmpty()) {
            Customer c = findCustomerByUsername(uname);
            if (c != null) return c;
        }

        // 2) Email -> local-part
        String email = firstNonEmpty(row, "Email", "email", "E-mail", "Contact", "Contact Email");
        if (!email.isEmpty() && email.contains("@")) {
            String local = email.substring(0, email.indexOf('@')).trim();
            Customer c = findCustomerByUsername(local);
            if (c != null) return c;
        }

        // 3) Combined name field
        String combined = firstNonEmpty(row, "Buyer","buyer","Name","name","Customer","customer","Full Name","full name");
        if (!combined.isEmpty()) {
            String[] fl = splitNameSmart(combined);
            Customer c = findCustomerByName(fl[0], fl[1]);
            if (c != null) return c;
        }

        // 4) Separate First/Last fields
        String first = firstNonEmpty(row, "First", "first", "First Name", "first name", "FName", "Given", "Given Name");
        String last  = firstNonEmpty(row, "Last", "last", "Last Name", "last name", "LName", "Family", "Surname");
        if (!first.isEmpty() || !last.isEmpty()) {
            Customer c = findCustomerByName(first, last);
            if (c != null) return c;
        }

        return null; // not found -> autopurchase fails (wallet required)
    }

    // “Last, First” OR “First [Middle …] Last”
    private static String[] splitNameSmart(String raw) {
        if (raw == null) return new String[]{"",""};
        String s = raw.trim();
        if (s.contains(",")) { // "Last, First Middle"
            String[] parts = s.split(",", 2);
            String last  = parts[0].trim();
            String first = (parts.length > 1 ? parts[1].trim() : "");
            // first token as FIRST (drop middles)
            String[] fparts = first.split("\\s+");
            if (fparts.length >= 1) first = fparts[0];
            return new String[]{ first, last };
        } else {
            // "First [Middle …] Last"
            String[] parts = s.split("\\s+");
            if (parts.length == 1) return new String[]{ parts[0], "" };
            String first = parts[0];
            String last  = parts[parts.length - 1];
            return new String[]{ first, last };
        }
    }

    // Scan the list to find a customer by first/last (case-insensitive simple compare)
    private static Customer findCustomerByName(String first, String last) {
        for (Customer c : customerList) {
            String cf = (c.getFirstName() == null ? "" : c.getFirstName());
            String cl = (c.getLastName()  == null ? "" : c.getLastName());
            boolean firstOk = first == null || first.isEmpty() || cf.equalsIgnoreCase(first);
            boolean lastOk  = last  == null || last.isEmpty()  || cl.equalsIgnoreCase(last);
            if (firstOk && lastOk) {
                return c;
            }
        }
        return null;
    }

    private static Customer findCustomerByUsername(String uname) {
        for (Customer c : customerList) {
            if (c.getUsername().equalsIgnoreCase(uname)) return c;
        }
        return null;
    }

    private static TicketType parseTicketTypeFlexible(String s) {
        String t = s.toLowerCase(Locale.ROOT).trim();
        if (t.equals("1") || t.contains("vip")) return TicketType.VIP;
        if (t.equals("2") || t.contains("gold")) return TicketType.GOLD;
        if (t.equals("3") || t.contains("silver")) return TicketType.SILVER;
        if (t.equals("4") || t.contains("bronze")) return TicketType.BRONZE;
        if (t.equals("5") || t.contains("ga") || t.contains("general")) return TicketType.GENERAL_ADMISSION;
        throw new IllegalArgumentException("Unknown ticket type: " + s);
    }
}
