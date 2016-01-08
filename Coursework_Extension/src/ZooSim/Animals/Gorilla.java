package ZooSim.Animals;

import ZooSim.Exceptions.AnimalDeadException;
import ZooSim.Exceptions.LacksSkillException;
import ZooSim.Zookeepers.Zookeeper;
import ZooSim.Zookeepers.playZookeeper;

/**
 * A Gorilla!
 *
 * @author Huw Jones
 * @since 28/10/2015
 */
public class Gorilla extends Ape {

    public Gorilla(char gender, int age, int health) {
        super(gender, age, health, 450, 9);
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
            this._paint();
        }
    }

    /**
     * Treats Gorilla.
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
            throw new LacksSkillException(keeper, "painting", this);
        }
        this._paint();
    }

    private void _paint() {
        super.increaseHealth(4);
    }

    /**
     * Animal finds a mate to mate with
     */
    @Override
    public void findMate() {

    }
}