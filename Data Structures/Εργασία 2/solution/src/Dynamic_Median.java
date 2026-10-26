import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Dynamic_Median {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Dynamic_Median <input_file_path>");
            return;
        }

        String filePath = args[0];

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0;
            double median;

            PQ RightHeap = new PQ();
            PQ LeftHeap = new PQ();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                int influenzaCases = Integer.parseInt(parts[3]);
                int population = Integer.parseInt(parts[2]);

                City currentCity = new City(Integer.parseInt(parts[0]), parts[1], population, influenzaCases);
                RightHeap.insert(currentCity);
                LeftHeap.insert(RightHeap.getmin());

                if (RightHeap.size() < LeftHeap.size()) {
                    RightHeap.insert(LeftHeap.remove(LeftHeap.max().getID()));
                }

                median = calculateMedian(RightHeap, LeftHeap);
                lineCount++;
                if (lineCount % 5 == 0) {
                    System.out.println("Median: " + median);
                }
            }
            median = calculateMedian(RightHeap, LeftHeap);
            System.out.println("Final Median: " + median);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double minimum(double x, double y) {
        if (x < y)
            return x;
        else
            return y;
    }

    public static double calculateMedian(PQ right, PQ left) {
        if (right.size() > left.size()) {
            return right.min().calculateDensity();
        } else {
            return minimum(right.min().calculateDensity(), left.max().calculateDensity());
        }
    }
}