package ZooSim.Exceptions;

/**
 * Thrown if an animal is not healthy enough to do something
 *
 * @author Huw Jones
 * @since 28/11/2015
 */
public class AnimalNotHealthyEnoughException extends Exception {
    public AnimalNotHealthyEnoughException(String message) {
        super(message);
    }
}
