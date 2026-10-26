import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DynamicInfluenza_k_withPQ {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java DynamicInfluenza_k_withPQ <k> <input_file>");
            System.exit(1);
        }

        String inputFile = args[1];
        int k = Integer.parseInt(args[0]);

        // The maximum cityDensityRatio that is already in priorityQueue
        double maxOfMins = -1;

        // The ID of the city with the maximum cityDensityRatio that is already in the
        // priorityQueue
        int IDmaxOfMins = -1;

        // Create the priority queue
        PQ priorityQueue = new PQ(2 * k);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int lineCount = 0;

            // Read the file line by line
            while ((line = reader.readLine()) != null) {
                // Increase the line count
                lineCount++;
                // Parse the line to create a City object
                City city = parseCity(line);

                // Insert the city into the priority queue only if is necessary
                if (lineCount <= k) {
                    if (city.calculateDensity() > maxOfMins) {
                        maxOfMins = city.calculateDensity();
                        IDmaxOfMins = city.getID();
                    }
                    priorityQueue.insert(city);
                } else if (city.calculateDensity() < maxOfMins) {
                    priorityQueue.remove(IDmaxOfMins);
                    priorityQueue.insert(city);
                    maxOfMins = priorityQueue.max().calculateDensity();
                    IDmaxOfMins = priorityQueue.max().getID();
                }

                // Every 5 lines, print the top k cities by influenza density
                if (lineCount % 5 == 0) {
                    printTopCities(priorityQueue, k);
                }
            }

            // Print the top k cities at the end
            System.out.println("Finally:");
            printTopCities(priorityQueue, k);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printTopCities(PQ priorityQueue, int k) {
        System.out.println("Top " + k + " cities with the lowest influenza density:");

        // Create a temporary priority queue for sorting
        PQ tempQueue = new PQ(2 * k);

        // Copy the top k cities from the priorityQueue to the tempQueue
        for (int i = 0; i <= k; i++) {
            City city = priorityQueue.getNode(i); // Assumes 1-based indexing in the priorityQueue
            if (city != null) {
                tempQueue.insert(city);
            }
        }

        // Print the sorted top k cities
        for (int i = 0; i <= k; i++) {
            City city = tempQueue.getmin();
            if (city != null) {
                System.out.println(city.getName());
            }
        }
        System.out.println("--------------");
    }

    private static City parseCity(String line) {
        String[] parts = line.split("\\s+");
        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        int population = Integer.parseInt(parts[2]);
        int influenzaCases = Integer.parseInt(parts[3]);
        return new City(id, name, population, influenzaCases);
    }

}
