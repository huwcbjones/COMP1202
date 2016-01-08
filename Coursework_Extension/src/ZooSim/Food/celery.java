package ZooSim.Food;

/**
 * A stick of celery
 *
 * @author Huw Jones
 * @since 19/11/2015
 */
public class celery extends Food {
    public celery() {
        super(Type.CELERY, 0, 1);
    }

    @Override
    public Food copyOf() {
        celery newCelery = new celery();
        if (this.getAmount() != 0) {
            try {
                newCelery.addAmount(this.getAmount());
            } catch (Exception ex) {

            }
        }
        return newCelery;
    }
}
