import java.util.Scanner;

/**
 * Command that initiates the deletion of an event from the system.
 * Implements the Command interface and delegates the deletion logic
 * to the already implemented method deleteEventByID using interactive input.
 * 
 * This class supports console-driven workflows where an administrator
 *  selects an event to remove by ID.
 */


public class DeleteEventCommand implements Command{
    private Scanner scanner;

    public DeleteEventCommand() {}

    public DeleteEventCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Executes the event deletion process.
     * Prompts the user for an event ID and delegates the removal
     */
    @Override
    public void execute() {
        RunTicket.deleteEventByID(scanner);
    }
}
