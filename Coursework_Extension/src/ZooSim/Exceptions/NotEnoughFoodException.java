package ZooSim.Exceptions;

/**
 * Thrown if there is not enough food
 *
 * @author Huw Jones
 * @since 01/11/2015
 */
public class NotEnoughFoodException extends Exception {
    public NotEnoughFoodException(String message) {
        super(message);
    }
}
