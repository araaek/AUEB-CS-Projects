class Refrigerator extends ElectronicsStore {
    private String typeRefrigerator;
    private String energyRefrigerator;
    private int maintenanceRefrigerator;
    private int freezerRefrigerator;

    public Refrigerator(String codeModel, String nameModel, int yearModel, String manufacturerModel, double priceModel,
                        String typeRefrigerator, String energyRefrigerator, int maintenanceRefrigerator, int freezerRefrigerator) {
        super(codeModel, nameModel, yearModel, manufacturerModel, priceModel);
        this.typeRefrigerator = typeRefrigerator;
        this.energyRefrigerator = energyRefrigerator;
        this.maintenanceRefrigerator = maintenanceRefrigerator;
        this.freezerRefrigerator = freezerRefrigerator;
    }

    public String getType() {
        return typeRefrigerator;
    }

    public String getEnergyClassRefrigerator() {
        return energyRefrigerator;
    }

    public int getMaintenanceCapacityRefrigerator() {
        return maintenanceRefrigerator;
    }

    public int getFreezerCapacityRefrigerator() {
        return freezerRefrigerator;
    }

    public String printInfo() {
        return ("Type: " + typeRefrigerator + "\nEnergy: " + energyRefrigerator + "\nMaintenance: "
                + maintenanceRefrigerator + "\nFreezer: " + freezerRefrigerator);
    }

    public String toString() {
        return super.toString();
    }
}