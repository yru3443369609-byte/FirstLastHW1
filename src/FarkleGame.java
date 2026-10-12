import java.awt.*;
import java.util.Random;
import java.util.Scanner;

public class FarkleGame {
    public static void displayDice(int[] dice, int[] used, char[] label) {
        System.out.println("Current Dice:");

        for (int i = 0; i < dice.length; i++) {
            if (used[i] == 0) {
                System.out.println(label[i] + ": " + dice[i] + " [Meld]");
            } else {
                System.out.println(label[i] + ": " + dice[i] + " [Available]");
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

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter your name:");
        String userName = scanner.nextLine().trim();
        if (userName.isEmpty()) {
            userName = "Unknown Player";
        }
        System.out.println("================================");
        System.out.println("|   Farkle Game by " +"[" +userName+"]"+ "       |");
        System.out.println("================================");

        char[] label = {'A', 'B', 'C', 'D', 'E', 'F'};
        Random randomNum = new Random();
        int[] dice = new int[6];

        for (int i = 0; i < dice.length; i++) {
            dice[i] = randomNum.nextInt(6) + 1;
        }


//            Let user choose dices
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

            System.out.println("Choose dices to meld (A-F), \"K\" to bank,\"R\" to reroll the Dice\n or \"Q\" to quit: ");
            String choice = scanner.nextLine().trim();
            System.out.println("Your choice is : " + choice);

            if (choice.trim().equalsIgnoreCase("K")) {
                totalScore += turnScore;

                System.out.println("Score banked: " + turnScore);
                System.out.println("Final total score: " + totalScore);

                gameOver = true;
                continue;
            }
            if (choice.equalsIgnoreCase("R")) {
                for (int i = 0; i < dice.length; i++) {
                    if (used[i] != 0) {
                        dice[i] = randomNum.nextInt(6) + 1;
                        unusedCount++;
                    }
                }
                if (unusedCount == 0) {
                    System.out.println("No available dice to reroll");
                    continue;
                }
                int[] currentUnusedDice = new int[unusedCount];
                int position = 0;

                for (int i = 0; i < dice.length; i++) {
                    if (used[i] != 0) {
                        currentUnusedDice[position] = dice[i];
                        position++;
                    }
                }

                if (isFarkle(currentUnusedDice)) {
                    System.out.println("Farkle! Your turn score is 0.");
                    turnScore = 0;
                    System.out.println("Player: " + userName);
                    System.out.println("Final total score: " + totalScore);
                    gameOver = true;
                }
                continue;
            }
            if (choice.trim().equalsIgnoreCase("Q")) {
                System.out.println();
                System.out.println("Player: " + userName);
                System.out.println("Banked score: " + totalScore);
                System.out.println("Current turn score: " + turnScore);
                System.out.println("Final total score: " + totalScore);
                gameOver = true;
                continue;
            }
            String[] choices = choice.trim().split(",");
            int[] selectedDice = new int[6];
            int selectedCount = 0;
            boolean validInput = true;

            for (int i = 0; i < choices.length; i++) {
                String input = choices[i].trim().toUpperCase();
                if (input.length() != 1 || input.charAt(0) < 'A' || input.charAt(0) > 'F') {
                    validInput = false;
                    break;
                }
                int index = input.charAt(0) - 'A';

                if (used[index] == 0) {
                    validInput = false;
                    break;
                }
                for (int j = 0; j < i; j++) {
                    if (choices[j].trim().equalsIgnoreCase(input)) {
                        validInput = false;
                        break;
                    }
                }
                if (!validInput) {
                    break;
                }
                selectedDice[selectedCount] = dice[index];
                selectedCount++;
            }
            if (!validInput) {
                System.out.println("Invalid input, please try again");
                continue;
            }
            int newMeldScore = calculateMeldScore(selectedDice);

            if (newMeldScore == 0) {
                System.out.println("This is not a valid meld. Try again.");
                continue;
            }

            for (int i = 0; i < choices.length; i++) {
                int index = choices[i].trim().toUpperCase().charAt(0) - 'A';

                used[index] = 0;
                meld[meldCount] = dice[index];
                meldCount++;
            }

            turnScore += newMeldScore;

            System.out.println("New meld score: " + newMeldScore);
            System.out.println("Turn score: " + turnScore);

            if (meldCount == 6) {
                System.out.println("**************");
                System.out.println("*  Hot Hand  *");
                System.out.println("**************");
                System.out.println();
                System.out.println("Would you like to roll 6 new dice, or bank and end your turn?");
                System.out.println("1. Roll all 6 dices again");
                System.out.println("2. Bank your score");

                String hotHandChoice = scanner.nextLine().trim();

                if (hotHandChoice.equals("1")) {
                    for (int i = 0; i < dice.length; i++) {
                        dice[i] = randomNum.nextInt(6) + 1;
                        used[i] = -1;
                    }
                    meldCount = 0;
                    meld = new int[6];
                    continue;
                } else if (hotHandChoice.equals("2")) {
                    totalScore += turnScore;
                    System.out.println("Final total score: " + totalScore);
                    gameOver = true;
                    continue;
                }else {
                    System.out.println("Invalid Choice");
                }
            }
        }
    }
}