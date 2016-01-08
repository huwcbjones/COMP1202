package ZooSim.Animals;

import ZooSim.Exceptions.AnimalDeadException;
import ZooSim.Exceptions.LacksSkillException;
import ZooSim.Zookeepers.Zookeeper;
import ZooSim.Zookeepers.physioZookeeper;

import java.util.ArrayList;

/**
 * A Chimpanzee
 *
 * @author Huw Jones
 * @since 28/10/2015
 */

public class Chimpanzee extends Ape {

    public Chimpanzee(char gender, int age, int health) {
        super(gender, age, health, 600, 8);
    }

    /**
     * Treats the animal (via their mate)
     *
     * @param animal Animal trying to treat their mate
     * @throws AnimalDeadException Thrown if the animal is dead
     */
    @Override
    public void treat(Animal animal) throws AnimalDeadException {
        if (this._mate == animal) {
            this.playChase();
        }
    }

    /**
     * Treats the Chimpanzee.
     * Only PhysioZookeepers can treat this .
     *
     * @param keeper Keeper who is treating the animal
     * @throws AnimalDeadException Thrown if the animal died
     * @throws LacksSkillException Thrown if the keeper is unskilled in this particular task
     */
    @Override
    public void treat(Zookeeper keeper) throws AnimalDeadException, LacksSkillException {
        if (this.isDead()) throw new AnimalDeadException();
        if (this.isDead()) throw new AnimalDeadException();
        if (!(keeper instanceof physioZookeeper)) {
            throw new LacksSkillException(keeper, "play chase", this);
        }
        this.playChase();
    }

    private void playChase() {
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
            // If the animal is not a chimpanzee, can't reproduce
            if (!(animal instanceof Chimpanzee)) {
                continue;
            }

            // We know the animal is a chimpanzee, so cast it to a chimpanzee
            Chimpanzee potentialMate = (Chimpanzee) animal;

            if (potentialMate.requestMate(this)) {
                this._mate = potentialMate;
                return;
            }
        }
    }
}
