// File: shared/dto/UpdateStockRequest.java
package shared.dto;

import java.io.Serializable;

public class UpdateStockRequest implements Serializable {
    private static final long serialVersionUID = 11L; 

    private String storeName;
    private String productName;
    private int newStock;

    public UpdateStockRequest(String storeName, String productName, int newStock) {
        this.storeName = storeName;
        this.productName = productName;
        this.newStock = newStock;
    }

    public String getStoreName() { return storeName; }
    public String getProductName() { return productName; }
    public int getNewStock() { return newStock; }

     @Override
    public String toString() {
        return "UpdateStockRequest{" +
               "store='" + storeName + '\'' +
               ", product='" + productName + '\'' +
               ", newStock=" + newStock +
               '}';
    }
}