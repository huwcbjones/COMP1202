package ZooSim.Zookeepers;

import ZooSim.Enclosures.Enclosure;
import ZooSim.Foodstore;

/**
 * Specially trained Zookeeper that can treat animals which require playing with.
 * Such treats include painting, watching films.
 *
 * @author Huw Jones
 * @see Zookeeper
 * @since 28/10/2015
 */
public class playZookeeper extends Zookeeper {

    public playZookeeper(Enclosure enclosure, Foodstore foodstore) {
        super(enclosure, foodstore);
    }
}
