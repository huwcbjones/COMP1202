package ZooSim.Zookeepers;

import ZooSim.Animals.Animal;
import ZooSim.Animals.Gorilla;
import ZooSim.Enclosures.Enclosure;
import ZooSim.Exceptions.LacksSkillException;
import ZooSim.Simulator;
import org.junit.Test;

/**
 * Test Unit for physioZookeeper
 *
 * @author Huw Jones
 * @since 18/11/2015
 */
public class physioZookeeperTest {

    @Test(expected = LacksSkillException.class)
    public void testTreat_lacksSkill() throws Exception {
        Simulator zoo = new Simulator();
        Enclosure enclosure = new Enclosure();
        Zookeeper zookeeper = new Zookeeper(enclosure, zoo.getFoodstore());

        Animal gorilla = new Gorilla('m', 1, 5);
        gorilla.treat(zookeeper);
    }
}