package ZooSim.Exceptions;

/**
 * Thrown if an Enclosure was not yet built
 *
 * @author Huw Jones
 * @since 31/10/2015
 */
public class EnclosureNotBuiltYetException extends Exception {
    public EnclosureNotBuiltYetException(String message) {
        super(message);
    }
}
