
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FarkleTest {

    @Test
    public void testSingleOne() {
        int[] dice = {1, 0, 0, 0, 0, 0};
        assertEquals(100, FarkleGame.calculateMeldScore(dice));
    }

    @Test
    public void testThreeOnes() {
        int[] dice = {1, 1, 1, 0, 0, 0};
        assertEquals(1000, FarkleGame.calculateMeldScore(dice));
    }

    @Test
    public void testFarkle() {
        int[] dice = {2, 3, 4, 6, 2, 3};
        assertTrue(FarkleGame.isFarkle(dice));
    }

    @Test
    public void testNotFarkle() {
        int[] dice = {1, 2, 3, 4, 5, 6};
        assertFalse(FarkleGame.isFarkle(dice));
    }
}