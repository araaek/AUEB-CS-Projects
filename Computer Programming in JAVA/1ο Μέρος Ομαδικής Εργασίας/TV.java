class Television extends ElectronicsStore {
    private String typeTV;
    private String dimensionTV;
    private String resolutionTV;
    private String portsTV;

    public Television(String codeModel, String nameModel, int yearModel, String manufacturerModel, double priceModel,
                      String typeTV, String dimensionTV, String resolutionModel, String portsModel) {
        super(codeModel, nameModel, yearModel, manufacturerModel, priceModel);
        this.typeTV = typeTV;
        this.dimensionTV = dimensionTV;
        this.resolutionTV = resolutionTV;
        this.portsTV = portsTV;
    }

    public String getTypeTV () {
        return typeTV;
    }

    public String getDimensionTV () {
        return dimensionTV;
    }

    public String getResolutionTV () {
        return resolutionTV;
    }

    public String getPortsTV () {
        return portsTV;
    }

    public String printInfo() {
        return ("Type: " + typeTV + "\n Screen Dimension: " + dimensionTV + "\n Resolution: "
                + resolutionTV + "\n Ports: " + portsTV);

    }
    public String toString() {
        return super.toString();
    }

}