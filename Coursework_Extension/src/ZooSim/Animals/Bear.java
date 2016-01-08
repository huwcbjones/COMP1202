package ZooSim.Animals;

import ZooSim.Exceptions.AnimalDeadException;
import ZooSim.Exceptions.LacksSkillException;
import ZooSim.Food.Food;
import ZooSim.Zookeepers.Zookeeper;

import java.util.ArrayList;

/**
 * A Bear
 *
 * @author Huw Jones
 * @since 28/10/2015
 */
public class Bear extends Animal {
    public Bear(char gender, int age, int health) {
        super(gender, age, health, 300, 7);
        this._eats.add(Food.Type.FISH);
        this._eats.add(Food.Type.STEAK);
    }

    /**
     * Treats the animal.
     * All Zookeepers can treat this animal.
     *
     * @param keeper Keeper who is treating the animal
     * @throws AnimalDeadException Thrown if the animal died
     * @throws LacksSkillException Thrown if the keeper is unskilled in this particular task
     */
    @Override
    public void treat(Zookeeper keeper) throws AnimalDeadException, LacksSkillException {
        if (this.isDead()) throw new AnimalDeadException();
        this._hug();
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
            this._hug();
        }
    }

    private void _hug() {
        this.increaseHealth(3);
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
            // If the animal is not a Bear, can't reproduce
            if (!(animal instanceof Bear)) {
                continue;
            }

            // We know the animal is a Bear, so cast it to a Bear
            Bear potentialMate = (Bear) animal;

            if (potentialMate.requestMate(this)) {
                this._mate = potentialMate;
                return;
            }
        }
    }
}
