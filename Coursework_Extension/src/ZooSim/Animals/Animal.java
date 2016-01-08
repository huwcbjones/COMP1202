package ZooSim.Animals;

import ZooSim.Enclosures.Enclosure;
import ZooSim.ErrorMsg;
import ZooSim.Exceptions.*;
import ZooSim.Food.Food;
import ZooSim.Foodstore;
import ZooSim.Interfaces.iReproduce;
import ZooSim.Zookeepers.Zookeeper;
import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;
import java.util.Random;

/**
 * Template Animal class
 *
 * @author Huw Jones
 * @since 27/10/2015
 */
public abstract class Animal implements iReproduce {
    /**
     * ArrayList&lt;Food.Type&gt; of foods the animal eats.
     */
    final ArrayList<Food.Type> _eats = new ArrayList<>();
    /**
     * Gender of animal (either m or f).
     */
    private final char _gender;
    /**
     * Average age of the animal in months before it dies.
     */
    private final int _lifeExpectancy;
    /**
     * Standard Deviation of Life Expectancy
     */
    private final double _lifeExpectancyDeviation;
    /**
     * Gestation Period of the animal
     */
    private final int _gestationPeriod;
    /**
     * Age of animal in months.
     */
    int _age;
    /**
     * Animal's health. Range of 0-10. 10 being max, 0 being dead.
     */
    int _health;
    /**
     * Mate of the animal
     */
    Animal _mate = null;
    /**
     * Enclosure animal belongs to
     */
    private Enclosure _enclosure;
    /**
     * Pregnancy state of the animal
     */
    private int _pregnancyState = -1;


    Animal(char gender, int age, int health, int lifeExpectancy, int gestationPeriod) {
        this._gender = gender;
        this._age = age;
        this._health = health;
        this._lifeExpectancy = lifeExpectancy;
        this._lifeExpectancyDeviation = Math.sqrt(lifeExpectancy);
        this._gestationPeriod = gestationPeriod;
    }

    /**
     * Get's the state of the Animal. Used for saving ZooSim state
     *
     * @return ZooSim compatible config String
     */
    public String getState() {
        String state = this.getClass().getSimpleName().toLowerCase() + ":";
        state += ((Character) this.getGender()).toString().toUpperCase() + ",";
        state += this.getAge() + ",";
        state += this._health + "\n";
        return state;
    }

    /**
     * Returns an int representing the life expectancy of the animal
     *
     * @return int Life Expectancy
     */
    public final int getLifeExpectancy() {
        return this._lifeExpectancy;
    }

    /**
     * Checks whether the animal can eat the food
     *
     * @param food Food to check is edible
     * @return boolean if animal can eat food
     */
    public final boolean canEat(Food.Type food) {
        return this._eats.contains(food);
    }

    /**
     * Returns health of the animal
     *
     * @return int Health
     */
    public int getHealth() {
        return this._health;
    }

    /**
     * Advances the simulator 1 month
     */
    public void aMonthPasses() {
        this.age();
        try {
            this.eat();
        } catch (Exception ex) {
            ErrorMsg.Warning(ex.getMessage());
        }

        this.checkMate();

        if (!this.isPregnant()) return;

        if (this._gestationPeriod == this._pregnancyState) {
            try {
                this.giveBirth();
                ErrorMsg.Information("A new animal has been born! :-)");
            } catch (AnimalDeadException ex) {
                ErrorMsg.Warning("A Animal has had a miscarriage.");
            }
        } else {
            this._pregnancyState++;

            // When pregnant, animal health decreases because of having to support an unborn child
            this.decreaseHealth(1);
        }
    }

    /**
     * Simulates the animal aging
     */
    private void age() {
        this._age++;
        this.getOld();
    }

    /**
     * Animal eats
     *
     * @throws NotEnoughFoodException    Thrown if there is not enough food
     * @throws UnknownEnclosureException Thrown if the animal has not been assigned an enclosure
     * @throws AnimalDeadException       Thrown if the animal is dead
     */
    public void eat() throws NotEnoughFoodException, UnknownEnclosureException, AnimalDeadException {
        if (this.isDead()) {
            throw new AnimalDeadException();
        }
        if (this._enclosure == null) {
            throw new UnknownEnclosureException("Enclosure for animal was not found.");
        }
        Foodstore store = this._enclosure.getFoodstore();
        ArrayList<Food.Type> availableFood = store.getAvailableFood();

        // Loop through food the animal eats
        for (Food.Type foodType : this._eats) {

            try {
                // Check if food is available in Foodstore
                if (!availableFood.contains(foodType)) {
                    store.requestFood(foodType);
                    continue;
                }
            } catch (UnknownFoodException ex) {
                continue;
            }

            // Take 1 item of food
            try {
                Food food = store.takeFood(foodType);
                this.increaseHealth(food.getHealth());
                this._enclosure.addWaste(food.getWaste());
                return;
            } catch (Exception ex) {
            }
        }

        // If no food was eaten, reduce health and throw an exception
        this.decreaseHealth(2);
        throw new NotEnoughFoodException("No food that animal eats available for consumption.");
    }

    /**
     * Decreases the animal's health by an amount
     *
     * @param amount Amount to decrease animal's health by
     */
    final void decreaseHealth(int amount) {

        this._health -= amount;
        if (this._health > 10) {
            this._health = 10;
        } else if (this._health < 0) {
            this._health = 0;
        }
    }

    /**
     * Simulates the animal getting old
     */
    private void getOld() {
        // Checks whether animal should die
        if (this.shouldDie()) {

            // If so, set the health to 0
            this._health = 0;
        }
    }

    /**
     * Returns true if the animal is dead
     *
     * @return boolean
     */
    public final boolean isDead() {
        return this._health == 0;
    }

    /**
     * Increases the animal's health by an amount
     *
     * @param amount Amount to increase animal's health by
     */
    final void increaseHealth(int amount) {

        this._health += amount;
        if (this._health > 10) {
            this._health = 10;
        } else if (this._health < 0) {
            this._health = 0;
        }
    }

    /**
     * Treats the animal (via their mate)
     *
     * @param animal Animal trying to treat their mate
     * @throws AnimalDeadException Thrown if the animal is dead
     */
    protected abstract void treat(Animal animal) throws AnimalDeadException;

    /**
     * Gets whether the animal should die.
     * Uses a normal distribution with the mean the life expectancy to model the animal's age.
     * Uses the square root of the life expectancy as standard deviation
     *
     * @return True if the animal should die
     */
    private boolean shouldDie() {
        // Create a new normal distribution (using org.apache.commons.math (v3))
        NormalDistribution distribution = new NormalDistribution(this._lifeExpectancy, this._lifeExpectancyDeviation);
        // Gets cumulative probability of the animals age
        double probability = distribution.cumulativeProbability(this._age);

        // Gets a random int between 0 and 100
        int ranInt = new Random().nextInt(100);

        // If the ranInt is less than the probability, animal should die
        return (ranInt < probability * 100);
    }

    /**
     * Treats the animal
     *
     * @param keeper Keeper performing the treat
     * @throws AnimalDeadException Thrown if the animal is dead
     * @throws LacksSkillException Thrown if the keeper lacks the skill to treat the animal
     */
    public abstract void treat(Zookeeper keeper) throws AnimalDeadException, LacksSkillException;

    /**
     * Sets the enclosure of the animal
     *
     * @param enclosure Enclosure to set
     */
    public final void setEnclosure(Enclosure enclosure) {
        this._enclosure = enclosure;
    }

    /**
     * Gets the animal pregnant
     *
     * @throws AnimalPregnantException         Thrown if the animal is already pregnant
     * @throws AnimalNotHealthyEnoughException Thrown if the animal is not healthy enough to get pregnant
     */
    @Override
    public final void getPregnant() throws AnimalPregnantException, AnimalNotHealthyEnoughException {
        // Male animals can't carry children!
        if (this.isMale()) return;

        if (this.isPregnant()) {
            throw new AnimalPregnantException("The animal can't get pregnant, she's already carrying a child.");
        }
        if (this._health < 8) {
            throw new AnimalNotHealthyEnoughException("Animals need 8 or more health to get pregnant");
        }

        this._pregnancyState = 0;
    }

    /**
     * Returns gender of animal as 'f' = female, 'm' = male
     *
     * @return char Gender Code
     */
    public final char getGender() {
        return this._gender;
    }

    /**
     * Returns age of animal
     *
     * @return int Age
     */
    public final int getAge() {
        return this._age;
    }

    /**
     * Gets an ArrayList of potential mates
     * @return ArrayList of potential mates
     * @throws NoSuchMethodException Thrown if the animal is <strong>not</strong> mate
     */
    final ArrayList<Animal> getPotentialMates() throws NoSuchMethodException {
        if (this.isFemale()) {
            throw new NoSuchMethodException();
        }
        ArrayList<Animal> potentialMates = new ArrayList<>();
        for (Animal animal : this._enclosure.getAnimals()) {
            // No necrophilia here...
            if (animal.isDead()) continue;

            // Do the gender check first before the cast, because there's no point casting if the genders aren't
            // correct first...
            // If genders match, animals can't reproduce
            if (this.getGender() == animal.getGender()) continue;

            // If the animal has a mate, can't mate with it
            if ((animal).hasMate()) continue;

            potentialMates.add(animal);
        }

        return potentialMates;
    }

    /**
     * Returns true if animal is male
     *
     * @return boolean
     */
    public final boolean isMale() {
        return this._gender == 'm' || this._gender == 'M';
    }

    /**
     * Returns true if animal already has a mate
     *
     * @return boolean
     */
    @Override
    public final boolean hasMate() {
        return (this._mate != null);
    }

    /**
     * Part of the mating ritual, animal requests another animal to mate with it
     *
     * @param requestee The animal requesting to mate with this animal
     * @return True if the request was granted
     */
    @Override
    public final boolean requestMate(Animal requestee) {
        if (this.isDead()) return false;
        if (this.hasMate()) return false;
        if (!requestee.getClass().equals(this.getClass())) return false;

        // Gets a random int between 0 and 100
        int ranInt = new Random().nextInt(100);

        // The less health the animal has, the less likely it is to accept the mating request

        boolean willMate = ranInt < this._health * 10;
        if (willMate) {
            this._mate = requestee;
        }
        return willMate;
    }

    /**
     * Returns if animal is pregnant
     *
     * @return Boolean representing state of pregnancy
     */
    @Override
    public final boolean isPregnant() {
        return this._pregnancyState != -1;
    }

    /**
     * Gets gestation period of animal
     *
     * @return int, number of months of gestation
     */
    @Override
    public final int getGestation() {
        return this._gestationPeriod;
    }

    /**
     * Animal gives birth
     *
     * @throws AnimalDeadException Thrown if the mother has a miscarriage
     */
    @Override
    public final void giveBirth() throws AnimalDeadException {
        // I'm actually dreading this implementation of reflection...
        // I thought about implementing giveBirth in every animal... but I thought it'd be better to implement it once
        // So here we go...
        // If you thought the previous uses of reflection were bad... urmmm... prepare yourself

        // Get gender randomly (probability of m/f is 0.5 because of how the chromosomes work)
        char gender = (new Random().nextBoolean()) ? 'm' : 'f';

        try {

            // Get's the current class loader
            ClassLoader classLoader = this.getClass().getClassLoader();

            // Creates the new class (will throw an exception if class is not found, but this is handled)
            Class<?> newAnimalClass = classLoader.loadClass(this.getClass().getName());

            // New babies are born with half health
            Animal baby = (Animal) newAnimalClass
                    .getConstructor(
                            char.class,
                            int.class,
                            int.class
                    ).newInstance(
                            gender,
                            0,
                            5
                    );

            // I pity you

            try {
                this._enclosure.addAnimal(baby);
            } catch (EnclosureFullException ex) {
                throw new AnimalDeadException();
            }
            this._pregnancyState = -1;
        } catch (Exception ex) {
            ErrorMsg.Fatal("I fucked up using Reflection.");
        }
    }

    /**
     * Returns true if animal is female
     *
     * @return boolean
     */
    public final boolean isFemale() {
        return this._gender == 'f' || this._gender == 'F';
    }

    /**
     * Checks up on mate
     */
    @Override
    public final void checkMate() {
        if (this._mate == null) {
            this.findMate();
            return;
        }

        if (this._mate.isDead()) {
            ErrorMsg.Information("The Animal's partner has died. :-(");
            this._mate = null;
            return;
        }
        if (this._health >= 9) {
            try {
                this._mate.treat(this);
            } catch (AnimalDeadException e) {
                return;
            }
        }

        if (this._mate.isPregnant()) return;

        try {
            if (new Random().nextInt(100) < 80) this._mate.getPregnant();
        } catch (AnimalNotHealthyEnoughException ex) {
            if (this._health >= 9) {
                try {
                    this._mate.treat(this);
                } catch (AnimalDeadException e) {

                }
            }
        } catch (AnimalPregnantException ex) {

        }
    }
}
