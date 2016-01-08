package ZooSim;

import ZooSim.Animals.TestAnimal;
import ZooSim.Enclosures.Enclosure;
import ZooSim.Food.Food;
import ZooSim.Zookeepers.Zookeeper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test Simulator
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class SimulatorTest {

    @Test
    public void testAMonthPasses() throws Exception {
        Simulator zoo = new Simulator();
        assertNotNull(zoo.getFoodstore());

        zoo.getFoodstore().addFood(Food.Type.STEAK, 60);

        Enclosure enclosure = new Enclosure();
        zoo.addEnclosure(enclosure);

        TestAnimal animal = new TestAnimal('m', 1, 10);
        enclosure.addAnimal(animal);

        Zookeeper zookeeper = new Zookeeper(enclosure, zoo.getFoodstore());
        zoo.addZookeeper(zookeeper);

        zoo.aMonthPasses();
        zoo.aMonthPasses();

        assertEquals(40, zoo.getFoodstore().getAmountOf(Food.Type.STEAK));

        assertEquals(3, animal.getAge());
    }
}