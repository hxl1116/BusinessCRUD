package pojo;

public class BusinessProduct {
    private int productID;
    private String productName;
    private double productPrice;

    public BusinessProduct(int id, String name, double price) {
        productID = id;
        productName = name;
        productPrice = price;
    }

    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Name: %s, Price: %.2f", productID, productName, productPrice);
    }
}
