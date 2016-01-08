package ZooSim;

import ZooSim.Exceptions.EnclosureNotBuiltYetException;
import org.junit.Test;

/**
 * Test Unit for Config File loader
 *
 * @author Huw Jones
 * @since 08/11/2015
 */
public class ConfigLoaderTest {

    @Test(expected = EnclosureNotBuiltYetException.class)
    public void testEnclosureNotBuilt() throws Exception {
        String config = "zoo:\nlion:F,10,4";
        Simulator simulator = new Simulator();
        ConfigLoader configLoader = new ConfigLoader(simulator, config);
        configLoader.loadConfig();
    }
}