package ZooSim.Food;

/**
 * A delicious Ice Cream
 *
 * @author Huw Jones
 * @since 20/11/2015
 */
public class icecream extends Food {
    public icecream() {
        super(Type.ICECREAM, 1, 3);
    }

    @Override
    public Food copyOf() {
        icecream newIcecream = new icecream();
        try {
            newIcecream.addAmount(this.getAmount());
        } catch (Exception ex) {
        }
        return newIcecream;
    }
}
