package ZooSim.Zookeepers;

import ZooSim.Enclosures.Enclosure;
import ZooSim.Foodstore;

/**
 * Specially trained Zookeeper that can treat animals that require physical treats.
 * Such treats include neck massage, game of chase, bathing.
 *
 * @author Huw Jones
 * @see Zookeeper
 * @since 28/10/2015
 */
public class physioZookeeper extends Zookeeper {
    public physioZookeeper(Enclosure enclosure, Foodstore foodstore) {
        super(enclosure, foodstore);
    }
}
