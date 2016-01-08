package ZooSim.Animals;

import ZooSim.Food.Food;

/**
 * Base Animal Class for Cats
 *
 * @author Huw Jones
 * @since 27/10/2015
 */
abstract class BigCat extends Animal {

    BigCat(char gender, int age, int health, int lifeExpectancy, int gestationPeriod) {
        super(gender, age, health, lifeExpectancy, gestationPeriod);
        this._eats.add(Food.Type.STEAK);
        this._eats.add(Food.Type.CELERY);
    }

    protected abstract void _stroke();

}
