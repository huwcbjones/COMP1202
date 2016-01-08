package ZooSim.Zookeepers;

import ZooSim.Animals.Animal;
import ZooSim.Enclosures.Enclosure;
import ZooSim.ErrorMsg;
import ZooSim.Food.Food;
import ZooSim.Foodstore;

import java.util.ArrayList;

/**
 * Zookeeper looks after an enclosure and manages the zoo
 *
 * @author Huw Jones
 * @since 28/10/2015
 */
public class Zookeeper {

    @SuppressWarnings("FieldCanBeLocal")
    private final int _removeWastePM = 20;
    @SuppressWarnings("FieldCanBeLocal")
    private final int _addFoodPM = 20;
    private final Foodstore _zooFoodstore;
    private Enclosure _enclosure;

    public Zookeeper(Enclosure enclosure, Foodstore zooFoodstore) {
        this._enclosure = enclosure;
        this._zooFoodstore = zooFoodstore;
    }

    /**
     * Carries out tasks
     */
    public void aMonthPasses() {
        _restockEnclosure();
        _removeWaste();
        _treatAnimals();
    }

    /**
     * Removes waste from Enclosure
     */
    private void _removeWaste() {
        try {
            _enclosure.removeWaste(_removeWastePM);
        } catch (Exception ex) {
            ErrorMsg.Information(ex.getMessage());
        }
    }

    /**
     * Restocks Enclosure with food
     */
    private void _restockEnclosure() {
        try {
            // Loop through all food in enclosure
            for (Food.Type food : _enclosure.getFoodstore().getAvailableFood()) {

                // Remove 20 (or max available) from Zoo Foodstore
                int amountToAdd = _zooFoodstore.takeFood(food, _addFoodPM, true).getAmount();

                // Add that amount to this Enclosure's Foodstore
                _enclosure.getFoodstore().addFood(food, amountToAdd);
            }
        } catch (Exception ex) {
            ErrorMsg.Warning(ex.getMessage());
        }
    }

    /**
     * Picks 2 animals form the enclosure and treats them.
     * If the animal cannot be treated (as Zookeeper is unskilled) it removes it and picks another from the array
     */
    private void _treatAnimals() {
        // Treat 1st animal
        ArrayList<Animal> animals = new ArrayList<>(_enclosure.getAnimals());
        _treatAnimals(animals);

        // Reset array list because passed by reference
        animals = new ArrayList<>(_enclosure.getAnimals());
        // Treat 2nd animal
        _treatAnimals(animals);
    }

    private void _treatAnimals(ArrayList<Animal> animals) {

        // This is the animal we will treat at the end of the method
        // Use null so we can compare and not get nullPointerExceptions
        Animal animalToTreat = null;
        // Loop through animals
        for (Animal animal : animals) {

            // If this is the first animal, treat it anyway!
            if (animalToTreat == null) {
                animalToTreat = animal;
            }
            if (animal.getHealth() < animalToTreat.getHealth()) {
                animalToTreat = animal;
            }
        }
        if (animalToTreat == null) {
            return;
        }
        try {
            animalToTreat.treat(this);
        } catch (Exception ex) {
            animals.remove(animalToTreat);
            if (animals.size() != 0) {
                _treatAnimals(animals);
            }
        }
    }


}
