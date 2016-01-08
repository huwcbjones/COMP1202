package ZooSim.Enclosures;

import ZooSim.Animals.Animal;
import ZooSim.ErrorMsg;
import ZooSim.Exceptions.EnclosureFullException;
import ZooSim.Exceptions.WasteEmptyException;
import ZooSim.Foodstore;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Enclosure keeps Animal(s), waste and has a Foodstore.
 *
 * @author Huw Jones
 * @since 27/10/2015
 */
public class Enclosure {
    private final Foodstore _foodstore = new Foodstore();
    private ArrayList<Animal> _animals = new ArrayList<>();
    private int _amountOfWaste = 0;
    private boolean _isIterating = false;
    private ArrayList<Animal> _animalsToAdd = new ArrayList<>();

    /**
     * Adds an animal to the enclosure
     *
     * @param animal The animal to add to the Enclosure
     * @throws EnclosureFullException Raised if enclosure is full
     */
    public void addAnimal(Animal animal) throws EnclosureFullException {
        // Check to see if the enclosure is full, otherwise throw EnclosureFullException
        if (this._animals.size() + this._animalsToAdd.size() == 20) {
            throw new EnclosureFullException();
        }

        // If we are iterating, add the animals to the add list
        if (this._isIterating) {
            _animalsToAdd.add(animal);
            return;
        }

        // Add animal to enclosure, then set it's enclosure to this so it knows where to get food from
        this._animals.add(animal);
        animal.setEnclosure(this);
    }

    /**
     * Removes an animal from the enclosure
     *
     * @param animal The animal to remove from the enclosure
     * @throws ArrayIndexOutOfBoundsException Raised if animal is not in enclosure
     */
    public void removeAnimal(Animal animal) throws ArrayIndexOutOfBoundsException {
        if (!this._animals.contains(animal)) {
            throw new ArrayIndexOutOfBoundsException("Animal " + animal.toString() + " was not found in Enclosure.");
        }
        this._animals.remove(animal);
    }

    /**
     * Removes an amount of waste from the enclosure
     *
     * @param amount Amount of waste to remove
     * @throws WasteEmptyException Raised if the amount of waste removed is greater than waste.
     */
    public void removeWaste(int amount) throws WasteEmptyException {
        // Check waste isn't empty before removing more waste
        if (this._amountOfWaste - amount < 0) {
            throw new WasteEmptyException("No more waste can be removed from Enclosure.");
        }
        this._amountOfWaste -= amount;
    }

    /**
     * Adds waste to the enclosure
     *
     * @param amount Amount of waste to add
     */
    public void addWaste(int amount) {
        this._amountOfWaste += amount;
    }

    /**
     * Returns the amount of waste in the enclosure
     *
     * @return Amount of waste
     */
    public int getWasteSize() {
        return _amountOfWaste;
    }

    /**
     * Returns the enclosure's food store
     *
     * @return Enclosure Foodstore
     */
    public Foodstore getFoodstore() {
        return this._foodstore;
    }

    /**
     * Gets size of enclosure
     *
     * @return Amount of animals
     */
    public int size() {
        return this._animals.size();
    }

    /**
     * Get's remaining capacity of the enclosure
     *
     * @return Amount of free space
     */
    public int getRemainingCapacity() {
        return 20 - this._animals.size();
    }

    /**
     * Runs the simulation and calls aMonthPasses on the relevant classes
     */
    public void aMonthPasses() {
        ArrayList<Animal> animals = new ArrayList<>();

        this._isIterating = true;
        for (Animal animal : this._animals) {
            animal.aMonthPasses();
            if (!animal.isDead()) {
                animals.add(animal);
            } else {
                String animalType = animal.getClass().getSimpleName();

                ErrorMsg.Information("One of your " + animalType + "s has died and gone to " + animalType + "y heaven. It was a grand old age of " + animal.getAge());
                ErrorMsg.Information("RIP in Pepe " + animalType + " #" + System.identityHashCode(animal) + ". You will be sorely missed. :(");
            }
        }
        this._isIterating = false;

        this._animals = animals;

        try {
            Animal[] newAnimals = new Animal[this._animalsToAdd.size()];
            newAnimals = this._animalsToAdd.toArray(newAnimals);
            this.addAllAnimal(newAnimals);
        } catch (EnclosureFullException e) {
            ErrorMsg.Fatal("Failed to add all animals to enclosure.");
        }
        this._animalsToAdd = new ArrayList<>();
    }

    /**
     * Adds all animals in an array to the enclosure.
     * This method will throw an exception if the entire array of animals cannot fit in the enclosure. So please check
     * the enclosure size before adding animals.
     *
     * @param animals Animal[]'s to add
     * @throws EnclosureFullException Thrown if all the animals won't fit in the enclosure
     */
    public void addAllAnimal(Animal[] animals) throws EnclosureFullException {
        // Check to see if the enclosure is full, otherwise throw EnclosureFullException
        if (this._animals.size() == 20) {
            throw new EnclosureFullException();
        } else if (this._animals.size() + this._animalsToAdd.size() + animals.length > 20) {
            throw new EnclosureFullException();
        }

        // If we are iterating, add the animals to the add list
        if (this._isIterating) {
            _animalsToAdd.addAll(Arrays.asList(animals));
            return;
        }

        // Loop through animals in array
        for (Animal animal : animals) {
            // Add animal to enclosure, then set it's enclosure to this so it knows where to get food from
            this._animals.add(animal);
            animal.setEnclosure(this);
        }
    }

    /**
     * Get's the current Enclosure state as a String. Compatible with the config file.
     *
     * @return State of the Enclosure
     */
    public String getState() {
        StringBuilder state = new StringBuilder();
        state.append("enclosure:");
        state.append(this._amountOfWaste);
        state.append("\n");
        state.append(this._foodstore.getState());
        for (Animal animal : this._animals) {
            state.append(animal.getState());
        }
        return state.toString();
    }

    /**
     * Gets animals in the enclosure
     *
     * @return ArrayList of animals
     */
    public ArrayList<Animal> getAnimals() {
        return this._animals;
    }
}
