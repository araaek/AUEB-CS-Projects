import java.util.Date;

class Order {
    private String orderCode;
    private ElectronicsStore deviceModel;
    private String customerName;
    private String customerphoneNumber;
    private Date orderDate;
    private Date expectedarrivalDate;
    private double finalCost;
    private String orderStatus;

    public Order(String orderCode, ElectronicsStore deviceModel, String customerName, String customerphoneNumber,
                 Date orderDate, Date expectedarrivalDate, double finalCost, String orderStatus) {
        this.orderCode = orderCode;
        this.deviceModel = deviceModel;
        this.customerName = customerName;
        this.customerphoneNumber = customerphoneNumber;
        this.orderDate = orderDate;
        this.expectedarrivalDate = expectedarrivalDate;
        this.finalCost = finalCost;
        this.orderStatus = orderStatus;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void SetNewStatus(String NewStatus){

        this.orderStatus = NewStatus;

    }

    public ElectronicsStore getDeviceModel() {
        return deviceModel;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhoneNumber() {
        return customerphoneNumber;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public Date getExpectedArrivalDate() {
        return expectedarrivalDate;
    }

    public double getFinalCost() {
        return finalCost;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String toString () {
        return "Order code: " + orderCode + "\n Device: " + deviceModel + "\n Customer Name: " + customerName + "\n Customer Phone Number: " +
            customerphoneNumber + "\n Order date: " + orderDate + "\n Expected Arrival Date: " + expectedarrivalDate + "\n Final cost: " +
                finalCost + "\n Order Status: " + orderStatus;

    }

}