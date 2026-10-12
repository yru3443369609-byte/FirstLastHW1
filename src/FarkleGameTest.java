import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FarkleGameTest {

    @Test
    public void testStraight() {
        int[] dice = {1, 2, 3, 4, 5, 6};
        assertEquals(100, FarkleGame.calculateMeldScore(dice));
        System.out.println("Test1 passed!");
    }

    @Test
    public void testThreePair() {
        int[] dice = {1, 1, 2, 2, 3, 3};
        assertEquals(1000, FarkleGame.calculateMeldScore(dice));
        System.out.println("Test2 passed!");

    }

    @Test
    public void testFarkle() {
        int[] dice = {2, 3, 4, 6, 2, 3};
        assertTrue(FarkleGame.isFarkle(dice));
        System.out.println("Test3 passed!");

    }

    @Test
    public void testNotFarkle() {
        int[] dice = {1, 2, 3, 4, 5, 6};
        assertFalse(FarkleGame.isFarkle(dice));
        System.out.println("Test4 passed!");

    }
}