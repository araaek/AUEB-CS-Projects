// File: shared/dto/AddProductRequest.java
package shared.dto;

import java.io.Serializable;

public class AddProductRequest implements Serializable {
    private static final long serialVersionUID = 10L; // New unique ID

    private String storeName;
    private Product newProduct; // The Product object to add

    public AddProductRequest(String storeName, Product newProduct) {
        this.storeName = storeName;
        this.newProduct = newProduct;
    }

    public String getStoreName() { return storeName; }
    public Product getNewProduct() { return newProduct; }

    @Override
    public String toString() {
        return "AddProductRequest{" +
               "store='" + storeName + '\'' +
               ", product=" + newProduct +
               '}';
    }
}