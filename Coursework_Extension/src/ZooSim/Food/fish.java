package ZooSim.Food;

/**
 * A fish
 *
 * @author Huw Jones
 * @since 19/11/2015
 */
public class fish extends Food {
    public fish() {
        super(Type.FISH, 3, 2);
    }

    @Override
    public Food copyOf() {
        fish newFish = new fish();
        try {
            newFish.addAmount(this.getAmount());
        } catch (Exception ex) {

        }
        return newFish;
    }
}
