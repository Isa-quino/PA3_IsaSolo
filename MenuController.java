/**
 * This is the main controller for handling user commands.
 * It implements the Command design pattern by delegating execution
 * to the Command instance.
 * It allows flexible routing and modular command handling
 */
public class MenuController {
    /**
     * Executes the given command
     * @param command command to be executed
     */
    public void handleCommand(Command command) {
        command.execute();
    }
}
