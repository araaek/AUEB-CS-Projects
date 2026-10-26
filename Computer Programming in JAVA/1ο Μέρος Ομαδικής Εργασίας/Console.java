class Console extends ElectronicsStore {
    private String typeConsole;
    private String processorConsole;
    private String graphicsConsole;
    private String soundConsole;
    private int harddrivecapacityConsole;


    public Console (String codeModel, String nameModel, int yearModel, String manufacturerModel, double priceModel,
                   String typeConsole, String processorConsole, String graphicsConsole, String soundConsole, int harddrivecapacityConsole) {
        super(codeModel, nameModel, yearModel, manufacturerModel, priceModel);
        this.typeConsole = typeConsole;
        this.processorConsole = processorConsole;
        this.graphicsConsole = graphicsConsole;
        this.soundConsole = soundConsole;
        this.harddrivecapacityConsole = harddrivecapacityConsole;
    }

    public String getTypeConsole() {
        return typeConsole;
    }

    public String getProcessorConsole() {
        return processorConsole;
    }

    public String getGraphicsConsole() {
        return graphicsConsole;
    }

    public String getSoundConsole() {
        return soundConsole;
    }

    public int getHardDriveCapacityConsole() {
        return harddrivecapacityConsole;
    }

    public String printInfo() {
        return ("Type: " + typeConsole + "\n Processor: " + processorConsole + "\n Graphics: "
                + graphicsConsole + "\n Sound: " + soundConsole + "\n Hard Drive: " + harddrivecapacityConsole);
    }
    public String toString() {
        return super.toString();
    }
}

