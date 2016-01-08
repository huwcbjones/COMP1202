package ZooSim.Zookeepers;

import ZooSim.Animals.Animal;
import ZooSim.Animals.Bear;
import ZooSim.Animals.Elephant;
import ZooSim.Animals.Lion;
import ZooSim.Enclosures.Enclosure;
import ZooSim.Exceptions.LacksSkillException;
import ZooSim.Food.Food;
import ZooSim.Foodstore;
import ZooSim.Simulator;
import org.junit.Test;

import java.net.SocketTimeoutException;

import static org.junit.Assert.assertEquals;

/**
 * Test Unit for Zookeeper
 *
 * @author Huw Jones
 * @since 05/11/2015
 */
public class ZookeeperTest {

    @Test
    public void testAMonthPasses() throws Exception {
        Simulator zoo = new Simulator();
        Enclosure enclosure = new Enclosure();
        Zookeeper zookeeper = new Zookeeper(enclosure, zoo.getFoodstore());

        Foodstore zooFoodstore = zoo.getFoodstore();
        Foodstore enclosureFoodstore = enclosure.getFoodstore();

        zoo.getFoodstore().addFood(Food.Type.STEAK, 40);
        enclosure.getFoodstore().addFood(Food.Type.STEAK, 0);

        zookeeper.aMonthPasses();

        assertEquals(20, zooFoodstore.getAmountOf(Food.Type.STEAK));
        assertEquals(20, enclosureFoodstore.getAmountOf(Food.Type.STEAK));

        zookeeper.aMonthPasses();
        assertEquals(0, zooFoodstore.getAmountOf(Food.Type.STEAK));
        assertEquals(40, enclosureFoodstore.getAmountOf(Food.Type.STEAK));
    }

    @Test
    public void testTreat() throws Exception {
        Simulator zoo = new Simulator();
        Enclosure enclosure = new Enclosure();
        Zookeeper zookeeper = new Zookeeper(enclosure, zoo.getFoodstore());

        Lion lion1 = new Lion('m', 1, 3);
        Lion lion2 = new Lion('m', 1, 4);
        Lion lion3 = new Lion('m', 1, 5);

        enclosure.addAllAnimal(new Animal[]{lion1, lion2, lion3});

        zookeeper.aMonthPasses();

        assertEquals(5, lion1.getHealth());

        Bear bear1 = new Bear('m', 1, 2);
        enclosure.addAnimal(bear1);

        zookeeper.aMonthPasses();

        assertEquals(5, bear1.getHealth());
    }

    @Test(expected = LacksSkillException.class)
    public void testTreat_lacksSkill() throws Exception {
        Simulator zoo = new Simulator();
        Enclosure enclosure = new Enclosure();
        Zookeeper zookeeper = new Zookeeper(enclosure, zoo.getFoodstore());

        Animal elephant = new Elephant('m', 1, 5);
        elephant.treat(zookeeper);
    }
}