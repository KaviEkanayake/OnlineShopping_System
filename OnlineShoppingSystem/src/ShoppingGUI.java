import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class ShoppingGUI extends JFrame {
    private JComboBox<String> productTypeComboBox;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JTextArea productDetailsTextArea;
    private JButton addToCartButton;
    private JButton viewCartButton;

    private List<Product> productList;
    private ShoppingCart shoppingCart;

    public ShoppingGUI() {
        // Initialize data
        productList = new ArrayList<>();
        shoppingCart = new ShoppingCart();

        // Set up the main frame
        setTitle("Westminster Shopping Centre");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize components
        productTypeComboBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothes"});
        tableModel = new DefaultTableModel();
        productTable = new JTable(tableModel);
        productDetailsTextArea = new JTextArea();
        addToCartButton = new JButton("Add to Shopping Cart");
        viewCartButton = new JButton("Shopping Cart");

        // Set up the product table
        tableModel.setColumnIdentifiers(new Object[]{"Product ID", "Name", "Category", "Price($)", "Info"});
        JScrollPane tableScrollPane = new JScrollPane(productTable);

        // Set up the layout
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Product Category:"));
        topPanel.add(productTypeComboBox);
        topPanel.add(viewCartButton);

        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(productDetailsTextArea, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addToCartButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        productTypeComboBox.addActionListener(e -> updateProductTable());

        productTable.getSelectionModel().addListSelectionListener(e -> displayProductDetails());

        addToCartButton.addActionListener(e -> addToCart());

        viewCartButton.addActionListener(e -> viewCart());

        // Load initial data
        initializeTestData();

        // Display the GUI
        setVisible(true);
    }

    private void initializeTestData() {
        productList.add(new Electronics("A1000", "TV", "Electronics", 299.99, "Samsung, 12 weeks warranty",0));
        productList.add(new Electronics("A203", "Dishwasher", "Electronics", 500.00, "Bosh, 36 weeks warranty", 0));
        productList.add(new Clothing("B001", "Shirt", "Clothing", 39.99, "S, White",0));
        productList.add(new Clothing("B201", "Leggings", "Clothing", 22.90, "M, Black", 0));

        updateProductTable();
    }

    private void updateProductTable() {
        tableModel.setRowCount(0); // Clear existing rows

        String selectedProductType = productTypeComboBox.getSelectedItem().toString();

        for (Product product:productList) {
            if (selectedProductType.equals("All") || selectedProductType.equals(getProductType(product))) {
                Object[] rowData = {
                        product.getProductId(),
                        product.getProductName(),
                        product.getCategory(),
                        product.getPrice(),
                        product.getInfo(),


                };
                tableModel.addRow(rowData);
            }
        }
    }

    private String getProductType(Product product) {
        if (product instanceof Electronics) {
            return "Electronics";
        } else if (product instanceof Clothing) {
            return "Clothes";
        } else {
            return "Unknown";
        }
    }

    private void displayProductDetails() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String productId = tableModel.getValueAt(selectedRow, 0).toString();
            for (Product product : productList) {
                if (product.getProductId().equals(productId)) {
                    productDetailsTextArea.setText(product.toString());
                    break;
                }
            }
        }
    }

    private void addToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow != -1) {
            String productId = tableModel.getValueAt(selectedRow, 0).toString();
            for (Product product : productList) {
                if (product.getProductId().equals(productId)) {
                    shoppingCart.addProduct(product);
                    JOptionPane.showMessageDialog(this, "Product added to cart!");
                    break;
                }
            }
        }
    }

    private void viewCart() {
        // Create a new frame for displaying the shopping cart
        JFrame cartFrame = new JFrame("Shopping Cart");
        cartFrame.setSize(400, 300);

        // Set up the components for the shopping cart frame
        DefaultTableModel cartTableModel = new DefaultTableModel();
        JTable cartTable = new JTable(cartTableModel);

        cartTableModel.setColumnIdentifiers(new Object[]{"Product", "Quantity", "Price"});

        // Populate the shopping cart table
        for (Product product : shoppingCart.getProductList()) {
            Object[] rowData = {
                    product.getProductId()+" , "+product.getProductName()+" , "+product.getInfo(),
                    product.getAvailableItems(),
                    product.getPrice()
            };
            cartTableModel.addRow(rowData);
        }

        JScrollPane cartTableScrollPane = new JScrollPane(cartTable);

        // Calculate the final price with discounts
        double totalPrice = shoppingCart.calculateTotalCost();
        double discountedPrice = applyDiscounts(totalPrice);

        // Display the final price and discounts in a label
        JLabel totalPriceLabel = new JLabel("Total Price: $" + formatPrice(totalPrice));
        JLabel discountedPriceLabel = new JLabel("Discounted Price: $" + formatPrice(discountedPrice));

        // Add components to the shopping cart frame
        cartFrame.setLayout(new BorderLayout());
        cartFrame.add(cartTableScrollPane, BorderLayout.CENTER);
        cartFrame.add(totalPriceLabel, BorderLayout.SOUTH);
        cartFrame.add(discountedPriceLabel, BorderLayout.SOUTH);

        // Set the frame visibility to true
        cartFrame.setVisible(true);
    }

    private double applyDiscounts(double totalPrice) {
        int electronicsCount = 0;
        int clothingCount = 0;

        // Count the number of electronics and clothing items in the cart
        for (Product product : shoppingCart.getProductList()) {
            if (product instanceof Electronics) {
                electronicsCount++;
            } else if (product instanceof Clothing) {
                clothingCount++;
            }
        }

        // Apply discounts based on the specified criteria
        double discountedPrice = totalPrice;

        if (electronicsCount >= 3) {
            discountedPrice *= 0.8; // 20% discount for at least three electronics
        }

        if (clothingCount == 0) {
            discountedPrice *= 0.9; // 10% discount for the first purchase without clothing items
        }

        return discountedPrice;
    }

    private String formatPrice(double price) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(price);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ShoppingGUI::new);
    }
    
}
