package dao;

import pojo.BusinessProduct;

import java.sql.*;
import java.util.ArrayList;

public class BusinessDAO {
    private static final String SELECT_PRODUCTS = "SELECT * FROM Product";
    private static final String INSERT_PRODUCT = "INSERT INTO Product VALUES(?, ?, ?)";
    private static final String UPDATE_PRODUCT = "UPDATE Product SET ProductName = ?, ProductPrice = ? WHERE ProductID = ?";
    private static final String DELETE_PRODUCT = "DELETE FROM Product WHERE ProductID = ?";

    private String databasePath;
    private String databaseFilename;

    public BusinessDAO(String path, String name) {
        databasePath = path;
        databaseFilename = name;
    }

    private Connection connect() {
        String url = String.format("jdbc:sqlite:%s", databasePath + databaseFilename);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public ArrayList<String[]> selectProducts() {
        ArrayList<String[]> data = new ArrayList<>();
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rstSt = stmt.executeQuery(SELECT_PRODUCTS)) {
            while (rstSt.next()) {
                data.add(new String[]{String.valueOf(
                        rstSt.getInt("ID")),
                        rstSt.getString("Name"),
                        String.valueOf(rstSt.getFloat("Price"))
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public void insertProduct(BusinessProduct product) {
        try (Connection conn = connect();
             PreparedStatement prdStmt = conn.prepareStatement(INSERT_PRODUCT)) {
            prdStmt.setInt(1, product.getProductID());
            prdStmt.setString(2, product.getProductName());
            prdStmt.setFloat(3, (float) product.getProductPrice());
            prdStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(BusinessProduct product) {
        try (Connection conn = connect();
             PreparedStatement prdStmt = conn.prepareStatement(UPDATE_PRODUCT)) {
            prdStmt.setString(1, product.getProductName());
            prdStmt.setFloat(2, (float) product.getProductPrice());
            prdStmt.setInt(3, product.getProductID());
            prdStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(BusinessProduct product) {
        try (Connection conn = connect();
             PreparedStatement prdStmt = conn.prepareStatement(DELETE_PRODUCT)) {
            prdStmt.setInt(1, product.getProductID());
            prdStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
