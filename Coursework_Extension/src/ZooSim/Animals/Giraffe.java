package ZooSim.Animals;

import ZooSim.Exceptions.AnimalDeadException;
import ZooSim.Exceptions.LacksSkillException;
import ZooSim.Food.Food;
import ZooSim.Zookeepers.Zookeeper;
import ZooSim.Zookeepers.physioZookeeper;

import java.util.ArrayList;

/**
 * A Giraffe
 *
 * @author Huw Jones
 * @since 28/10/2015
 */
public class Giraffe extends Animal {

    public Giraffe(char gender, int age, int health) {
        super(gender, age, health, 300, 13);
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
            _neckMassage();
        }
    }

    /**
     * Treats Giraffe.
     * Only a physioZookeeper can treat this animal.
     *
     * @throws AnimalDeadException Thrown if the animal died
     * @throws LacksSkillException Thrown if the keeper is unskilled in this particular task
     */
    @Override
    public void treat(Zookeeper keeper) throws AnimalDeadException, LacksSkillException {
        if (this.isDead()) throw new AnimalDeadException();
        if (!(keeper instanceof physioZookeeper)) {
            throw new LacksSkillException(keeper, "neck massage", this);
        }
        this._neckMassage();
    }

    private void _neckMassage() {
        this.increaseHealth(4);
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
            // If the animal is not a Giraffe, can't reproduce
            if (!(animal instanceof Giraffe)) {
                continue;
            }

            // We know the animal is a Giraffe, so cast it to a Giraffe
            Giraffe potentialMate = (Giraffe) animal;

            if (potentialMate.requestMate(this)) {
                this._mate = potentialMate;
                return;
            }
        }
    }
}
