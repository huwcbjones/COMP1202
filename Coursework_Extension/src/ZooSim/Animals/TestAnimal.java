package ZooSim.Animals;

import ZooSim.Exceptions.AnimalDeadException;
import ZooSim.Exceptions.LacksSkillException;
import ZooSim.Food.Food;
import ZooSim.Zookeepers.Zookeeper;

import java.util.ArrayList;

/**
 * A Simple Animal used for testing purposes
 *
 * @author Huw Jones
 * @since 04/11/2015
 */
public class TestAnimal extends Animal {

    public TestAnimal(char g, int a, int h) {
        super(g, a, h, 24, 4);
        this._eats.add(Food.Type.STEAK);
        this._eats.add(Food.Type.CELERY);
    }

    /**
     * Treats the animal (via their mate)
     *
     * @param animal Animal trying to treat their mate
     * @throws AnimalDeadException Thrown if the animal is dead
     */
    @Override
    public void treat(Animal animal) throws AnimalDeadException {
        if (animal.isDead()) throw new AnimalDeadException();
    }

    @Override
    public void treat(Zookeeper keeper) throws AnimalDeadException, LacksSkillException {
        if (this.isDead()) throw new AnimalDeadException();
    }

    /**
     * Animal finds a mate to mate with
     */
    @Override
    public void findMate() {
        ArrayList<Animal> potentialMates;
        try {
            potentialMates = this.getPotentialMates();
        } catch (NoSuchMethodException e) {
            return;
        }

        for (Animal animal : potentialMates) {
            // If the animal is not a chimpanzee, can't reproduce
            if (!(animal instanceof TestAnimal)) {
                continue;
            }

            // We know the animal is a chimpanzee, so cast it to a chimpanzee
            TestAnimal potentialMate = (TestAnimal) animal;

            if (potentialMate.requestMate(this)) {
                this._mate = potentialMate;
                return;
            }
        }
    }
}
