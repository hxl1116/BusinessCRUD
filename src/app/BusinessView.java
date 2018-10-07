package app;

import dao.BusinessDAO;
import dbutil.BusinessDBUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import pojo.BusinessProduct;

import java.util.ArrayList;

public class BusinessView extends Application {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;
    private static final double COL_WIDTH = WIDTH / 3.0;
    private static final double EDIT_FIELD_WIDTH = 150;
    private static final double EDIT_FIELD_HEIGHT = 25;

    private BusinessDBUtil util;
    private Scene primaryScene;

    private TabPane mainContentDisplay;
    private TableView<BusinessProduct> productTable;
    private ObservableList<BusinessProduct> observableProducts;
    private BusinessProduct selectedProduct;

    @Override
    public void start(Stage primaryStage) {
        init();
        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("Business Database Viewer");
        primaryStage.show();
    }

    @Override
    public void init() {
        BusinessDAO dao = new BusinessDAO(getParameters().getRaw().get(0), getParameters().getRaw().get(1));
        util = new BusinessDBUtil(dao);

        observableProducts = FXCollections.observableArrayList();
        observableProducts.addAll(util.getProducts());
        productTable = productContentDisplay();
        productTable.setItems(observableProducts);
        mainContentDisplay = mainContentDisplay();
        primaryScene = new Scene(mainContentDisplay);
    }

    private TabPane mainContentDisplay() {
        TabPane root = new TabPane();
        root.setPrefSize(WIDTH, HEIGHT);

        Tab productTab = new Tab("Products");
        productTab.setClosable(false);

        Tab addTab = new Tab("Add Product");
        addTab.setClosable(true);

        Tab editTab = new Tab("Edit Product");
        editTab.setClosable(true);

        ContextMenu productTabContextMenu = productContentDisplayContextMenu();
        productTable.setContextMenu(productTabContextMenu);

        productTab.setContent(productTable);

        VBox productAddDisplay = addProdContentDisplay();
        addTab.setContent(productAddDisplay);

        VBox productUpdateDisplay = editProdContentDisplay();
        editTab.setContent(productUpdateDisplay);

        root.getTabs().addAll(productTab);

        productTabContextMenu.getItems().get(0).setOnAction(event -> mainContentDisplay.getTabs().add(addTab));

        productTabContextMenu.getItems().get(1).setOnAction(event -> {
            selectedProduct = productTable
                    .getSelectionModel()
                    .getSelectedItem();
            mainContentDisplay.getTabs().add(editTab);
        });

        productTabContextMenu.getItems().get(2).setOnAction(event -> {
            BusinessProduct product = productTable
                    .getSelectionModel()
                    .getSelectedItem();
            util.deleteProduct(product.getProductID());

            observableProducts.setAll(util.getProducts());
            productTable.refresh();

        });

        return root;
    }

    @SuppressWarnings("unchecked")
    private TableView<BusinessProduct> productContentDisplay() {
        TableView<BusinessProduct> productTableView = new TableView<>();
        productTableView.setPrefSize(WIDTH, HEIGHT);

        TableColumn<BusinessProduct, Integer> productIdCol = new TableColumn<>("ID");
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productIdCol.setMinWidth(COL_WIDTH);

        TableColumn<BusinessProduct, String> productNameCol = new TableColumn<>("Name");
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productNameCol.setMinWidth(COL_WIDTH);

        TableColumn<BusinessProduct, Double> productPriceCol = new TableColumn<>("Price");
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        productPriceCol.setMinWidth(COL_WIDTH);

        productTableView.getColumns().addAll(productIdCol, productNameCol, productPriceCol);

        return productTableView;
    }

    private ContextMenu productContentDisplayContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem addItem = new MenuItem("Add Product");
        MenuItem editItem = new MenuItem("Edit Product");
        MenuItem deleteItem = new MenuItem("Delete Product");

        contextMenu.getItems().addAll(addItem, editItem, deleteItem);

        return contextMenu;
    }

    private VBox addProdContentDisplay() {
        VBox vBox = new VBox(10);
        vBox.setPrefSize(WIDTH, HEIGHT);
        vBox.setAlignment(Pos.CENTER);

        TextField idTextField = new TextField();
        idTextField.setText(String.valueOf(util.getProducts().size() + 1));
        idTextField.setMaxSize(EDIT_FIELD_WIDTH, EDIT_FIELD_HEIGHT);

        TextField nameTextField = new TextField();
        nameTextField.setPromptText("Enter name");
        nameTextField.setMaxSize(EDIT_FIELD_WIDTH, EDIT_FIELD_HEIGHT);

        TextField priceTextField = new TextField();
        priceTextField.setPromptText("Enter price");
        priceTextField.setMaxSize(EDIT_FIELD_WIDTH, EDIT_FIELD_HEIGHT);

        Button createProductButton = new Button("Add Product");
        createProductButton.setMaxSize(EDIT_FIELD_WIDTH, EDIT_FIELD_HEIGHT);

        vBox.getChildren().addAll(idTextField, nameTextField, priceTextField, createProductButton);

        createProductButton.setOnAction(event -> {
            if (!idTextField.getText().isEmpty() &&
                    !nameTextField.getText().isEmpty() &&
                    !priceTextField.getText().isEmpty()) {
                util.createProduct(
                        Integer.parseInt(idTextField.getText()),
                        nameTextField.getText(),
                        Double.parseDouble(priceTextField.getText())
                );

                observableProducts.setAll(util.getProducts());
                productTable.refresh();
            }

            idTextField.setText(String.valueOf(util.getProducts().size() + 1));
            nameTextField.setText(null);
            priceTextField.setText(null);
        });

        return vBox;
    }

    private VBox editProdContentDisplay() {
        VBox vBox = new VBox(10);
        vBox.setPrefSize(WIDTH, HEIGHT);
        vBox.setAlignment(Pos.CENTER);

        TextField nameTextField = new TextField();
        nameTextField.setPromptText("Enter name");
        nameTextField.setMaxSize(EDIT_FIELD_WIDTH, EDIT_FIELD_HEIGHT);

        TextField priceTextField = new TextField();
        priceTextField.setPromptText("Enter price");
        priceTextField.setMaxSize(EDIT_FIELD_WIDTH, EDIT_FIELD_HEIGHT);

        Button updateProductButton = new Button("Edit Product");
        updateProductButton.setMaxSize(EDIT_FIELD_WIDTH, EDIT_FIELD_HEIGHT);

        vBox.getChildren().addAll(nameTextField, priceTextField, updateProductButton);

        updateProductButton.setOnAction(event -> {
            if (!nameTextField.getText().isEmpty() && !priceTextField.getText().isEmpty()) {
                util.updateProduct(
                        selectedProduct.getProductID(),
                        nameTextField.getText(),
                        Double.parseDouble(priceTextField.getText())
                );

                observableProducts.setAll(util.getProducts());
                productTable.refresh();
            }

            nameTextField.setText(null);
            priceTextField.setText(null);
        });

        return vBox;
    }
}
