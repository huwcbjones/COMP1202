package ZooSim.Food;

/**
 * A piece of fruit
 *
 * @author Huw Jones
 * @since 19/11/2015
 */
public class fruit extends Food {
    public fruit() {
        super(Type.FRUIT, 2, 3);
    }

    @Override
    public Food copyOf() {
        fruit newFruit = new fruit();

        try {
            newFruit.addAmount(this.getAmount());
        } catch (Exception ex) {

        }
        return newFruit;
    }
}
