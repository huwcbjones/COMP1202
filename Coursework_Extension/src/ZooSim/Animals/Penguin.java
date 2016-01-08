package ZooSim.Animals;

import ZooSim.Exceptions.AnimalDeadException;
import ZooSim.Exceptions.LacksSkillException;
import ZooSim.Food.Food;
import ZooSim.Zookeepers.Zookeeper;
import ZooSim.Zookeepers.playZookeeper;

import java.util.ArrayList;

/**
 * A Penguin!
 *
 * @author Huw Jones
 * @since 28/10/2015
 */
public class Penguin extends Animal {

    public Penguin(char gender, int age, int health) {
        super(gender, age, health, 240, 2);
        this._eats.add(Food.Type.FISH);
        this._eats.add(Food.Type.ICECREAM);
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
            _watchAFilm();
        }
    }

    /**
     * Treats Penguin.
     * Only playZookeepers can treat this animal.
     *
     * @param keeper The keeper carrying out the action
     * @throws AnimalDeadException Thrown if the animal died
     * @throws LacksSkillException Thrown if the keeper is unskilled in this particular task
     */
    @Override
    public void treat(Zookeeper keeper) throws AnimalDeadException, LacksSkillException {
        if (this.isDead()) throw new AnimalDeadException();
        if (!(keeper instanceof playZookeeper)) {
            throw new LacksSkillException(keeper, "watch a film", this);
        }
        this._watchAFilm();
    }

    private void _watchAFilm() {
        this.increaseHealth(2);
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
            if (!(animal instanceof Penguin)) {
                continue;
            }

            // We know the animal is a chimpanzee, so cast it to a chimpanzee
            Penguin potentialMate = (Penguin) animal;

            if (potentialMate.requestMate(this)) {
                this._mate = potentialMate;
                return;
            }
        }
    }
}
