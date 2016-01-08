package ZooSim.Exceptions;

/**
 * Thrown if an Enclosure is full
 *
 * @author Huw Jones
 * @since 27/10/2015
 */
public class EnclosureFullException extends Exception {
    public EnclosureFullException() {
        super("Enclosure does not have enough space to take more animals");
    }
}
