package shared.dto;

import java.io.Serializable;
import java.util.Map; // Map<ProductName, Quantity>

public class PurchaseRequest implements Serializable {
    private static final long serialVersionUID = 4L;

    private String storeName;
    private Map<String, Integer> productsToBuy; // Key: ProductName, Value: Quantity

    // Constructor, getters, setters...
    public PurchaseRequest(String storeName, Map<String, Integer> productsToBuy) {
        this.storeName = storeName;
        this.productsToBuy = productsToBuy;
    }

    public String getStoreName() { return storeName; }
    public Map<String, Integer> getProductsToBuy() { return productsToBuy; }

     @Override
    public String toString() {
        return "PurchaseRequest{" +
               "store='" + storeName + '\'' +
               ", items=" + productsToBuy +
               '}';
    }
}