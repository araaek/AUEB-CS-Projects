// File: shared/dto/RemoveProductRequest.java
package shared.dto;

import java.io.Serializable;
import java.util.Objects;

public class RemoveProductRequest implements Serializable {
    private static final long serialVersionUID = 14L; // Νέο μοναδικό ID

    private String storeName;
    private String productName;

    public RemoveProductRequest(String storeName, String productName) {
        this.storeName = storeName;
        this.productName = productName;
    }

    public String getStoreName() { return storeName; }
    public String getProductName() { return productName; }

    @Override
    public String toString() {
        return "RemoveProductRequest{" +
               "store='" + storeName + '\'' +
               ", product='" + productName + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemoveProductRequest that = (RemoveProductRequest) o;
        return Objects.equals(storeName, that.storeName) &&
               Objects.equals(productName, that.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeName, productName);
    }
}