package ZooSim.Exceptions;

/**
 * Thrown when the waste is already empty
 *
 * @author Huw Jones
 * @since 27/10/2015
 */
public class WasteEmptyException extends Exception {
    public WasteEmptyException(String message) {
        super(message);
    }
}
