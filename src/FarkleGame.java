import java.util.Random;
import java.util.Scanner;

public class FarkleGame {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("================================");
        System.out.println("|   Farkle Game by [Yin Ru]    |");
        System.out.println("================================");

//         Ask for target score
        System.out.println("How many points would you like to play in this game? [10000]");
        String targetInput = scanner.nextLine().trim();

        int targetScore = 10000;

//         Ask for number of players
        System.out.println("How many players?");
        String playerCountInput = scanner.nextLine().trim();

        int playerCount = 1;

        if (!playerCountInput.isEmpty()) {
            try {
                int inputCount = Integer.parseInt(playerCountInput);

                if (inputCount > 0) {
                    playerCount = inputCount;
                } else {
                    System.out.println("Invalid number. Using 1 player.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Using 1 player.");
            }
        }

//         Create players
        Player[] players = new Player[playerCount];

        String[] defaultNames = {"Baby", "Scary", "Sporty", "Posh"};

        for (int i = 0; i < playerCount; i++) {

            String defaultName;

            if (playerCount == 1) {
                defaultName = "Unknown Player#1";
            } else if (i < defaultNames.length) {
                defaultName = defaultNames[i];
            } else {
                defaultName = "Player# " + (i + 1);
            }

            System.out.println("What is player " + (i + 1) + "'s name? [" + defaultName + "]");

            String enteredName = scanner.nextLine().trim();

            if (enteredName.isEmpty()) {
                enteredName = defaultName;
            }

            players[i] = new Player(enteredName);
        }

//         Check target score input
        if (!targetInput.isEmpty()) {
            try {
                int inputScore = Integer.parseInt(targetInput);

                if (inputScore > 0) {
                    targetScore = inputScore;
                } else {
                    System.out.println("Invalid score. Using default 10000.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Using default 10000.");
            }
        }

        char[] label = {'A', 'B', 'C', 'D', 'E', 'F'};
        Random randomNum = new Random();

        boolean gameOver = false;
        boolean quitGame = false;

        int currentPlayerIndex = 0;
        int turnNumber = 1;
        int totalScore = 0;
        String playerName = "";

        boolean finalRound = false;
        int finalTurnsRemaining = 0;
        int winnerIndex = -1;

//         Main game loop
        while (!gameOver) {

            playerName = players[currentPlayerIndex].getName();
            totalScore = players[currentPlayerIndex].getTotalScore();

//             Roll six dice
            int[] dice = Dice.rollSix(randomNum);

//             -1 means available, 0 means already selected
            int[] used = {-1, -1, -1, -1, -1, -1};

            int meldCount = 0;
            int turnScore = 0;
            boolean turnOver = false;

            System.out.println("=================================");
            System.out.println("Turn " + turnNumber);
            System.out.println("Player: " + playerName);
            System.out.println("Total Score: " + totalScore);
            System.out.println("Target Score: " + targetScore);
            System.out.println("=================================");

//             Current player's turn
            while (!turnOver && !gameOver && totalScore < targetScore) {

                int[] unusedDice;
                int unusedCount = 0;

                Dice.displayDice(dice, used, label);

                System.out.println(
                        "Choose dice to meld (A-F), \"K\" to bank, "
                                + "\"R\" to reroll the dice, or \"Q\" to quit:"
                );

                String choice = scanner.nextLine().trim();

                System.out.println("Your choice is: " + choice);

//                 Bank score
                if (choice.equalsIgnoreCase("K")) {

                    players[currentPlayerIndex].addScore(turnScore);
                    totalScore = players[currentPlayerIndex].getTotalScore();

                    System.out.println("Score banked: " + turnScore);
                    System.out.println("Total score: " + totalScore);

                    turnOver = true;
                    continue;
                }

//                 Reroll available dice
                if (choice.equalsIgnoreCase("R")) {

                    unusedCount = Dice.rerollAvailable(dice, used, randomNum);

                    if (unusedCount == 0) {
                        System.out.println("No available dice to reroll");
                        continue;
                    }

                    unusedDice = new int[unusedCount];
                    int position = 0;

                    for (int i = 0; i < dice.length; i++) {
                        if (used[i] != 0) {
                            unusedDice[position] = dice[i];
                            position++;
                        }
                    }

//                     Check Farkle
                    if (FarkleScoring.isFarkle(unusedDice)) {

                        System.out.println();
                        System.out.println("Farkle! Your turn score is 0.");

                        turnScore = 0;

                        System.out.println("Player: " + playerName);
                        System.out.println("Final total score: " + totalScore);

                        turnOver = true;
                    }

                    continue;
                }

//                 Quit game
                if (choice.equalsIgnoreCase("Q")) {

                    System.out.println();
                    System.out.println("Player: " + playerName);
                    System.out.println("Banked score: " + totalScore);
                    System.out.println("Current turn score: " + turnScore);
                    System.out.println("Final total score: " + totalScore);

                    quitGame = true;
                    gameOver = true;

                    continue;
                }

//                 Read selected dice
                String[] choices = choice.split(",");
                int[] selectedDice = new int[6];
                int selectedCount = 0;
                boolean validInput = true;

                for (int i = 0; i < choices.length; i++) {

                    String input = choices[i].trim().toUpperCase();

                    if (input.length() != 1
                            || input.charAt(0) < 'A'
                            || input.charAt(0) > 'F') {

                        validInput = false;
                        break;
                    }

                    int index = input.charAt(0) - 'A';

//                     Check if dice was already selected
                    if (used[index] == 0) {
                        validInput = false;
                        break;
                    }

//                     Check duplicate selections
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
                    System.out.println("Invalid input, please try again.");
                    continue;
                }

//                 Calculate selected dice score
                int newMeldScore =
                        FarkleScoring.calculateMeldScore(selectedDice);

                if (newMeldScore == 0) {
                    System.out.println("This is not a valid meld. Try again.");
                    continue;
                }

//                 Mark selected dice as used
                for (int i = 0; i < choices.length; i++) {

                    int index = choices[i].trim()
                            .toUpperCase().charAt(0) - 'A';

                    used[index] = 0;
                    meldCount++;
                }

                turnScore += newMeldScore;

                System.out.println("New meld score: " + newMeldScore);
                System.out.println("Turn score: " + turnScore);

//                 Hot Hand: all six dice have been used
                if (meldCount == 6) {

                    System.out.println();
                    System.out.println("**************");
                    System.out.println("*  Hot Hand  *");
                    System.out.println("**************");
                    System.out.println();

                    String hotHandChoice = "";

                    while (!hotHandChoice.equals("1")
                            && !hotHandChoice.equals("2")) {

                        System.out.println(
                                "Would you like to roll 6 new dice, "
                                        + "or bank and end your turn?"
                        );

                        System.out.println("1. Roll all 6 dice again");
                        System.out.println("2. Bank your score");

                        hotHandChoice = scanner.nextLine().trim();

                        if (!hotHandChoice.equals("1")
                                && !hotHandChoice.equals("2")) {

                            System.out.println("Invalid choice. Try again.");
                        }
                    }

                    if (hotHandChoice.equals("1")) {

                        dice = Dice.rollSix(randomNum);

                        for (int i = 0; i < dice.length; i++) {
                            used[i] = -1;
                        }

                        meldCount = 0;

                        continue;

                    } else {

                        players[currentPlayerIndex].addScore(turnScore);

                        totalScore =
                                players[currentPlayerIndex].getTotalScore();

                        System.out.println("Final total score: " + totalScore);

                        turnOver = true;
                    }
                }
            }

//             Display all players' scores
            System.out.println();
            System.out.println("Current Total Scores:");

            for (int i = 0; i < playerCount; i++) {

                System.out.println(
                        players[i].getName() + ": "
                                + players[i].getTotalScore()
                );
            }

//             Check if a player reaches the target score
            if (!finalRound
                    && players[currentPlayerIndex].getTotalScore() >= targetScore) {

                if (playerCount == 1) {

                    winnerIndex = currentPlayerIndex;
                    gameOver = true;

                } else {

                    finalRound = true;
                    winnerIndex = currentPlayerIndex;
                    finalTurnsRemaining = playerCount - 1;

                    System.out.println(
                            "Final Round! Each player gets one more turn."
                    );
                }

            } else if (finalRound
                    && currentPlayerIndex != winnerIndex
                    && !quitGame) {

                finalTurnsRemaining--;
            }

//             Check if all final turns are finished
            if (finalRound && finalTurnsRemaining == 0) {

                gameOver = true;

            } else if (!gameOver) {

                currentPlayerIndex =
                        (currentPlayerIndex + 1) % playerCount;
            }

            turnNumber++;
        }

//         Display winner and final scores
        if (!quitGame && winnerIndex != -1) {

            for (int i = 0; i < playerCount; i++) {

                if (players[i].getTotalScore()
                        > players[winnerIndex].getTotalScore()) {

                    winnerIndex = i;
                }
            }

            System.out.println();
            System.out.println("==============================");
            System.out.println("Winner: " + players[winnerIndex].getName());
            System.out.println("Congratulations!!!");
            System.out.println("Final total score:");

            for (int i = 0; i < playerCount; i++) {

                System.out.println(
                        players[i].getName() + ": "
                                + players[i].getTotalScore()
                );
            }

            System.out.println("==============================");

        } else {

            System.out.println("Game ended.");
        }

        scanner.close();
    }
}
