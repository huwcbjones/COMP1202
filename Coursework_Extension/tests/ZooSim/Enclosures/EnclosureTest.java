package ZooSim.Enclosures;

import ZooSim.Animals.Animal;
import ZooSim.Animals.TestAnimal;
import ZooSim.Food.Food;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for Enclosure
 *
 * @author Huw Jones
 * @since 04/11/2015
 */
public class EnclosureTest {

    @Test
    public void testAddAnimal() throws Exception {
        Enclosure enclosure = new Enclosure();
        TestAnimal animal = new TestAnimal('m', 1, 10);

        enclosure.addAnimal(animal);

        assertEquals(1, enclosure.size());
    }

    @Test
    public void testRemoveAnimal() throws Exception {
        Enclosure enclosure = new Enclosure();
        TestAnimal animal = new TestAnimal('m', 1, 10);

        enclosure.addAnimal(animal);
        enclosure.removeAnimal(animal);
        assertEquals(0, enclosure.size());
    }

    @Test
    public void testAnimalAging() throws Exception {
        Enclosure enclosure = new Enclosure();
        TestAnimal animal = new TestAnimal('m', 1, 10);
        enclosure.addAnimal(animal);

        enclosure.aMonthPasses();

        assertEquals(2, animal.getAge());
    }

    @Test
    public void testWasteHandling() throws Exception {
        Enclosure enclosure = new Enclosure();

        // test Enclosure::getWasteSize
        assertEquals(0, enclosure.getWasteSize());

        // test Enclosure::addWaste
        enclosure.addWaste(5);
        assertEquals(5, enclosure.getWasteSize());

        // test Enclosure::removeWaste
        enclosure.removeWaste(3);
        assertEquals(2, enclosure.getWasteSize());
    }

    @Test
    public void testAMonthPasses() throws Exception {
        Enclosure enclosure = new Enclosure();
        Animal animal = new TestAnimal('m', 1, 10);

        enclosure.addAnimal(animal);

        enclosure.getFoodstore().addFood(Food.Type.CELERY, 1);
        enclosure.aMonthPasses();
        assertEquals(1, enclosure.getWasteSize());

        assertEquals(0, enclosure.getFoodstore().takeFood(Food.Type.CELERY, 1, true).getAmount());

        enclosure.getFoodstore().addFood(Food.Type.STEAK, 1);
        enclosure.aMonthPasses();
        assertEquals(5, enclosure.getWasteSize());
    }

    @Test
    public void testDeaths() throws Exception {
        Enclosure enclosure = new Enclosure();
        Animal animal = new TestAnimal('m', 1, 1);

        enclosure.addAnimal(animal);

        enclosure.aMonthPasses();
        enclosure.aMonthPasses();
        assertEquals(0, enclosure.size());
    }
}