package ZooSim;

import ZooSim.Exceptions.InvalidQuantityException;
import ZooSim.Exceptions.NotEnoughFoodException;
import ZooSim.Exceptions.UnknownFoodException;
import ZooSim.Food.Food;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stores food for Zoo or Enclosure
 *
 * @author Huw Jones
 * @since 27/10/2015
 */
public class Foodstore {

    private final HashMap<Food.Type, Food> storage = new HashMap<>();
    private final HashMap<String, Food.Type> typeHashMap = new HashMap<>();

    public Foodstore() {
        for (Food.Type type : Food.Type.values()) {
            typeHashMap.put(type.name().toLowerCase(), type);
        }
    }

    /**
     * Gets an ArrayList of the available food in the foodstore
     *
     * @return ArrayList of available food
     */
    public ArrayList<Food.Type> getAvailableFood() {
        return new ArrayList<>(storage.keySet());
    }

    /**
     * Requests food to be transferred to the Foodstore
     *
     * @param food Food to be transferred
     * @throws UnknownFoodException Thrown if food type is unknown
     */
    public void requestFood(String food) throws UnknownFoodException {
        requestFood(this.typeHashMap.get(food));
    }

    /**
     * Requests food to be transferred to the Foodstore
     *
     * @param food Food to be transferred
     * @throws UnknownFoodException Thrown if food type is unknown
     */
    public void requestFood(Food.Type food) throws UnknownFoodException {
        /*
        switch(food){
            case CELERY:
                newFood = new celery();
                break;
            default:
                throw new UnknownFoodException("Food type " + food.name() + " was not found.");
        }*/

        // F**k it, hammer time!

        try {
            // Get's the current class loader
            ClassLoader classLoader = this.getClass().getClassLoader();

            // Creates the new class (will throw an exception if class is not found, but this is handled)
            Class<?> newAnimalClass = classLoader.loadClass(Simulator.class.getPackage().getName() + ".Food." + food.name().toLowerCase());

            // 1) Creates a new food Food (we know it's an food as all foods inherit from Food, makes typing it a
            //    little easier. You could use an Object, but Food is more suitable for aforementioned reasons.
            //    + polymorphism
            // 2) Then it creates a new instance.

            Food newFood = (Food) newAnimalClass.newInstance();

            storage.put(food, newFood);

        } catch (Exception ex) {
            throw new UnknownFoodException("Food type " + food.name() + " was not found.");
        }
    }


    /**
     * Adds amount of food to foodstore
     *
     * @param food   Food to add
     * @param amount Amount to add
     */
    public void addFood(String food, int amount) {
        addFood(this.typeHashMap.get(food), amount);
    }

    /**
     * Adds amount of food to foodstore
     *
     * @param food   Food to add
     * @param amount Amount to add
     */
    public void addFood(Food.Type food, int amount) {
        // Check if foodstore already contains food
        if (storage.containsKey(food)) {

            try {
                // HashMap stores object references, so we can just call addAmount on Food
                storage.get(food).addAmount(amount);
            } catch (InvalidQuantityException ex) {
                ErrorMsg.Warning("Failed to add food to Foodstore: " + ex.getMessage());
            }
        } else {
            try {
                // requestFood puts the food type into the HashMap
                this.requestFood(food);
                // Call this again, if it fails, it'll be caught in the catch block and exit, otherwise it'll be handled
                // in the first if, then quit the recursive loop
                this.addFood(food, amount);
            } catch (UnknownFoodException ex) {
                ErrorMsg.Warning("Failed to add food to Foodstore: " + ex.getMessage());
            }
        }
    }

    /**
     * Takes 1 food from the Foodstore
     *
     * @param food Food to take
     * @return Amount of food taken from the store
     * @throws NotEnoughFoodException Thrown if there is not enough food left
     * @throws UnknownFoodException Thrown if the food type is unknown
     * @throws InvalidQuantityException Thrown if the amount specified is invalid
     */
    public Food takeFood(Food.Type food) throws NotEnoughFoodException, UnknownFoodException, InvalidQuantityException {
        return takeFood(food, 1);
    }

    /**
     * Takes x amount of food from Foodstore
     *
     * @param food   Food to take
     * @param amount Amount to take
     * @return Amount of food taken from the store
     * @throws NotEnoughFoodException Thrown if there is not enough food left
     * @throws UnknownFoodException Thrown if the food type is unknown
     * @throws InvalidQuantityException Thrown if the amount specified is invalid
     */
    public Food takeFood(Food.Type food, int amount) throws NotEnoughFoodException, UnknownFoodException, InvalidQuantityException {
        return this.takeFood(food, amount, false);
    }

    /**
     * Takes x amount of food from Foodstore
     *
     * @param food          Food to take
     * @param amount        Amount to take
     * @param allowedCapped Allow the amount of food to be capped to food left (if amount &gt; food)
     * @return Amount of food taken from the store
     * @throws NotEnoughFoodException Thrown if there is not enough food left
     * @throws UnknownFoodException Thrown if the food type is unknown
     * @throws InvalidQuantityException Thrown if the amount specified is invalid
     */
    public Food takeFood(Food.Type food, int amount, boolean allowedCapped) throws NotEnoughFoodException, UnknownFoodException, InvalidQuantityException {
        Food currentFood;
        // Check we have this type of food in Foodstore
        if (storage.containsKey(food)) {

            // Store current amount
            currentFood = storage.get(food);

            // Check if more than available being requested
            if (amount > currentFood.getAmount()) {

                // If asked for all available food
                if (allowedCapped) {
                    storage.remove(food);
                    return currentFood;
                } else {
                    throw new NotEnoughFoodException("Cannot take more food than there is available. Food({" + food + "}, {" + currentFood + "}, {" + amount + "})");
                }
            } else {
                Food newFood = currentFood.copyOf();
                currentFood.takeAmount(amount);
                newFood.takeAmount(newFood.getAmount());
                newFood.addAmount(amount);
                return newFood;
            }
        } else if (allowedCapped) {
            // Put the food into storage, so next month it will be obtained
            this.requestFood(food);
            return this.storage.get(food).copyOf();
        } else {
            this.requestFood(food);
            throw new NotEnoughFoodException("Cannot take more food than there is available. Food({" + food.name() + "}, {" + amount + "})");
        }
    }

    /**
     * Takes 1 food from the Foodstore
     *
     * @param food Food to take
     * @return Amount of food taken from the store
     * @throws NotEnoughFoodException Thrown if there is not enough food left
     * @throws UnknownFoodException Thrown if the food type is unknown
     * @throws InvalidQuantityException Thrown if the amount specified is invalid
     */
    public Food takeFood(String food) throws NotEnoughFoodException, UnknownFoodException, InvalidQuantityException {
        return takeFood(this.typeHashMap.get(food), 1);
    }

    /**
     * Returns amount of a type of food in the foodstore
     *
     * @param food Food to check
     * @return Amount of food
     */
    public int getAmountOf(Food.Type food) {
        if (!storage.containsKey(food)) {
            return 0;
        }
        return storage.get(food).getAmount();
    }

    /**
     * Creates a String that represents the Foodstore state (that can be reloaded by the Simulator)
     *
     * @return State string
     */
    public String getState() {
        StringBuilder state = new StringBuilder();

        for (Food food : this.storage.values()) {
            state.append(food.getType().name().toLowerCase());
            state.append(":");
            state.append(food.getAmount());
            state.append("\n");
        }

        return state.toString();
    }

}
