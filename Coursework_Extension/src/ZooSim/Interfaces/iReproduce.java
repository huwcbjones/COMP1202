package ZooSim.Interfaces;

import ZooSim.Animals.Animal;
import ZooSim.Exceptions.AnimalDeadException;
import ZooSim.Exceptions.AnimalNotHealthyEnoughException;
import ZooSim.Exceptions.AnimalPregnantException;

/**
 * Reproduceable Interface
 *
 * @author Huw Jones
 * @since 28/11/2015
 */
public interface iReproduce {

    /**
     * Returns true if animal already has a mate
     *
     * @return boolean
     */
    boolean hasMate();

    /**
     * Animal finds a mate to mate with
     */
    void findMate();

    /**
     * Part of the mating ritual, animal requests another animal to mate with it
     *
     * @param requestee The animal requesting to mate with this animal
     * @return True if the request was granted
     */
    boolean requestMate(Animal requestee);

    /**
     * Returns if animal is pregnant
     *
     * @return Boolean representing state of pregnancy
     */
    boolean isPregnant();

    /**
     * Gets the animal pregnant
     *
     * @throws AnimalPregnantException         Thrown if the animal is already pregnant
     * @throws AnimalNotHealthyEnoughException Thrown if the animal is not healthy enough to get pregnant
     */
    void getPregnant() throws AnimalPregnantException, AnimalNotHealthyEnoughException;

    /**
     * Gets gestation period of animal
     *
     * @return int, number of months of gestation
     */
    int getGestation();

    /**
     * Animal gives birth
     *
     * @throws AnimalDeadException Thrown if the mother has a miscarriage
     */
    void giveBirth() throws AnimalDeadException;

    /**
     * Checks up on mate
     */
    void checkMate();
}
