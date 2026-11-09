
import java.util.Random;

public class Dice {

    public static int[] rollSix(Random random) {
        int[] dice = new int[6];

        for (int i = 0; i < dice.length; i++) {
            dice[i] = random.nextInt(6) + 1;
        }

        return dice;
    }

    public static int rerollAvailable(int[] dice, int[] used, Random random) {

        int unusedCount = 0;

        for (int i = 0; i < dice.length; i++) {
            if (used[i] != 0) {
                dice[i] = random.nextInt(6) + 1;
                unusedCount++;
            }
        }

        return unusedCount;
    }

    public static void displayDice(
            int[] dice, int[] used, char[] label) {

        System.out.println("Current Dice:");

        for (int i = 0; i < dice.length; i++) {
            if (used[i] == 0) {
                System.out.println(label[i] + ": " + dice[i] + " [Meld]");
            } else {
                System.out.println(label[i] + ": " + dice[i] + " [Available]");
            }
        }
    }
}
