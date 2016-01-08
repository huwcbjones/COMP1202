package ZooSim.Animals;

import ZooSim.Exceptions.AnimalDeadException;
import ZooSim.Exceptions.LacksSkillException;
import ZooSim.Zookeepers.Zookeeper;

import java.util.ArrayList;

/**
 * A Tiger
 *
 * @author Huw Jones
 * @since 28/10/2015
 */
public class Tiger extends BigCat {
    public Tiger(char gender, int age, int health) {
        super(gender, age, health, 276, 4);
    }

    /**
     * Treats the animal.
     * All Zookeepers can treat this animal.
     *
     * @param keeper Keeper to perform the treat
     * @throws AnimalDeadException Thrown if the animal died
     * @throws LacksSkillException Thrown if the keeper is unskilled in this particular task
     */
    @Override
    public void treat(Zookeeper keeper) throws AnimalDeadException, LacksSkillException {
        if (this.isDead()) throw new AnimalDeadException();
        {
            this._stroke();
        }
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
            _stroke();
        }
    }

    @Override
    protected void _stroke() {
        super.increaseHealth(3);
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
            if (!(animal instanceof Tiger)) {
                continue;
            }

            // We know the animal is a chimpanzee, so cast it to a chimpanzee
            Tiger potentialMate = (Tiger) animal;

            if (potentialMate.requestMate(this)) {
                this._mate = potentialMate;
                return;
            }
        }
    }
}
