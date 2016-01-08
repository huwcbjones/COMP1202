package ZooSim.Exceptions;

/**
 * Thrown if an animal is already pregnant
 *
 * @author Huw Jones
 * @since 28/11/2015
 */
public class AnimalPregnantException extends Exception {
    public AnimalPregnantException(String message) {
        super(message);
    }
}
