

abstract class ElectronicsStore {
    protected String codeModel;
    protected String nameModel;
    protected int yearModel;
    protected String manufacturerModel;
    protected double priceModel;

    public ElectronicsStore (String codeModel, String nameModel, int yearModel, String manufacturerModel, double priceModel) {
        this.codeModel = codeModel;
        this.nameModel = nameModel;
        this.yearModel = yearModel;
        this.manufacturerModel = manufacturerModel;
        this.priceModel = priceModel;
    }

    public String getCodeModel () {
        return codeModel;
    }

    public String getNameModel () {
        return nameModel;
    }

    public int getYearModel () {
        return yearModel;
    }

    public String getManufacturerModel () {
        return manufacturerModel;
    }

    public double getPriceModel () {
        return priceModel;
    }

    public String toString () {
        return "Model's code: " + codeModel + "\n Model's name: " + nameModel + "\n Model's year: " +
                yearModel + "\n Model's manufacturer: " + manufacturerModel + "\n Model's price: " + priceModel;
    }
}
