package ZooSim.Exceptions;

/**
 * Thrown if there was an error whilst loading the config.
 *
 * @author Huw Jones
 * @since 30/10/2015
 */
public class ConfigException extends Exception {
    public ConfigException(String message) {
        super(message);
    }
}
