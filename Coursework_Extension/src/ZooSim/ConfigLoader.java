package ZooSim;

import ZooSim.Animals.Animal;
import ZooSim.Enclosures.Enclosure;
import ZooSim.Exceptions.*;
import ZooSim.Parser.Lexer;
import ZooSim.Parser.Token;
import ZooSim.Parser.Tokens;
import ZooSim.Zookeepers.Zookeeper;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Loads Zoo config from a String,
 *
 * @author Huw Jones
 * @since 30/10/2015
 */

class ConfigLoader {
    private final Lexer _lexer;
    private final Simulator _zoo;
    private final HashMap<String, String> _animals = new HashMap<>();
    private Enclosure _currentEnclosure = null;

    public ConfigLoader(Simulator zoo, String config) {
        _lexer = new Lexer(config);
        _zoo = zoo;
    }

    public ConfigLoader(Simulator zoo, String config, String fileName) {
        _lexer = new Lexer(config, fileName);
        _zoo = zoo;
    }

    /**
     * Loads the zoo state
     *
     * @throws Exception Thrown back from lexer/config parser
     */
    public void loadConfig() throws Exception {
        if (!_parseZoo()) {
            ErrorMsg.Fatal("Config root 'zoo' not found in config file.");
        }
        try {
            _parseConfig();
        } catch (Exception ex) {
            ErrorMsg.Warning(ex.getMessage(), _lexer);
            ex.printStackTrace();
            throw ex;
        }
    }

    /**
     * Finds the start point of the zoo
     *
     * @return Boolean indicating whether the root was found
     */
    private boolean _parseZoo() {
        try {
            // Get token
            Token token = _lexer.getNextToken();

            // Loop through tokens until we find a "zoo"
            while (!token.getValue().equals("zoo")) {

                // If we reach end of file, return false to tell the loader we failed to load the config
                if (token.isType(Tokens.EOF)) {
                    return false;
                }

                // Get new token to recheck
                token = _lexer.getNextToken();
            }
            return true;
        } catch (Exception ex) {
            ErrorMsg.Fatal(ex.getMessage());
        }
        return false;
    }

    /**
     * Loops through the Lexer and performs the correct action based on the config file.
     *
     * @throws ConfigException               Thrown if there is a config syntax error
     * @throws ParseException                Thrown by the lexer if an unknown character was encountered.
     * @throws ClassNotFoundException        Thrown if a class was not found when loading.
     * @throws NoSuchMethodException         Thrown if a class was loaded but does not have a method.
     * @throws IllegalAccessException        Something
     * @throws InstantiationException        Something
     * @throws InvocationTargetException     Something
     * @throws EnclosureNotBuiltYetException Thrown if an enclosure was not built before adding animals
     * @throws EnclosureNotFoundException    Thrown if something was added to an unknown enclosure
     * @throws EnclosureFullException        Thrown if an enclosure is full and more animals are added
     */
    private void _parseConfig() throws
            ConfigException, ParseException, ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InstantiationException, InvocationTargetException, EnclosureNotBuiltYetException,
            EnclosureNotFoundException, EnclosureFullException {
        // Instantiate arrays
        String[] food_array = {"steak", "hay", "celery", "fruit", "fish", "ice cream"};
        String[] animals_array = {"Bear", "Chimpanzee", "Elephant", "Giraffe", "Gorilla", "Lion", "Penguin", "Tiger"};
        String[] zookeepers_array = {"Zookeeper", "physioZookeeper", "playZookeeper"};
        ArrayList<String> foods = new ArrayList<>(Arrays.asList(food_array));
        ArrayList<String> zookeepers = new ArrayList<>(Arrays.asList(zookeepers_array));
        for (String animal : animals_array) {
            _animals.put(animal.toLowerCase(), animal);
        }

        // Default foodstore assumed to be the Zoo's until an enclosure is created.
        Foodstore currentFoodStore = _zoo.getFoodstore();


        Token token = _lexer.getNextToken();

        // Loop through tokens until lexer gets to end of file
        while (!token.isType(Tokens.EOF)) {

            // Parse foodstore
            if (foods.contains(token.getValue())) {
                _parseFood(token, currentFoodStore);
            }

            // Parse enclosure
            if (token.getValue().toString().toLowerCase().equals("enclosure")) {
                _currentEnclosure = _parseEnclosure();
                currentFoodStore = _currentEnclosure.getFoodstore();
            }

            // Parse Zookeeper
            if (zookeepers.contains(token.getValue())) {
                _parseZookeeper(token);
            }

            // Parse animal
            if (_animals.containsKey(token.getValue().toString())) {

                // Check if we have a current enclosure
                if (_currentEnclosure == null) {
                    // Aha, wrote this late at night, stupidly verbose, but it's humorous so I'm keeping it :-)
                    throw new EnclosureNotBuiltYetException("Enclosure was not built before animals were added. You have now got dangerous (maybe, I haven't checked what you ordered yet) animals running around in your zoo! Just joking, you didn't seriously think I'd let you do that right?");
                }
                // Get the new animal
                Animal newAnimal = _parseAnimal(_animals.get(token.getValue().toString()));

                // Check to see if an enclosure has been specified
                token = _lexer.getNextToken();
                if (token.isType(Tokens.COMMA)) {

                    // Adding to enclosure specified
                    token = _lexer.getNextToken();
                    if (!token.isType(Tokens.INTEGER)) {
                        throw new ConfigException("Specified enclosure was an invalid type. (Not an integer)");
                    }
                    _zoo.getEnclosure((int) token.getValue() - 1).addAnimal(newAnimal);
                } else {
                    // Otherwise add it to the current enclosure
                    _currentEnclosure.addAnimal(newAnimal);
                }
            }
            token = _lexer.getNextToken(true);
        }
    }


    /**
     * Parses and adds food to the relevant foodstore
     *
     * @param foodToken Food token
     * @param foodstore Foodstore to add food to
     * @throws ConfigException Thrown if there is a syntax error in the config file
     * @throws ParseException  Thrown by the Lexer if it fails to parse some characters
     */
    private void _parseFood(Token foodToken, Foodstore foodstore) throws ParseException, ConfigException {
        // Get the type of food from the token
        String food = (String) foodToken.getValue();

        // Check for colon (syntax)
        Token separator = _lexer.getNextToken();
        if (!separator.isType(Tokens.COLON)) {
            throw new ConfigException("Statement not separated with a colon ':'.");
        }

        // Get amount of food/check it's an Integer
        Token value = _lexer.getNextToken();
        if (!value.isType(Tokens.INTEGER)) {
            throw new ConfigException("Type provided to " + food + " was not an integer.");
        }

        // Add food to the foodstore
        try {
            foodstore.requestFood(food);
            foodstore.addFood(food, (int) value.getValue());
        } catch (UnknownFoodException ex) {
            ErrorMsg.Warning(ex.getMessage());
        }
    }

    /**
     * Parses and creates the enclosure from the config file
     *
     * @return The new enclosure that was added to the zoo
     * @throws ParseException  Thrown if the parser encounters unknown character
     * @throws ConfigException Thrown if there is a syntax error in the config file
     */
    private Enclosure _parseEnclosure() throws ParseException, ConfigException {
        // Check for colon
        Token separator = _lexer.getNextToken();
        if (!separator.isType(Tokens.COLON)) {
            throw new ConfigException("Statement not separated with a colon ':'.");
        }

        // Get next token and check it is of type Tokens.INTEGER
        Token value = _lexer.getNextToken();
        if (!value.isType(Tokens.INTEGER)) {
            throw new ConfigException("Type provided to enclosure was not an integer.");
        }

        // Create new enclosure
        Enclosure newEnclosure = new Enclosure();

        // Add the waste
        newEnclosure.addWaste((int) value.getValue());

        // Add the enclosure to the zoo
        _zoo.addEnclosure(newEnclosure);

        // Return the enclosure so the reference can be stored in the currentEnclosure variable.
        return newEnclosure;
    }

    /**
     * Parses and creates the zookeeper from the config file
     *
     * @param zooToken Type of zookeeper
     * @throws ConfigException           Thrown if there is a config syntax error
     * @throws ParseException            Thrown by the lexer if an unknown character was encountered.
     * @throws ClassNotFoundException    Thrown if a class was not found when loading.
     * @throws NoSuchMethodException     Thrown if a class was loaded but does not have a method.
     * @throws IllegalAccessException    Something
     * @throws InstantiationException    Something
     * @throws InvocationTargetException Something
     */
    private void _parseZookeeper(Token zooToken) throws
            ConfigException, ParseException, ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InstantiationException, InvocationTargetException {
        String zooKeeper = zooToken.getValue().toString();

        // Check for colon
        Token separator = _lexer.getNextToken();
        if (!separator.isType(Tokens.COLON)) {
            throw new ConfigException("Statement not separated with a colon ':'.");
        }

            /*
            *
            * Dynamic class loader, woo! :-)
            *
            * Note: I wrote this method after I wrote the animal one, hence this is pretty much a copy + paste from
            * the _parseAnimal method.
            * Basically, I had the spec open on my second screen and I was working through it. The example config had
            * Zoo Keepers last, hence I wrote this method last.
             */

        // Get's the current class loader
        ClassLoader classLoader = this.getClass().getClassLoader();

        // Creates the new class (will throw an exception if class is not found, but this is handled)
        Class<?> newZookeeper = classLoader.loadClass(Simulator.class.getPackage().getName() + ".Zookeepers." + zooKeeper);

        // 1) Creates a new Zookeeper class (we know it's a Zookeeper as all Zookeepers inherit from Zookeeper,
        //    makes typing it a little easier. You could use an Object, but Animal is more suitable for
        //    aforementioned reasons.
        // 2) Then gets the constructor (and as all the classes inherit from Zookeeper, constructor *should* be the same)
        // 3) Then it creates a new instance using the parameters already parsed and stored.
        Zookeeper newZooKeeperClass = (Zookeeper) newZookeeper
                .getConstructor(
                        Enclosure.class,
                        Foodstore.class
                ).newInstance(
                        this._currentEnclosure,
                        this._zoo.getFoodstore()
                );
        this._zoo.addZookeeper(newZooKeeperClass);
    }

    /**
     * Parses and creates the animal
     *
     * @param animal Animal Type
     * @return Animal: the animal
     * @throws ConfigException           Thrown if there is a config syntax error
     * @throws ParseException            Thrown by the lexer if an unknown character was encountered.
     * @throws ClassNotFoundException    Thrown if a class was not found when loading.
     * @throws NoSuchMethodException     Thrown if a class was loaded but does not have a method.
     * @throws IllegalAccessException    Something
     * @throws InstantiationException    Something
     * @throws InvocationTargetException Something
     */
    private Animal _parseAnimal(String animal) throws
            ConfigException, ParseException, ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InstantiationException, InvocationTargetException {

        // Get net token and check it's a colon (indicates start of config settings)
        Token separator = _lexer.getNextToken();
        if (separator.isType(Tokens.EOL)) {
            // We have one entry per line, so throw an error if the required args are not found
            throw new ConfigException("Incorrect format. Gender, age and health are required.");
        } else if (!separator.isType(Tokens.COLON)) {
            throw new ConfigException("Statement not separated with a colon ':'.");
        }

        // Get gender
        Token gender = _lexer.getNextToken();
        if (gender.isType(Tokens.EOL)) {
            throw new ConfigException("Incorrect format. Gender, age and health are required.");
        } else if (!gender.isType(Tokens.CHAR)) {
            throw new ConfigException("Gender format invalid.");

            // Check we've only got male [m] or female [f] genders, no [t]s in this zoo (at the current time)
        } else if (!(gender.getValue().toString().toLowerCase().equals("m")
                || gender.getValue().toString().toLowerCase().equals("f"))) {
            throw new ConfigException("Gender format invalid.");
        }

        // Check for comma
        Token comma = _lexer.getNextToken();
        if (comma.isType(Tokens.EOL)) {
            throw new ConfigException("Incorrect format. Age and health are required.");
        } else if (!comma.isType(Tokens.COMMA)) {
            throw new ConfigException("Incorrect format. No comma separating values.");
        }

        // Get age
        Token age = _lexer.getNextToken();
        if (age.isType(Tokens.EOL)) {
            throw new ConfigException("Incorrect format. Age and health are required.");
        } else if (!age.isType(Tokens.INTEGER)) {
            throw new ConfigException("Incorrect format. Age is an integer");
        }

        // Check for comma
        comma = _lexer.getNextToken();
        if (comma.isType(Tokens.EOL)) {
            throw new ConfigException("Incorrect format. Age and health are required.");
        } else if (!comma.isType(Tokens.COMMA)) {
            throw new ConfigException("Incorrect format. No comma separating values.");
        }

        // Get health
        Token health = _lexer.getNextToken();
        if (age.isType(Tokens.EOL)) {
            throw new ConfigException("Incorrect format. Age and health are required.");
        } else if (!age.isType(Tokens.INTEGER)) {
            throw new ConfigException("Incorrect format. Age is an integer");
        }


        /*
        *
        * Dynamic class loader, woo! :-)
        *
         */

        // Get's the current class loader
        ClassLoader classLoader = this.getClass().getClassLoader();

        // Creates the new class (will throw an exception if class is not found, but this is handled)
        Class<?> newAnimalClass = classLoader.loadClass(Simulator.class.getPackage().getName() + ".Animals." + animal);

        // 1) Creates a new animal class (we know it's an animal as all animals inherit from Animal, makes typing it a
        //    little easier. You could use an Object, but Animal is more suitable for aforementioned reasons.
        // 2) Then gets the constructor (and as all the classes inherit from Animal, constructor *should* be the same)
        // 3) Then it creates a new instance using the parameters already parsed and stored.
        return (Animal) newAnimalClass
                .getConstructor(
                        char.class,
                        int.class,
                        int.class
                ).newInstance(
                        (gender.getValue().toString().charAt(0)),
                        age.getValue(),
                        health.getValue()
                );
    }

}