// File: shared/dto/GetStoreProductSalesRequest.java
package shared.dto;

import java.io.Serializable;
import java.util.Objects;

public class GetStoreProductSalesRequest implements Serializable {
    private static final long serialVersionUID = 15L; // Νέο μοναδικό ID

    private String storeName;

    public GetStoreProductSalesRequest(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreName() { return storeName; }

    @Override
    public String toString() {
        return "GetStoreProductSalesRequest{" +
               "store='" + storeName + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetStoreProductSalesRequest that = (GetStoreProductSalesRequest) o;
        return Objects.equals(storeName, that.storeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeName);
    }
}