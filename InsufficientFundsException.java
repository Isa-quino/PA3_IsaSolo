/**
 * Custom exception thrown when a withdrawal exceeds the available balance
 * in BankAccount. Provides optional access to the shortage amount
 * to support detailed error handling or user feedback.
 */

public class InsufficientFundsException extends Exception{
    private double shortageAmount;

    public InsufficientFundsException(String messageIn, double shortageAmountIn) {
        super(messageIn);
        this.shortageAmount = shortageAmountIn;
    }

    public InsufficientFundsException(String messageIn) {
        super(messageIn);
    }

    public InsufficientFundsException() {}

    public double getShortageAmount() {
        return this.shortageAmount;
    }
}