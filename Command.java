/**
 * This interface represents a generic command in the system.
 * Implementing classes define specific actions that can be executed,
 * such as purchasing tickets, deleting events, and refunds.
 * This interface implements the Command design pattern, enabling
 * encapsulation of request logic and flexible command handling.
 */
public interface Command {
    /**
     * Executes the commands action and is defined
     * by the implementing class.
     */
    void execute(); // abstract method
}
