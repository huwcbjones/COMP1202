package ZooSim;

import ZooSim.Enclosures.Enclosure;
import ZooSim.Exceptions.EnclosureNotFoundException;
import ZooSim.Zookeepers.Zookeeper;

import java.util.ArrayList;

/**
 * Zoo Simulator
 *
 * @author Huw Jones
 * @since 28/10/2015
 */

public class Simulator {
    private final ArrayList<Enclosure> _enclosures = new ArrayList<>();
    private final ArrayList<Zookeeper> _zookeepers = new ArrayList<>();
    private final Foodstore foodstore;
    private int _month = 0;

    public Simulator() {
        this.foodstore = new Foodstore();
    }

    /**
     * Runs the simulation and calls aMonthPasses on the relevant classes
     */
    public void aMonthPasses() {

        System.out.println("**************************************************");
        System.out.println("* MONTH " + String.format("%1$" + 40 + "s", _month) + " *");


        // Oh, I do love lambas and one-liners ;-)
        this._zookeepers.forEach(Zookeeper::aMonthPasses);
        this._enclosures.forEach(Enclosure::aMonthPasses);

        /*for (Zookeeper keeper : this._zookeepers) {
            keeper.aMonthPasses();
        }

        for (Enclosure enclosure : this._enclosures) {
            enclosure.aMonthPasses();
        }
        */
        this._month++;
    }

    /**
     * Get's Zoo Foodstore
     *
     * @return Foodstore
     */
    public Foodstore getFoodstore() {
        return this.foodstore;
    }

    /**
     * Loads the Simulator to a state as provided by the config
     *
     * @param config String containing config file
     */
    public void load(String config) {
        ConfigLoader loader = new ConfigLoader(this, config);
        try {
            loader.loadConfig();
        } catch (Exception ex) {
            ErrorMsg.Fatal("Exception whilst loading config.");
        }
    }

    /**
     * Loads the Simulator to a state as provided by the config
     *
     * @param config   String containing config file
     * @param fileName Filename of config file (useful for debugging/fixing issues)
     */
    public void load(String config, String fileName) {
        ConfigLoader loader = new ConfigLoader(this, config, fileName);
        try {
            loader.loadConfig();
        } catch (Exception ex) {
            ErrorMsg.Fatal("Exception whilst loading config.");
        }
    }

    /**
     * Saves simulator config to a String
     *
     * @return Simulator config
     */
    public String save() {
        StringBuilder config = new StringBuilder();

        // Zoo header
        config.append("zoo:\n");

        // Get foodstore state
        config.append(this.foodstore.getState());

        // Loop through enclosures and get enclosure state
        for (Enclosure enclosure : this._enclosures) {
            config.append(enclosure.getState());
        }

        // Loop through Zookeepers and get zookeeper state
        for (Zookeeper zookeeper : this._zookeepers) {
            config.append(zookeeper.getClass().getSimpleName());
            config.append(":\n");
        }

        return config.toString();
    }

    /**
     * Adds an enclosure to the zoo
     *
     * @param enclosure Enclosure to add
     * @return Index of new enclosure
     */
    int addEnclosure(Enclosure enclosure) {
        // Aha, this is so satisfying!
        // If only the spec encouraged the use of cohesive, encapsulated code!
        int newIndex = this._enclosures.size();
        this._enclosures.add(enclosure);
        return newIndex;
    }

    /**
     * Gets the enclosure, otherwise throws an EnclosureNotFoundException
     *
     * @param index Please 0-ise index, otherwise enjoy EnclosureNotFoundException! Woo!
     * @return Enclosure - the enclosure
     * @throws EnclosureNotFoundException Thrown if enclosure was not found
     */
    Enclosure getEnclosure(int index) throws EnclosureNotFoundException {
        try {
            return _enclosures.get(index);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new EnclosureNotFoundException("Enclosures does not contain and enclosure with that index.");
        }
    }

    /**
     * Adds a Zookeeper to the zoo
     *
     * @param zookeeper Zookeeper to add to the zoo
     */
    void addZookeeper(Zookeeper zookeeper) {
        this._zookeepers.add(zookeeper);
    }

}
