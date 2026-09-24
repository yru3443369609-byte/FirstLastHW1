import java.awt.*;
import java.util.Random;
import java.util.Scanner;

public class FarkleGame {
    public static void displayDice(int[] dice, int[] used, char[] label) {
        System.out.println("Current Dice:");

        for (int i = 0; i < dice.length; i++) {
            if (used[i] == 0) {
                System.out.println(label[i] + ": " + dice[i] + " (Meld)");
            } else {
                System.out.println(label[i] + ": " + dice[i] + " (Unused)");
            }
        }
    }
    public static boolean isFarkle(int[] dice) {

        int[] count = new int[7];
        for (int i = 0; i < dice.length; i++) {
            count[dice[i]]++;
        }
//            If is triple or more
        for (int i = 0; i <= 6; i++) {
            if (count[i] >= 3) {
                return false;
            }
        }
//            if is three pair
        int pairCount = 0;
        for (int i = 1; i < 7; i++) {
            if (count[i] == 2) {
                pairCount++;
            }
        }
        if (pairCount == 3) {
            return false;
        }
//            check if is straight
        boolean isStraight = true;
        for (int i = 1; i <= 6; i++) {
            if (count[i] != 1) {
                isStraight = false;
            }
        }
        if (isStraight) {
            return false;
        }
//            single 1 or 5
        for (int i = 0; i < dice.length; i++) {
            if (dice[i] == 1 || dice[i] == 5) {
                return false;
            }
        }
        return true;
    }

    public static int calculateMeldScore(int[] meld) {
        int meldScore = 0;
        int meldDiceCount = 0;
        int[] meldDice = {0, 0, 0, 0, 0, 0};
        boolean isValidMeld = false;
        {   // Calculate the meld
            for (int i = 0; i < 6; i++) {
                if (meld[i] != 0) {
                    meldDice[meldDiceCount] = meld[i];
                    meldDiceCount++;
                }
            }

            int[] meldSizes = {0, 0, 0, 0, 0, 0, 0};
            for (int i = 0; i < 6; i++) {
                meldSizes[meldDice[i]]++;
            }
            // Check straight
            boolean isStraight = true;
            for (int i = 1; i < 7; i++) {
                if (meldSizes[i] != 1) {
                    isStraight = false;
                }
            }
            if (isStraight) {
                meldScore += 1000;
            } else {
                // Check three pairs
                int pairCount = 0;
                for (int i = 1; i < 7; i++) {
                    if (meldSizes[i] == 2) {
                        pairCount++;
                    }
                }
                if (pairCount == 3) {
                    meldScore += 750;
                } else {
                    // Check triples +
                    boolean isTriple = false;
                    for (int i = 1; i < 7; i++) {
                        if (meldSizes[i] >= 3) {
                            // Found triple (or more!)
                            isTriple = true;
                            int triplePoints = 0;
                            if (i == 1) {
                                triplePoints = 1000;
                            } else {
                                triplePoints = i * 100;
                            }
                            if (meldSizes[i] > 3) {
                                triplePoints += (meldSizes[i] - 3) * 100 * i;
                            }
                            meldScore += triplePoints;
                        }
                    }

                    // Add 1's & 5's if unused
                    if (meldSizes[1] < 3) {
                        meldScore += meldSizes[1] * 100;
                    }

                    if (meldSizes[5] < 3) {
                        meldScore += meldSizes[5] * 50;
                    }
                }
            }
        }
        return meldScore;
    }

    public static void main(String[] args) {
        char[] label = {'A', 'B', 'C', 'D', 'E', 'F'};
        Random randomNum = new Random();
        int[] dice = new int[6];

        for (int i = 0; i < dice.length; i++) {
            dice[i] = randomNum.nextInt(6) + 1;
            System.out.println(label[i] + ": " + dice[i]);
        }


//            Let user choose dices
        Scanner scanner = new Scanner(System.in);
        int[] used = {-1, -1, -1, -1, -1, -1};
        int[] meld = {0, 0, 0, 0, 0, 0};
        int meldCount = 0;
        boolean gameOver = false;
        int turnScore = 0;
        int totalScore = 0;

        while (!gameOver) {
            int[] unusedDice = new int[6];
            int unusedCount = 0;

            displayDice(dice, used, label);

            System.out.println("Choose dices to meld (A-F), K to bank or Q to quit: ");
            String choice = scanner.nextLine();
            System.out.println("Your choice is : " + choice);

            if (choice.trim().equalsIgnoreCase("K")) {
                System.out.println();
                System.out.println("Final Meld:");
                for (int i = 0; i < meld.length; i++) {
                    if (meld[i] != 0) {
                        System.out.println(label[i] + ": " + meld[i]);
                    }
                }

                System.out.println("Final Unused Dice:");
                for (int i = 0; i < dice.length; i++) {
                    if (used[i] != 0) {
                        System.out.println(label[i] + ": " + dice[i]);
                    }
                }

                System.out.println("Meld Score: " + turnScore);

                totalScore += turnScore;
                gameOver = true;
                continue;
            }
            if (choice.trim().equals("Q")) {
                gameOver = true;
                continue;
            }
            String[] choices = choice.trim().split(",");
            for (int i = 0; i < choices.length; i++) {
                int index = choices[i].trim().charAt(0) - 'A';
                if (used[index] == 0) {
                    System.out.println("This position is used");
                } else {
                    used[index] = 0;
                    meld[meldCount] = dice[index];
                    meldCount++;
                    System.out.println("Dice number is :" + dice[index]);
                    turnScore = calculateMeldScore(meld);
                }
                System.out.println("The recent Score is : " + turnScore);
            }

            for (int i = 0; i < dice.length; i++) {
                if (used[i] != 0) {
                    dice[i] = randomNum.nextInt(6) + 1;
                    System.out.println(label[i] + ": " + dice[i]);
                    unusedDice[unusedCount] = dice[i];
                    unusedCount++;
                }
            }
                int[] currentUnusedDice = new int[unusedCount];

                for (int i = 0; i < unusedCount; i++) {
                    currentUnusedDice[i] = unusedDice[i];
                }

                if (isFarkle(currentUnusedDice)) {
                    System.out.println("The dice is Farkle");
                    turnScore = 0;
                    gameOver = true;
                }
                if (meldCount == 6) {
                    totalScore += turnScore;
                    gameOver = true;
                }
            }
            System.out.println("The Finally Score is : " + totalScore);
        }
    }
