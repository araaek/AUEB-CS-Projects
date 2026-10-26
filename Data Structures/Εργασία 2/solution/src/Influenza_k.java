import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Influenza_k {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Influenza_k <k> <file_name>");
            return;
        }

        int k = Integer.parseInt(args[0]);
        String fileName = args[1];

        try {
            City[] cities = readCitiesFromFile(fileName);
            if (k > cities.length) {
                System.out.println("k must not be greater than the total number of cities.");
                return;
            }

            // Sort the cities using Quicksort
            quicksort(cities, 0, cities.length - 1);

            // Print the top k cities
            System.out.println("The top " + k + " cities are:");
            for (int i = 0; i < k; i++) {
                System.out.println(cities[i].getName());
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static City[] readCitiesFromFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        int numCities = 0;

        // Count the number of cities in the file
        while (reader.readLine() != null) {
            numCities++;
        }
        reader.close();

        // Initialize the array of cities
        City[] cities = new City[numCities];

        // Read city data from the file
        reader = new BufferedReader(new FileReader(fileName));
        int index = 0;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split("\\s+"); // Split based on spaces
            int ID = Integer.parseInt(tokens[0]);
            String name = tokens[1];
            int population = Integer.parseInt(tokens[2]);
            int influenzaCases = Integer.parseInt(tokens[3]);

            // Check the data in file
            if (ID < 1 || ID > 999 || population <= 0 || population > 10000000 || influenzaCases > population) {
                System.out.println("Invalid data in the input file.");
                System.exit(1); // Terminate the program
            }

            cities[index] = new City(ID, name, population, influenzaCases);
            index++;
        }
        reader.close();

        return cities;
    }

    private static void quicksort(City[] cities, int low, int high) {
        // Check if there is more than one element in the subarray
        if (high <= low)
            return;

        // Find the pivot element such that elements smaller than the pivot
        // are on the left, and elements greater than the pivot are on the right
        int pivotIndex = partition(cities, low, high);

        // Recursively sort the subarrays on the left and right of the pivot
        quicksort(cities, low, pivotIndex - 1);
        quicksort(cities, pivotIndex + 1, high);

    }

    private static int partition(City[] cities, int low, int high) {
        // Choose the pivot element (last element in the subarray)
        City pivot = cities[high];

        // Index of the smaller element
        int i = low - 1;

        // Traverse through the subarray
        for (int j = low; j < high; j++) {
            // If the current element is smaller than or equal to the pivot
            if (cities[j].compareTo(pivot) <= 0) {
                // Swap cities[i+1] and cities[j]
                i++;
                swap(cities, i, j);
            }
        }

        // Swap cities[i+1] and the pivot
        swap(cities, i + 1, high);

        // Return the index of the pivot element
        return i + 1;
    }

    private static void swap(City[] cities, int i, int j) {
        // Swap the positions of two elements in the array
        City temp = cities[i];
        cities[i] = cities[j];
        cities[j] = temp;
    }
}