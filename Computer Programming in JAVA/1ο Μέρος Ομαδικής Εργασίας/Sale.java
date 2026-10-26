import java.util.Date;

class Sale {
    private String salesCode;
    private ElectronicsStore deviceModel;
    private String customerName;
    private String customerphoneNumber;
    private Date saleDate;
    private double finalCost;

    public Sale(String salesCode, ElectronicsStore deviceModel, String customerName, String customerphoneNumber,
                Date saleDate, double finalCost) {
        this.salesCode = salesCode;
        this.deviceModel = deviceModel;
        this.customerName = customerName;
        this.customerphoneNumber = customerphoneNumber;
        this.saleDate = saleDate;
        this.finalCost = finalCost;
    }

    public String getSalesCode() {
        return salesCode;
    }

    public ElectronicsStore getAppliance() {
        return deviceModel;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhoneNumber() {
        return customerphoneNumber;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public double getFinalCost() {
        return finalCost;
    }

    public String toString () {
        return "Sales Code: " + salesCode + "\n Device: " + deviceModel + "\n Customer Name: " + customerName + "\n Customer Phone Number: " +
                customerphoneNumber + "\n Sale date: " + salesCode + "\n Final Cost: " + finalCost;
    }

}
