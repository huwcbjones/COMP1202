package ZooSim.Food;

/**
 * A bale of hay
 *
 * @author Huw Jones
 * @since 19/11/2015
 */
public class hay extends Food {
    public hay() {
        super(Type.HAY, 1, 4);
    }

    @Override
    public Food copyOf() {
        hay newHay = new hay();

        try {
            newHay.addAmount(this.getAmount());
        } catch (Exception ex) {

        }
        return newHay;
    }
}
