package ZooSim.Food;

import ZooSim.Exceptions.InvalidQuantityException;
import ZooSim.Exceptions.NotEnoughFoodException;

/**
 * Abstract class that provides all member variables and access methods required for different types of food
 *
 * @author Huw Jones
 * @since 19/11/2015
 */
public abstract class Food {

    private final Type type;
    private final int health;
    private final int waste;
    private int amount = 0;

    Food(Type type, int health, int waste) {
        this.type = type;
        this.health = health;
        this.waste = waste;
    }

    /**
     * Gets health value
     *
     * @return int - how much health food provides
     */
    public int getHealth() {
        return health;
    }

    /**
     * Gets waste value
     *
     * @return int - how much waste is produced
     */
    public int getWaste() {
        return waste;
    }

    /**
     * Gets amount of food
     *
     * @return int - amount of food
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Adds an amount of food
     *
     * @param amount amount of food to add
     * @throws InvalidQuantityException If amount &lt; 0
     */
    public void addAmount(int amount) throws InvalidQuantityException {
        if (amount < 0) {
            throw new InvalidQuantityException("addAmount takes natural numbers only.");
        } else {
            this.amount += amount;
        }
    }

    /**
     * Takes an amount of food
     *
     * @param amount amount of food to take
     * @throws NotEnoughFoodException   Thrown if there isn't enough food to take
     * @throws InvalidQuantityException Thrown if amount to take is &lt;=0
     */
    public void takeAmount(int amount) throws NotEnoughFoodException, InvalidQuantityException {
        if (amount <= 0) {
            throw new InvalidQuantityException("takeAmount takes natural numbers only.");
        } else if (this.amount - amount < 0) {
            throw new NotEnoughFoodException("Cannot take more food than available.");
        } else {
            this.amount -= amount;
        }
    }

    /**
     * Get's food type
     *
     * @return Food.Type
     */
    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Food)) {
            return false;
        }
        Food food = (Food) object;
        return this.type == food.type && this.amount == food.amount;
    }

    /**
     * Creates a copy of the Food
     *
     * @return Copy of food
     */
    public abstract Food copyOf();

    /**
     * Types of Food
     */
    public enum Type {
        HAY,
        STEAK,
        FRUIT,
        CELERY,
        FISH,
        ICECREAM
    }

}
