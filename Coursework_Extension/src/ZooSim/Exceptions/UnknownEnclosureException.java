package ZooSim.Exceptions;

/**
 * Thrown when Enclosure for an animal was not set
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class UnknownEnclosureException extends Exception {

    public UnknownEnclosureException(String message) {
        super(message);
    }
}
