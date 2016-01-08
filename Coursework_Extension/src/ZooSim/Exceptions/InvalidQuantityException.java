package ZooSim.Exceptions;

/**
 * Thrown when an invalid quantity was provided
 *
 * @author Huw Jones
 * @since 20/11/2015
 */
public class InvalidQuantityException extends Exception {
    public InvalidQuantityException(String message) {
        super(message);
    }
}
