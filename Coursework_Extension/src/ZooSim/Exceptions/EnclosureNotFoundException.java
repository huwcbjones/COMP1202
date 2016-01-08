package ZooSim.Exceptions;

/**
 * Thrown if an Enclosure was not found
 *
 * @author Huw Jones
 * @since 01/11/2015
 */
public class EnclosureNotFoundException extends Exception {
    public EnclosureNotFoundException(String message) {
        super(message);
    }
}
