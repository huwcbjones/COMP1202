package ZooSim.Animals;

import ZooSim.Exceptions.AnimalDeadException;
import ZooSim.Exceptions.LacksSkillException;
import ZooSim.Zookeepers.Zookeeper;

import java.util.ArrayList;

/**
 * A Lion
 *
 * @author Huw Jones
 * @since 28/10/2015
 */
public class Lion extends BigCat {
    public Lion(Character gender, Integer age, Integer health) {
        super(gender, age, health, 144, 4);
    }

    /**
     * Treats Lion
     * All Zookeepers can treat this animal
     *
     * @param keeper The keeper carrying out the action
     * @throws AnimalDeadException Thrown if the animal died
     * @throws LacksSkillException Thrown if the keeper is unskilled in this particular task
     */
    @Override
    public void treat(Zookeeper keeper) throws AnimalDeadException, LacksSkillException {
        if (this.isDead()) throw new AnimalDeadException();
        if (this.isDead()) {
            throw new AnimalDeadException();
        }
        this._stroke();
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
            this._stroke();
        }
    }

    @Override
    protected void _stroke() {
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
            // If the animal is not a Lion, can't reproduce
            if (!(animal instanceof Lion)) {
                continue;
            }

            // We know the animal is a Lion, so cast it to a Lion
            Lion potentialMate = (Lion) animal;

            if (potentialMate.requestMate(this)) {
                this._mate = potentialMate;
                return;
            }
        }
    }
}
