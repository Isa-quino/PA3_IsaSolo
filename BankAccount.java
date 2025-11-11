/**
 * Represents a customer's bank account / wallet in the TicketMiner system.
 * Tracks available balance and provides methods for depositing and withdrawing funds.
 * 
 * Used to process ticket purchases, refunds, and validate sufficient funds.
 */

public class BankAccount {
    private String name;
    private double balance;

    public BankAccount() {}

    public BankAccount(String nameIn, double balanceIn) {
        this.name = nameIn;
        this.balance = balanceIn;
    }

    public double getBalance() {
        return this.balance;
    }

    public void withdraw(double amount) throws InsufficientFundsException{
        if(amount > balance) {
            double shortage = amount - balance;
            throw new InsufficientFundsException("Insufficient Funds. You are short " + shortage, shortage); // creating new instance
        } //else not needed because "throw" ends the method
        this.balance -= amount;
        System.out.println("Withdrawl successful. Remaining balance: " + this.balance);
    }

    public void deposit(double refundAmount) { // used to refund a ticket
        if (refundAmount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }
        balance += refundAmount;
        System.out.println("Refund Successful.\nNew Balance: $" + balance);
    }
}
