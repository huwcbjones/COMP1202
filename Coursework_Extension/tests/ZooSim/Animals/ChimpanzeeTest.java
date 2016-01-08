package ZooSim.Animals;

import ZooSim.Enclosures.Enclosure;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 28/11/2015
 */
public class ChimpanzeeTest {

    @Test
    public void testMate () throws Exception {
        Chimpanzee maleChimp = new Chimpanzee('m', 8, 8);
        Chimpanzee femaleChimp = new Chimpanzee('f', 8, 9);

        // Add chimps to enclosure
        Enclosure enclosure = new Enclosure();
        enclosure.addAllAnimal(new Animal[]{maleChimp, femaleChimp});

        do {
            maleChimp.findMate();
        } while (!maleChimp.hasMate());

        assertTrue(maleChimp.hasMate());
        assertTrue(femaleChimp.hasMate());
    }

    @Test
    public void testGetPregnant () throws Exception {
        Chimpanzee maleChimp = new Chimpanzee('m', 8, 8);
        Chimpanzee femaleChimp = new Chimpanzee('f', 8, 9);

        // Add chimps to enclosure
        Enclosure enclosure = new Enclosure();
        enclosure.addAllAnimal(new Animal[]{maleChimp, femaleChimp});

        do {
            maleChimp.findMate();
        } while(!maleChimp.hasMate());

        maleChimp.checkMate();

        assertTrue(femaleChimp.isPregnant());
    }

    @Test
    public void testGiveBirth () throws Exception {
        Enclosure enclosure = new Enclosure();
        Chimpanzee femaleChimp = new Chimpanzee('f', 4, 10);
        enclosure.addAnimal(femaleChimp);
        do {
            femaleChimp.getPregnant();
        } while(!femaleChimp.isPregnant());

        do {
            femaleChimp.aMonthPasses();
        } while(femaleChimp.isPregnant());

        femaleChimp.aMonthPasses();

        assertEquals(enclosure.size(), 2);
    }

    @Test
    public void testCheckMate () throws Exception {

    }
}