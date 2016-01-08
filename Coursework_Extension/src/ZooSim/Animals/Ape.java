package ZooSim.Animals;

import ZooSim.Food.Food;

/**
 * Base class for Apes
 *
 * @author Huw Jones
 * @since 27/10/2015
 */
abstract class Ape extends Animal {

    Ape(char gender, int age, int health, int lifeExpectancy, int gestationPeriod) {
        super(gender, age, health, lifeExpectancy, gestationPeriod);
        this._eats.add(Food.Type.FRUIT);
        this._eats.add(Food.Type.ICECREAM);
    }
}
