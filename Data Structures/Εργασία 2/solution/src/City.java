public class City implements CityInterface, Comparable<City> {
    private int ID;
    private String name;
    private int population;
    private int influenzaCases;

    public City() {
        // Default constructor
    }

    public City(int ID, String name, int population, int influenzaCases) {
        this.ID = ID;
        this.name = name;
        this.population = population;
        this.influenzaCases = influenzaCases;
    }

    @Override
    public int getID() {
        return ID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPopulation() {
        return population;
    }

    @Override
    public int getInfluenzaCases() {
        return influenzaCases;
    }

    @Override
    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setPopulation(int population) {
        this.population = population;
    }

    @Override
    public void setInfluenzaCases(int influenzaCases) {
        this.influenzaCases = influenzaCases;
    }

    @Override
    public int compareTo(City other) {
        double densityThis = calculateDensity();
        double densityOther = other.calculateDensity();

        if (densityThis == densityOther) {
            int nameComparison = this.getName().compareTo(other.getName());
            if (nameComparison == 0) {
                return Integer.compare(this.getID(), other.getID());
            }
            return nameComparison;
        }

        return Double.compare(densityThis, densityOther);
    }

    public double calculateDensity() {
        return Math.round((double) getInfluenzaCases() / getPopulation() * 50000 * 100.0) / 100.0;
    }
}
