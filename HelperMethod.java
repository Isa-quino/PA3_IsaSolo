import java.util.InputMismatchException;
import java.util.Scanner;

/** @author Daniela Molina-Najera (work in progress)
 * The purpose of this class is to have all the helper
 * methods I have in order to make the code cleaner
 * and easier to read.
 */
public class HelperMethod {

    // //MIGHT NOT USE
    // public double texasSalesTax(double amount) {
    //     return amount * 8.25;
    // }

    /**
     * Checks for input mismatch (int)
     * @param scanner
     * @param message
     * @return
     */
    public int inputMismatchInt(Scanner scanner, String message) {
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


    /**
     * Checks for input mismatch (double)
     * @param scanner
     * @param message
     * @return
     */
    public double inputMismatchDOuble(Scanner scanner, String message) {
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
}
