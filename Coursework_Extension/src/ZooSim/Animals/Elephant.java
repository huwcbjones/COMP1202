package ZooSim.Animals;

import ZooSim.Exceptions.AnimalDeadException;
import ZooSim.Exceptions.LacksSkillException;
import ZooSim.Food.Food;
import ZooSim.Zookeepers.Zookeeper;
import ZooSim.Zookeepers.physioZookeeper;

import java.util.ArrayList;

/**
 * An Elephant!
 *
 * @author Huw Jones
 * @since 28/10/2015
 */
public class Elephant extends Animal {

    public Elephant(char gender, int age, int health) {
        super(gender, age, health, 840, 18);
        this._eats.add(Food.Type.HAY);
        this._eats.add(Food.Type.FRUIT);
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
        if (this._mate == animal) {
            this._bath();
        }
    }

    /**
     * Treats Elephant.
     * This animal can only be treated by a physioZookeeper.
     *
     * @param keeper The keeper carrying out the action
     * @throws AnimalDeadException Thrown if the animal died
     * @throws LacksSkillException Thrown if the keeper is unskilled in this particular task
     */
    @Override
    public void treat(Zookeeper keeper) throws AnimalDeadException, LacksSkillException {
        if (this.isDead()) throw new AnimalDeadException();
        if (!(keeper instanceof physioZookeeper)) {
            throw new LacksSkillException(keeper, "bath", this);
        }
        _bath();
    }

    private void _bath() {
        this.increaseHealth(5);
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
            // If the animal is not a Elephant, can't reproduce
            if (!(animal instanceof Elephant)) {
                continue;
            }

            // We know the animal is a Elephant, so cast it to a Elephant
            Elephant potentialMate = (Elephant) animal;

            if (potentialMate.requestMate(this)) {
                this._mate = potentialMate;
                return;
            }
        }
    }
}

