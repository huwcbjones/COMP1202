package ZooSim.Exceptions;

import ZooSim.Animals.Animal;
import ZooSim.Zookeepers.Zookeeper;

/**
 * Thrown if Zookeeper "lacks" the required skill when performing an action
 *
 * @author Huw Jones
 * @since 28/10/2015
 */
public class LacksSkillException extends Exception {

    public LacksSkillException(Zookeeper keeper, String task, Animal animal) {

        // I wanted to do, but Java is stupid and super() has to be the first statement in the constructor.
        // It's complicated but revolves around the fact that the parent class gets constructed first, before this
        // inherited class. Therefore the constructor for the parent must be called first.
        // But as shown below, you can do 1 line statements. So what's to stop you calling this.method() in the super()
        // method before this has been constructed and therefore throwing a runtime exception. But the compiler will
        // not detect this and let this runtime exception happen.
        // So back to the point, it's late at night and I can't be bothered with this, so the compiler should really
        // detect if any methods that are not instantiated throw exceptions if called before super() and the final class
        // (this) has been constructed.

        // Did a bit of further reading and it turns out that the compiler will optimise the 1 liner I have in the
        // super() constructor to what's below anyway... Waste of time, but learnt quite a bit.
        // Also found out that StringBuilder is useful when you are doing concatenation in loops (see .getState methods
        // on any of Enclosure, Foodstore, Animal) because the compiler isn't clever enough to detect and optimise as it
        // would normally with straight concatenation.
        // Anyway, the more you know! Oh, and I have yet to check this, but I'm not in the mood of learning Java byte
        // code and comparing the difference between what the compiler spits out depending on what constructs you use.

        /*
        StringBuilder builder = new StringBuilder();
        builder.append(keeper.getClass().getSimpleName());
        builder.append(" failed to perform task: ");
        builder.append(task);
        builder.append(" on animal: ");
        builder.append(animal.getClass().getSimpleName());
        super(builder.toString());
        */

        super(keeper.getClass().getSimpleName() + " failed to perform task: " + task + " on animal: " + animal.getClass().getSimpleName());
    }
}
