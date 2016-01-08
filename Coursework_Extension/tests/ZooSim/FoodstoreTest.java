package ZooSim;

import ZooSim.Exceptions.NotEnoughFoodException;
import ZooSim.Food.Food;
import org.junit.Test;

/**
 * Testing class for Zoo.Foodstore
 *
 * @author Huw Jones
 * @since 04/11/2015
 */
public class FoodstoreTest {

    /**
     * Tests adding food to the Foodstore
     *
     * @throws Exception If test failed
     */
    @Test
    public void testAddFood() throws Exception {
        // Create new Foodstore
        Foodstore foodstore = new Foodstore();

        // Add some steak
        foodstore.addFood(Food.Type.STEAK, 5);

        // Foodstore should now contain steak
        assert (foodstore.getAvailableFood().contains(Food.Type.STEAK));
    }

    /**
     * Tests taking food from the Foodstore
     *
     * @throws Exception If test failed
     */
    @Test
    public void testTakeFood() throws Exception {
        // Create new Foodstore
        Foodstore foodstore = new Foodstore();

        // Add some steak
        foodstore.addFood(Food.Type.STEAK, 5);

        // Foodstore should now contain steak
        assert (foodstore.getAvailableFood().contains(Food.Type.STEAK));

        // Foodstore should return 2
        assert (foodstore.takeFood(Food.Type.STEAK, 2).getAmount() == 2);

        foodstore.takeFood(Food.Type.STEAK);

        // Foodstore should return 2 even though 5 food was requested
        assert (foodstore.takeFood(Food.Type.STEAK, 5, true).getAmount() == 2);
    }

    /**
     * Tests whether Foodstore throws NotEnoughFoodException when more food than available is requested
     *
     * @throws Exception If test failed
     */
    @Test(expected = NotEnoughFoodException.class)
    public void testFoodstoreException() throws Exception {
        // Create new Foodstore
        Foodstore foodstore = new Foodstore();

        // Add some steak
        foodstore.addFood(Food.Type.STEAK, 5);

        // Foodstore should now contain steak
        assert (foodstore.getAvailableFood().contains(Food.Type.STEAK));

        // Should now throw NotEnoughFoodException
        foodstore.takeFood(Food.Type.STEAK, 10);
    }
}