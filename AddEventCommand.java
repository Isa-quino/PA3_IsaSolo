import java.util.Scanner;

/**
 * @author Daniela Molina-Najera
 * Command that initiates the process of adding a new event to the system.
 * Implements the Command interface and delegates event creation with the
 * already implemented addEvent method
 * 
 * This class supports both pre-supplied event creation and interactive
 * input with Scanner, allowing flexibility in how events are added.
 */


public class AddEventCommand implements Command{
    private Event event;
    private Scanner scanner;

    public AddEventCommand() {}

    public AddEventCommand(Event event, Scanner scanner) {
        this.event = event;
        this.scanner = scanner;
    }

    public AddEventCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Executes the event creation process.
     */
    @Override
    public void execute() {
        RunTicket.addEvent(scanner);
    }
}