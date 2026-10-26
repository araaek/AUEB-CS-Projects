/*
	Name: 
	Student Number: 
*/

public class VotingApp {
    public static void main(String[] args) {
        // array of candidates
        String[] candidates = { "A", "B", "C" };

        // array of results
        int[][] results = {
                { 182, 41, 202 },
                { 145, 85, 325 },
                { 195, 15, 115 },
                { 110, 24, 407 },
                { 255, 11, 357 }
        };

        // array of total votes
        int[] totalVotes = new int[3];
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 5; i++) {
                totalVotes[j] += results[i][j];
            }
        }

        // The sum of all votes
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            sum += totalVotes[i];
        }

        // array of percentages
        double[] percentages = new double[3];
        for (int i = 0; i < 3; i++) {
            percentages[i] = (double) totalVotes[i] / sum * 100;
        }

        // printing the results
        System.out.println("Results:");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(results[i][j] + "\t");
            }
            System.out.println();
        }

        // printing the sums
        System.out.println("\nTotal votes:");
        for (int i = 0; i < 3; i++) {
            System.out.println(candidates[i] + ": " + totalVotes[i] + " votes");
        }

        // printing the percentages
        System.out.println("\nPercentages:");
        for (int i = 0; i < 3; i++) {
            System.out.println(candidates[i] + ": " + String.format("%.2f", percentages[i]) + "%");
        }

        // Check for the winner
        int max;
        int secondMax;
        if (percentages[0] > percentages[1]) {
            max = 0;
            secondMax = 1;
        } else {
            max = 1;
            secondMax = 0;
        }
        if (percentages[2] > percentages[max]) {
            secondMax = max;
            max = 2;
        } else if (percentages[2] > percentages[secondMax]) {
            secondMax = 2;
        }

        if (percentages[max] > 50) {
            System.out.println("\nWinner: " + candidates[max]);
        } else {
            System.out.println("\nRepeat election between the top two candidates:");
            System.out.println(candidates[max] + ": " + String.format("%.2f", percentages[max]) + "%");
            System.out.println(candidates[secondMax] + ": " + String.format("%.2f", percentages[secondMax]) + "%");
        }
    }

}
