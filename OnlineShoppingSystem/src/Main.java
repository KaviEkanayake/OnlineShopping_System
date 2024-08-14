import java.io.*;
import java.util.*;

interface ShoppingManager {
    void addProduct(Product product);

    boolean deleteProduct(String productId);

    void printProductList();

    void saveProductsToFile();

    void loadProductsFromFile();
}

abstract class Product implements Serializable {
    private String productId;
    private String productName;
    private int availableItems;
    private double price;
    private String category;
    private String info;


    public Product(String productId, String productName, String category, double price, String info, int availableItems) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.info = info;
        this.availableItems = availableItems;
    }

    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId){
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(int availableItems) {
        this.availableItems = availableItems;
    }
    public String getCategory(){
        return category;
    }
    public void setCategory(String category){
        this.category= category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductType() {
        return null;
    }
    public String getInfo(){
        return info;
    }
    public void setInfo(String info){
        this.info = info;
    }

}

class Electronics extends Product {
    private String brand;
    private int warrantyPeriod;
    private String info = "brand" + warrantyPeriod;


    public Electronics(String productId, String productName, String category, double price,String info,int availableItems) {
        super(productId, productName, category, price, info, availableItems);
        this.info = info;

    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    @Override
    public String getProductType() {
        return "Electronics";
    }
}

class Clothing extends Product {
    private String size;
    private String color;
    private String info = "size"+"color";

    public Clothing(String productId, String productName, String category, double price, String info,int availableItems) {
        super(productId, productName, category, price, info, availableItems);
        this.info = info;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String getProductType() {
        return "Clothing";
    }
}

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class ShoppingCart {
    private List<Product> productList;

    public ShoppingCart() {
        this.productList = new ArrayList<>();
    }

    public void addProduct(Product product) {
        productList.add(product);
    }

    public void removeProduct(Product product) {
        productList.remove(product);
    }

    public double calculateTotalCost() {
        double totalCost = 0.0;
        for (Product product : productList) {
            totalCost += product.getPrice();
        }
        return totalCost;
    }


    public List<Product> getProductList() {
        return productList;
    }
}

class WestminsterShoppingManager implements ShoppingManager {
    private List<Product> productList;

    public WestminsterShoppingManager() {
        this.productList = new ArrayList<>();
    }

    @Override
    public void addProduct(Product product) {
        if (productList.size() < 50) {
            productList.add(product);
            System.out.println("Product added successfully.");
        } else {
            System.out.println("Cannot add more products. Maximum limit reached.");
        }
    }

    @Override
    public boolean deleteProduct(String productId) {
        Iterator<Product> iterator = productList.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getProductId().equals(productId)) {
                iterator.remove();
                System.out.println("Product deleted successfully.");
                return true;
            }
        }
        System.out.println("Product with ID " + productId + " not found.");
        return false;
    }

    @Override
    public void printProductList() {
        productList.sort(Comparator.comparing(Product::getProductId));
        for (Product product : productList) {
            System.out.println("Product ID: " + product.getProductId());
            System.out.println("Product Name: " + product.getProductName());
            System.out.println("Available Items: " + product.getAvailableItems());
            System.out.println("Price: " + product.getPrice());
            System.out.println("Product Type: " + product.getProductType());
            System.out.println("------------------------------");
        }
    }

    @Override
    public void saveProductsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("productList.ser"))) {
            oos.writeObject(productList);
            System.out.println("Product list saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadProductsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("productList.ser"))) {
            productList = (List<Product>) ois.readObject();
            System.out.println("Product list loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

// Main.java
public class Main {
    public static void main(String[] args) {
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        ShoppingCart shoppingCart = new ShoppingCart();
        Scanner scanner = new Scanner(System.in);

        int choice;

        do {
            System.out.println("1. Add Product");
            System.out.println("2. Delete Product");
            System.out.println("3. Print Product List");
            System.out.println("4. Save Products to File");
            System.out.println("5. Load Products from File");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    addProduct(shoppingManager, scanner);
                    break;
                case 2:
                    deleteProduct(shoppingManager, scanner);
                    break;
                case 3:
                    shoppingManager.printProductList();
                    break;
                case 4:
                    shoppingManager.saveProductsToFile();
                    break;
                case 5:
                    shoppingManager.loadProductsFromFile();
                    break;
                case 6:
                    System.out.println("Exiting the application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }

        } while (choice != 6);

        scanner.close();
    }

    private static void addProduct(WestminsterShoppingManager shoppingManager, Scanner scanner) {
        System.out.println("Select product type:");
        System.out.println("1. Electronics");
        System.out.println("2. Clothing");
        System.out.print("Enter your choice: ");

        int productTypeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        System.out.print("Enter product ID: ");
        String productId = scanner.nextLine();

        System.out.print("Enter product name: ");
        String productName = scanner.nextLine();

        System.out.print("Enter available items: ");
        int availableItems = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume the newline

        if (productTypeChoice == 1) {
            System.out.print("Enter brand: ");
            String brand = scanner.nextLine();

            System.out.print("Enter warranty period: ");
            int warrantyPeriod = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            String info = brand+warrantyPeriod;

            shoppingManager.addProduct(new Electronics(productId, productName,"Electronics", price, info,availableItems));
        } else if (productTypeChoice == 2) {
            System.out.print("Enter size: ");
            String size = scanner.nextLine();

            System.out.print("Enter color: ");
            String color = scanner.nextLine();

            String info = size+color;

            shoppingManager.addProduct(new Clothing(productId, productName,"Clothing", price, info,availableItems));
        } else {
            System.out.println("Invalid product type choice.");
        }
    }

    private static void deleteProduct(WestminsterShoppingManager shoppingManager, Scanner scanner) {
        System.out.print("Enter product ID to delete: ");
        String productIdToDelete = scanner.nextLine();

        shoppingManager.deleteProduct(productIdToDelete);
    }
}
