package ZooSim.Food;

/**
 * A nice juicy steak
 *
 * @author Huw Jones
 * @since 19/11/2015
 */
public class steak extends Food {
    public steak() {
        super(Type.STEAK, 3, 4);
    }

    @Override
    public Food copyOf() {
        steak newSteak = new steak();
        try {
            newSteak.addAmount(this.getAmount());
        } catch (Exception ex) {

        }
        return newSteak;
    }
}
