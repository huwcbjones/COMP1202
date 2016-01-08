package ZooSim.Exceptions;

/**
 * Thrown if an unknown food is found
 *
 * @author Huw Jones
 * @since 03/11/2015
 */
public class UnknownFoodException extends Exception {
    public UnknownFoodException(String message) {
        super(message);
    }
}
