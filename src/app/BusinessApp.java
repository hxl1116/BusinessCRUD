package app;

import dao.BusinessDAO;
import dbutil.BusinessDBUtil;
import javafx.stage.Stage;
import pojo.BusinessProduct;

import java.util.Scanner;

public class BusinessApp {
    private static final String USAGE = "C: Create a product\n" +
            "R: View products\n" +
            "U: Update a product\n" +
            "D: Delete a product\n" +
            "S: Query products\n" +
            "Q: Exit application\n\n";
    private static final String ADD_PRODUCT = "Add Product Format: ID Name Price\n";
    private static final String UPDATE_PRODUCT = "Update Product Format: ID newName newPrice\n";
    private static final String DELETE_PRODUCT = "Delete Product Format: ID\n";

    private static Scanner key = new Scanner(System.in);

    private static BusinessDBUtil util;

    public static void main(String[] args) {
        if (args.length == 2) {
            Thread businessThread = new Thread(() -> {
                boolean flag = true;
                String input;
                String[] prodData;
                while (flag) {
                    System.out.print("Awaiting Command: ");
                    input = key.nextLine();
                    switch (input) {
                        case "C":
                            System.out.println(ADD_PRODUCT);
                            prodData = key.nextLine().split(" ");
                            util.createProduct(
                                    Integer.parseInt(prodData[0]),
                                    prodData[1],
                                    Double.parseDouble(prodData[2])
                            );
                            break;
                        case "R":
                            for (BusinessProduct product : util.getProducts()) {
                                System.out.println(product.toString());
                            }
                            System.out.println();
                            break;
                        case "U":
                            System.out.println(UPDATE_PRODUCT);
                            prodData = key.nextLine().split(" ");
                            util.updateProduct(
                                    Integer.parseInt(prodData[0]),
                                    prodData[1],
                                    Double.parseDouble(prodData[2])
                            );
                            break;
                        case "D":
                            System.out.println(DELETE_PRODUCT);
                            input = key.nextLine();
                            util.deleteProduct(Integer.parseInt(input));
                            break;
                        case "Q":
                            flag = false;
                            break;
                    }
                }
                System.exit(0);
            });
            if (initialize(args[0], args[1])) businessThread.run();
        } else System.out.println("Usage: db_filename");
    }

    private static boolean initialize(String path, String filename) {
        System.out.println(USAGE);

        BusinessDAO dao = new BusinessDAO(path, filename);
        util = new BusinessDBUtil(dao);

        return true;
    }
}
