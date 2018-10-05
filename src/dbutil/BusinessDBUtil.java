package dbutil;

import dao.BusinessDAO;
import pojo.BusinessProduct;

import java.util.ArrayList;

public class BusinessDBUtil {
    private BusinessDAO businessDAO;

    private ArrayList<BusinessProduct> products;

    public BusinessDBUtil(BusinessDAO dao) {
        businessDAO = dao;
        products = new ArrayList<>();

        parseProducts();
    }

    private void parseProducts() {
        ArrayList<String[]> data = businessDAO.selectProducts();
        for (String[] product : data) {
            products.add(new BusinessProduct(Integer.parseInt(product[0]), product[1], Double.parseDouble(product[2])));
        }
    }

    public ArrayList<BusinessProduct> getProducts() {
        return products;
    }

    public void createProduct(int id, String name, double price) {
        BusinessProduct product = new BusinessProduct(id, name, price);

        products.add(product);
        businessDAO.insertProduct(product);
    }

    public void updateProduct(int id, String name, double price) {
        for (BusinessProduct product : products) {
            if (product.getProductID() == id) {
                product.setProductName(name);
                product.setProductPrice(price);
            }
        }

        businessDAO.updateProduct(new BusinessProduct(id, name, price));
    }

    public void deleteProduct(int id) {
        BusinessProduct prodToDelete = null;
        for (BusinessProduct product : products) {
            if (product.getProductID() == id) prodToDelete = product;
        }
        products.remove(prodToDelete);

        businessDAO.deleteProduct(prodToDelete);
    }
}
