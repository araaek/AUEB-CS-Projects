public class WashingMachine extends ElectronicsStore {
    private String energyWashingMachine;
    private int capacityWashingMachine;
    private int revolutionsWashingMachine;

    public WashingMachine(String codeModel, String nameModel, int yearModel, String manufacturerModel, double priceModel,
                          String energyWashingMachine, int capacityWashingMachine, int revolutionsWashingMachine) {
        super(codeModel, nameModel, yearModel, manufacturerModel, priceModel);
        this.energyWashingMachine = energyWashingMachine;
        this.capacityWashingMachine = capacityWashingMachine;
        this.revolutionsWashingMachine = revolutionsWashingMachine;
    }

    // Getters and setters for washing machine-specific characteristics
    public String getEnergyClassWashingMachine() {
        return energyWashingMachine;
    }

    public int getCapacityWashingMachine() {
        return capacityWashingMachine;
    }

    public int getRevolutionsWashingMachine() {
        return revolutionsWashingMachine;
    }

    public String printInfo() {
        return ("Energy: " + energyWashingMachine + "\n Capacity: " + capacityWashingMachine + "\n Revolutions: "
                + revolutionsWashingMachine);
    }

    public String toString() {
        return super.toString();
    }
}

