
public class FarkleScoring {

    public static boolean isFarkle(int[] dice) {

        int[] count = new int[7];

        for (int i = 0; i < dice.length; i++) {
            count[dice[i]]++;
        }

        // Check if there are three or more of the same number
        for (int i = 0; i <= 6; i++) {
            if (count[i] >= 3) {
                return false;
            }
        }

        // Check three pairs
        int pairCount = 0;

        for (int i = 1; i < 7; i++) {
            if (count[i] == 2) {
                pairCount++;
            }
        }

        if (pairCount == 3) {
            return false;
        }

        // Check straight
        boolean isStraight = true;

        for (int i = 1; i <= 6; i++) {
            if (count[i] != 1) {
                isStraight = false;
            }
        }

        if (isStraight) {
            return false;
        }

        // Check single 1 or 5
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

        int[] meldSizes = new int[7];

        for (int i = 0; i < meld.length; i++) {
            if (meld[i] != 0) {
                meldSizes[meld[i]]++;
                meldDiceCount++;
            }
        }

        // Check Full House
        if (meldDiceCount == 5) {
            boolean hasTriple = false;
            boolean hasPair = false;

            for (int i = 1; i <= 6; i++) {
                if (meldSizes[i] == 3) {
                    hasTriple = true;
                } else if (meldSizes[i] == 2) {
                    hasPair = true;
                }
            }

            if (hasTriple && hasPair) {
                return 1500;
            }
        }

        // Check Straight
        boolean isStraight = true;

        for (int i = 1; i <= 6; i++) {
            if (meldSizes[i] != 1) {
                isStraight = false;
            }
        }

        if (isStraight && meldDiceCount == 6) {
            return 1000;
        }

        // Check Three Pairs
        int pairCount = 0;

        for (int i = 1; i <= 6; i++) {
            if (meldSizes[i] == 2) {
                pairCount++;
            }
        }

        if (pairCount == 3 && meldDiceCount == 6) {
            return 750;
        }

        // Check triples and additional dice
        for (int i = 1; i <= 6; i++) {
            if (meldSizes[i] >= 3) {
                int triplePoints;

                if (i == 1) {
                    triplePoints = 1000;
                } else {
                    triplePoints = i * 100;
                }

                if (meldSizes[i] > 3) {
                    triplePoints +=
                            (meldSizes[i] - 3) * 100 * i;
                }

                meldScore += triplePoints;
            }
        }

        // Add single 1s and 5s
        if (meldSizes[1] < 3) {
            meldScore += meldSizes[1] * 100;
        }

        if (meldSizes[5] < 3) {
            meldScore += meldSizes[5] * 50;
        }

        return meldScore;
    }
}
