package ZooSim.Zookeepers;

import ZooSim.Animals.Animal;
import ZooSim.Animals.Chimpanzee;
import ZooSim.Enclosures.Enclosure;
import ZooSim.Exceptions.LacksSkillException;
import ZooSim.Simulator;
import org.junit.Test;

/**
 * Test Unit for playZookeeper
 *
 * @author Huw Jones
 * @since 28/10/2015
 */

public class playZookeeperTest {

    @Test(expected = LacksSkillException.class)
    public void testTreat_lacksSkill() throws Exception {
        Simulator zoo = new Simulator();
        Enclosure enclosure = new Enclosure();
        Zookeeper zookeeper = new Zookeeper(enclosure, zoo.getFoodstore());

        Animal chimpanzee = new Chimpanzee('m', 1, 5);
        chimpanzee.treat(zookeeper);
    }
}