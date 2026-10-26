package shared.dto;

import java.io.Serializable;
import java.util.Objects;

public class Product implements Serializable {
    private static final long serialVersionUID = 2L; // Increment version if needed due to structure change

    private String productName;
    private String productType;
    private int availableAmount;
    private double price;
    private int totalSold = 0;
    private boolean isAvailable = true; // Νέο πεδίο, default true

    public Product(String productName, String productType, int availableAmount, double price) {
        this.productName = productName;
        this.productType = productType;
        this.availableAmount = availableAmount;
        this.price = price;
        this.isAvailable = true; // Ensure constructor sets it
    }

    // Getters
    public String getProductName() { return productName; }
    public String getProductType() { return productType; }
    public int getAvailableAmount() { return availableAmount; }
    public double getPrice() { return price; }
    public int getTotalSold() { return totalSold; }
    public boolean isAvailable() { return isAvailable; } // Νέο Getter

    // Setters
    public void setAvailableAmount(int availableAmount) { this.availableAmount = availableAmount; }
    public void addSoldCount(int quantity) { this.totalSold += quantity; }
    public void setPrice(double price) { this.price = price; }
    public void setAvailable(boolean available) { isAvailable = available; } // Νέο Setter

    @Override
    public String toString() {
        return "Product{" +
               "name='" + productName + '\'' +
               ", type='" + productType + '\'' +
               ", stock=" + availableAmount +
               ", price=" + price +
               ", sold=" + totalSold +
               ", available=" + isAvailable + // Προσθήκη στο toString
               '}';
    }

     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productName.equals(product.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName);
    }
}