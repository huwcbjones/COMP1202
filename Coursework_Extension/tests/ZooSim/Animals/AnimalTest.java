package ZooSim.Animals;

import ZooSim.Enclosures.Enclosure;
import ZooSim.Exceptions.AnimalDeadException;
import ZooSim.Exceptions.NotEnoughFoodException;
import ZooSim.Exceptions.UnknownEnclosureException;
import ZooSim.Exceptions.UnknownFoodException;
import ZooSim.Food.Food;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests all Animals
 *
 * @author Huw Jones
 * @since 04/11/2015
 */
public class AnimalTest {

    private final int age = 1;

    /**
     * Tests Bear.aMonthPasses() for age
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testBear_aMonthPasses_age() throws Exception {
        testAnimal_aMonthPasses_age(new Bear('m', age, 10));
    }

    private void testAnimal_aMonthPasses_age(Animal animal) throws Exception {
        Enclosure enclosure = new Enclosure();

        animal.setEnclosure(enclosure);

        animal.aMonthPasses();

        // Animal should have aged by 1
        assertEquals(age + 1, animal._age);

        animal.aMonthPasses();

        // Animal should have aged by 2 now
        assertEquals(age + 2, animal._age);
    }

    /**
     * Tests Chimpanzee.aMonthPasses() for age
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testChimpanzee_aMonthPasses_age() throws Exception {
        testAnimal_aMonthPasses_age(new Chimpanzee('m', age, 10));
    }

    /**
     * Tests Elephant.aMonthPasses() for age
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testElephant_aMonthPasses_age() throws Exception {
        testAnimal_aMonthPasses_age(new Elephant('m', age, 10));
    }

    /**
     * Tests Giraffe.aMonthPasses() for age
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testGiraffe_aMonthPasses_age() throws Exception {
        testAnimal_aMonthPasses_age(new Giraffe('m', age, 10));
    }

    /**
     * Tests Gorilla.aMonthPasses() for age
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testGorilla_aMonthPasses_age() throws Exception {
        testAnimal_aMonthPasses_age(new Gorilla('m', age, 10));
    }

    /**
     * Tests Lion.aMonthPasses() for age
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testLion_aMonthPasses_age() throws Exception {
        testAnimal_aMonthPasses_age(new Lion('m', age, 10));
    }

    /**
     * Tests Penguin.aMonthPasses() for age
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testPenguin_aMonthPasses_age() throws Exception {
        testAnimal_aMonthPasses_age(new Penguin('m', age, 10));
    }

    /**
     * Tests Tiger.aMonthPasses() for age
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testTiger_aMonthPasses_age() throws Exception {
        testAnimal_aMonthPasses_age(new Tiger('m', age, 10));
    }

    /**
     * Tests TestAnimal.aMonthPasses() for health
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testTestAnimal_aMonthPasses_age() throws Exception {
        testAnimal_aMonthPasses_age(new TestAnimal('m', age, 10));
    }

    /**
     * Tests Bear.aMonthPasses() for health
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testBear_aMonthPasses_health() throws Exception {
        testAnimal_aMonthPasses_health(new Bear('m', age, 10), 3);
    }

    private void testAnimal_aMonthPasses_health(Animal animal, int foodModifier)
            throws NotEnoughFoodException, UnknownEnclosureException, UnknownFoodException, AnimalDeadException {
        Enclosure enclosure = new Enclosure();

        animal.setEnclosure(enclosure);

        animal.aMonthPasses();

        // No food, so animal's health should decrease by 2
        assertEquals(8, animal._health);

        int expectedMaxHealth = 8 + foodModifier;

        // Max health = 10
        if (expectedMaxHealth > 10) {
            expectedMaxHealth = 10;
        }

        enclosure.getFoodstore().addFood(Food.Type.STEAK, 1);
        enclosure.getFoodstore().addFood(Food.Type.ICECREAM, 1);
        enclosure.getFoodstore().addFood(Food.Type.HAY, 1);

        // Animal eats food and eat = + foodModifier
        animal.eat();
        assertEquals(expectedMaxHealth, animal._health);

        for (int i = 1; i <= 5; i++) {
            animal.aMonthPasses();
        }
        assertEquals(0, animal._health);

        // Animal health should now be 0
        animal.aMonthPasses();
        assertEquals(0, animal._health);
    }

    /**
     * Tests Chimpanzee.aMonthPasses() for health
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testChimpanzee_aMonthPasses_health() throws Exception {
        testAnimal_aMonthPasses_health(new Chimpanzee('m', age, 10), 1);
    }

    /**
     * Tests Elephant.aMonthPasses() for health
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testElephant_aMonthPasses_health() throws Exception {
        testAnimal_aMonthPasses_health(new Elephant('m', age, 10), 1);
    }

    /**
     * Tests Giraffe.aMonthPasses() for health
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testGiraffe_aMonthPasses_health() throws Exception {
        testAnimal_aMonthPasses_health(new Giraffe('m', age, 10), 1);
    }

    /**
     * Tests Gorilla.aMonthPasses() for health
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testGorilla_aMonthPasses_health() throws Exception {
        testAnimal_aMonthPasses_health(new Gorilla('m', age, 10), 1);
    }

    /**
     * Tests Lion.aMonthPasses() for health
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testLion_aMonthPasses_health() throws Exception {
        testAnimal_aMonthPasses_health(new Lion('m', age, 10), 3);
    }

    /**
     * Tests Penguin.aMonthPasses() for health
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testPenguin_aMonthPasses_health() throws Exception {
        testAnimal_aMonthPasses_health(new Penguin('m', age, 10), 1);
    }

    /**
     * Tests TestAnimal.aMonthPasses() for health
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testTestAnimal_aMonthPasses_health() throws Exception {
        testAnimal_aMonthPasses_health(new TestAnimal('m', age, 10), 3);
    }

    /**
     * Tests Tiger.aMonthPasses() for health
     *
     * @throws Exception Thrown if test fails
     */
    @Test
    public void testTiger_aMonthPasses_health() throws Exception {
        testAnimal_aMonthPasses_health(new Tiger('m', age, 10), 3);
    }
}