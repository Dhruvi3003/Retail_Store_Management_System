//RetailStoreManagement.java
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class RetailStoreManagement {

    // Scanner for user input
    static Scanner scanner = new Scanner(System.in);

    // Database connection settings
    private static final String DB_URL = "jdbc:mysql://localhost:3306/retail_store";
    private static final String USER = "root";
    private static final String PASS = "";

    // Default admin credentials
    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "password";
    
    // Create BST instance for product management
    public static BST bst = new BST();

    // Create HashMap to store product sales data
    private static HashMap<String, Integer> productSales = new HashMap<>();

    // Create Hashtable to store customer purchase history
    private static Hashtable<Integer, String> customerPurchases = new Hashtable<>();

    public static void main(String[] args) throws Exception {
        
        try {
            // Authenticate user
            if (!authenticateUser()) {
                System.out.println("\nAuthentication failed. \nWrong Username or password. \nExiting system.");
                return;
            }

            // Establish database connection
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Create tables if not exist
            createTables(conn);

            // Load products into BST
            bst.loadProductsIntoBST(conn);

            // Load sales analysis
            loadSalesAnalysis(conn);

            // Main menu loop
            while (true) {

                // Display menu options
                System.out.println("\nWelcome To RetailStore Management System");
                System.out.println("\nSelect choices\n");
                
                System.out.println("1. Manage Products");
                System.out.println("2. Display Statistics");
                System.out.println("3. Manage Customers");
                System.out.println("4. Manage Orders");
                System.out.println("5. Manage Payments");
                System.out.println("6. Generate Bill");
                System.out.println("7. Manage Staff");
                System.out.println("8. To Display Product Sales");
                System.out.println("9. To Display Customer Purchase");
                System.out.println("10. Exit\n");
                System.out.println();
            
                // Get user choice
                int choice = validateIntInput();
                switch (choice) {
                    // Handle user choice
                    case 1 -> manageProducts(conn);
                    case 2 -> displayStatistics(conn);
                    case 3 -> manageCustomers(conn);
                    case 4 -> manageOrders(conn);
                    case 5 -> managePayments(conn);
                    case 6 -> generateBill(conn);
                    case 7 -> manageStaff(conn);
                    case 8 -> displayProductSales();
                    case 9 -> displayCustomerPurchases();
                    case 10 -> System.exit(0);
                }
            }
            
                           
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // User authentication method
    private static boolean authenticateUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        return username.equals(DEFAULT_ADMIN_USERNAME) && password.equals(DEFAULT_ADMIN_PASSWORD);
    }

    // To manage products
    private static void manageProducts(Connection conn) throws  Exception{
        boolean is = false;
        while(!is){
            System.out.println("1. Add Product");
            System.out.println("2. To View Product");
            System.out.println("3. To delete Product");
            System.out.println("4. View All Products");
            System.out.println("5. Update Product");
            System.out.println("6. Return to main menu\n");

            int choice = validateIntInput();

            switch (choice) {
                case 1 -> addProduct(conn);
                case 2 -> bst.viewProduct();
                case 3 -> bst.deleteProduct(conn);
                case 4 -> viewAllProducts(conn);
                case 5 -> updateProduct(conn);
                case 6 -> is = true;
                default -> System.out.println("Invalid choice");
            }

        }
    }

    // To manage Customers
    private static void manageCustomers(Connection conn) throws  Exception{
        boolean is = false;
        while(!is){
            System.out.println("1. Add Customer");
            System.out.println("2. View Customer");
            System.out.println("3. View All Customers");
            System.out.println("4. Return to main menu\n");

            int choice = validateIntInput();

            switch (choice) {
                    case 1 -> addCustomer(conn);
                    case 2 -> viewCustomer(conn);
                    case 3 -> viewAllCustomers(conn);
                    case 4 -> is = true;
                    default -> System.out.println("Invalid choice");
            }

        }
    }

    // To manage Orders
    private static void manageOrders(Connection conn) throws  Exception {
        boolean is = false;
        while(!is){
            System.out.println("1. Place Order");
            System.out.println("2. View Order");
            System.out.println("3. Update Order");
            System.out.println("4. View All Orders");
            System.out.println("5. Return to main menu\n");

            int choice = validateIntInput();

            switch (choice) {
                    case 1 -> placeOrder(conn);
                    case 2 -> viewOrder(conn);
                    case 3 -> updateOrder(conn);
                    case 4 -> viewAllOrders(conn);
                    case 5 -> is = true;
                    default -> System.out.println("Invalid choice");
            }

        }
    }

    // To manage payments
    private static void managePayments(Connection conn) throws  Exception {
        
        boolean is = false;
        while(!is){
            System.out.println("1. Add Payment");
            System.out.println("2. View Payment");
            System.out.println("3. View All Payments");
            System.out.println("4. Return to main menu\n");

            int choice = validateIntInput();

            switch (choice) {
                    case 1 -> addPayment(conn);
                    case 2 -> viewPayment(conn);
                    case 3 -> viewAllPayments(conn);
                    case 4 -> is = true;
                    default -> System.out.println("Invalid choice");
            }

        }
    }
    
    // To manage staff
    private static void manageStaff(Connection conn) throws  Exception {
        
        boolean is = false;
        while(!is){
            System.out.println("1. Add Staff");
            System.out.println("2. View Staff");
            System.out.println("3. Update Staff");
            System.out.println("4. View All Staff");
            System.out.println("5. Return to main menu\n");

            int choice = validateIntInput();

            switch (choice) {
                    case 1 -> addStaff(conn);
                    case 2 -> viewStaff(conn);
                    case 3 -> updateStaff(conn);
                    case 4 -> viewAllStaff(conn);
                    case 5 -> is = true;
                    default -> System.out.println("Invalid choice");
            }

        }
    }

    // Queries/Schema of Tables
    public static void createTables(Connection connection) throws Exception{
        Statement statement = connection.createStatement();
        String query = "CREATE TABLE IF NOT EXISTS Products (" + 
                        "    ProductID INT PRIMARY KEY," + 
                        "    Name VARCHAR(100)," + 
                        "    Price DECIMAL(10, 2)," + 
                        "    QuantityInStock INT," + 
                        "    Status VARCHAR(20) DEFAULT 'Available'," +
                        "    dateFrom DATE DEFAULT CURRENT_TIMESTAMP," +
                        "    dateTo DATE DEFAULT NULL" +
                      ")";
        statement.execute(query);

        query = "CREATE TABLE IF NOT EXISTS Customers (" + 
                        "    CustomerID INT PRIMARY KEY," + 
                        "    Name VARCHAR(100)," + 
                        "    Email VARCHAR(100)," +
                        "    Phone VARCHAR(15))";
        statement.execute(query);

        query = "CREATE TABLE IF NOT EXISTS Staff (" +
                        " staffID INT PRIMARY KEY," +
                        " name VARCHAR(100)," +
                        " email VARCHAR(100)," +
                        " phone VARCHAR(15)," +
                        " position VARCHAR(100))";
        statement.execute(query);

        query = "CREATE TABLE IF NOT EXISTS Orders (" + 
                        " OrderID INT PRIMARY KEY AUTO_INCREMENT, " + 
                        " CustomerID INT, " + 
                        " StaffID INT, " + 
                        " OrderDate DATE, " + 
                        " FOREIGN KEY (StaffID) REFERENCES Staff(StaffID), " + 
                        " FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID))";
        statement.execute(query);

        query = "CREATE TABLE IF NOT EXISTS Payments (" +
                        " PaymentID INT PRIMARY KEY AUTO_INCREMENT, " +
                        " OrderID INT, " +
                        " PaymentMethod VARCHAR(100), " +
                        " PaymentDate DATE, " +
                        " Amount DECIMAL(10, 2), " +
                        " FOREIGN KEY (OrderID) REFERENCES Orders(OrderID))";
        statement.execute(query);

        query = "CREATE TABLE IF NOT EXISTS OrderDetails (" + 
                        " OrderID INT, " + 
                        " ProductID INT, " + 
                        " Quantity INT, " + 
                        " PRIMARY KEY (OrderID, ProductID), " + 
                        " FOREIGN KEY (OrderID) REFERENCES Orders(OrderID), " + 
                        " FOREIGN KEY (ProductID) REFERENCES Products(ProductID))";
        statement.execute(query);


    }

    // Add product
    private static void addProduct(Connection conn) throws SQLException {
    try {
        System.out.print("\nEnter product ID:   ");
        int productID = validateIntInput();

        System.out.print("Enter product name:   ");
        String name = scanner.nextLine();

        System.out.print("Enter product price:  ");
        double price = validateDoubleInput();

        System.out.print("Enter product quantity:   ");
        int quantity = validateIntInput();

        System.out.println();

        PreparedStatement pst = conn.prepareStatement("INSERT INTO products(ProductID,Name,Price,QuantityInStock) VALUES (?, ?, ?, ?)");
        pst.setInt(1, productID);
        pst.setString(2, name);
        pst.setDouble(3, price);
        pst.setInt(4, quantity);
        pst.executeUpdate();
        System.out.println("Product added successfully.\n");
        bst.loadProductsIntoBST(conn);
        
    } catch (SQLException e) {
        if (e.getSQLState().equals("23000")) {
            System.out.println("\nProduct with this ID already exists.\n");
        } else {
            System.out.println("\nError adding product: " + e.getMessage());
        }
    }
}


// View all products
private static void viewAllProducts(Connection conn) throws SQLException {
    PreparedStatement pst = conn.prepareStatement("SELECT * FROM products");
    ResultSet resultSet = pst.executeQuery();
    while (resultSet.next()) {
        System.out.println("\n-------------");
        System.out.println("Product ID: " + resultSet.getInt(1));
        System.out.println("Name: " + resultSet.getString(2));
        System.out.println("Price: " + resultSet.getDouble(3));
        System.out.println("Quantity in Stock: " + resultSet.getInt(4));
        System.out.println("Status is : " + resultSet.getString(5));
        System.out.println("-------------");
    }
}

    
    //Update Product
    public static void updateProduct(Connection conn) throws Exception {
        System.out.print("Enter id to update ");
        int id = validateIntInput();
        //scanner.nextLine();
        Product product = bst.searchRec(BST.root, id);
        if (product != null) {
            System.out.println("Enter new name (press enter to skip)");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                product.setName(name);
            }
            System.out.println("Enter new price (press enter to skip)");
            String priceStr = scanner.nextLine();
            if (!priceStr.isEmpty()) {
                try {
                    product.setPrice(Double.parseDouble(priceStr));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price. Please enter a valid number.");
                    return;
                }
            }
            System.out.println("Enter new quantity (press enter to skip)");
        String quantityStr = scanner.nextLine();
        if (!quantityStr.isEmpty()) {
            try {
                product.setQuantityInStock(Integer.parseInt(quantityStr));
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Please enter a valid number.");
                return;
            }
        }
        System.out.println("Update Status");
        String statusStr = scanner.nextLine();
        if (!statusStr.isEmpty()) {
            product.setStatus(statusStr);
        }
        PreparedStatement pst = conn.prepareStatement("UPDATE products SET Name = ?, Price = ?, QuantityInStock = ?, Status = ? WHERE ProductID = ?");
        pst.setString(1, product.getName());
        pst.setDouble(2, product.getPrice());
        pst.setInt(3, product.getQuantityInStock());
        pst.setString(4, product.getStatus());
        pst.setInt(5, product.getProductID());
        if (pst.executeUpdate() > 0) {
            System.out.println("Updated");
            bst.loadProductsIntoBST(conn);
        } else {
            System.out.println("Failed");
        }
    } else {
        System.out.println("Product not found.");
    }
}


// Display Product Statistics
public static void displayStatistics(Connection conn) throws Exception {
    System.out.println("Total Products: " + bst.getTotalProducts());
    System.out.println("Discontinued products: " + bst.countDiscontinuedProducts());
    System.out.println("Total Stock Value: " + bst.getTotalStockValue());
    System.out.printf("Average Product Price: %.2f" , bst.getAverageProductPrice());
    System.err.println();
}

  // Add customer
  private static void addCustomer(Connection conn) throws SQLException {
    try {
        System.out.print("Enter customer ID:    ");
        int customerID = validateIntInput();
        //scanner.nextLine(); // consume newline

        System.out.print("Enter customer name:  ");
        String name = scanner.nextLine();

        System.out.print("Enter customer email: ");
        String email = scanner.nextLine();

        System.out.print("Enter customer phone: ");
        String phone = scanner.nextLine();
        
        PreparedStatement pst = conn.prepareStatement("INSERT INTO customers VALUES (?, ?, ?, ?)");
        pst.setInt(1, customerID);
        pst.setString(2, name);
        pst.setString(3, email);
        pst.setString(4, phone);
        pst.executeUpdate();
        System.out.println("\nCustomer added successfully.\n");
    } catch (Exception e) {
        System.out.println("Error : " + e.getMessage());
    }
}

// Method to validate email format
    static String validateEmail(){
        while(true){
            String email = scanner.nextLine();
            if(email.endsWith("@gmail.com")){
                return email;
            }
            else{
                System.out.println("Re - Enter E-mail in specified format");
            }
        }
    }

    // View customer
    private static void viewCustomer(Connection conn) throws SQLException {
        System.out.print("Enter customer ID:    ");
        int customerID = validateIntInput();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM customers WHERE CustomerID = ?");
        pst.setInt(1, customerID);
        ResultSet resultSet = pst.executeQuery();
        if (resultSet.next()) {
            System.out.println("\n-----------");
            System.out.println("Customer ID: " + resultSet.getInt(1));
            System.out.println("Name: " + resultSet.getString(2));
            System.out.println("Email: " + resultSet.getString(3));
            System.out.println("Phone: " + resultSet.getString(4));
            System.out.println("-----------");
        } else {
            System.out.println("\nCustomer not found.");
        }
    }

    // View all customers
    private static void viewAllCustomers(Connection conn) throws SQLException {
    PreparedStatement pst = conn.prepareStatement("SELECT * FROM customers");
    ResultSet resultSet = pst.executeQuery();
    while (resultSet.next()) {
        System.out.println("\n-----------");
        System.out.println("Customer ID: " + resultSet.getInt(1));
        System.out.println("Name: " + resultSet.getString(2));
        System.out.println("Email: " + resultSet.getString(3));
        System.out.println("Phone: " + resultSet.getString(4));
        System.out.println("-------------");
    }
}

// Place order
private static void placeOrder(Connection conn) throws SQLException {
    try {
        
        boolean isContinue = true;
        boolean isPlaced = false;
        int availableQuantity = 0;
        
        System.out.print("Enter customer ID:    ");
        int customerID = validateIntInput();

        System.out.print("Enter Staff ID:    ");
        int staffID = validateIntInput();

        PreparedStatement pst = conn.prepareStatement("INSERT INTO orders(CustomerId,StaffID,OrderDate) VALUES (?,?, ?)", Statement.RETURN_GENERATED_KEYS);

        pst.setInt(1, customerID);
        pst.setInt(2,staffID);
        pst.setDate(3, new Date(System.currentTimeMillis()));

        pst.executeUpdate();

         // Get the generated order ID
         ResultSet generatedKeys = pst.getGeneratedKeys();
         int orderID = 0;
        if (generatedKeys.next()) {
            orderID = generatedKeys.getInt(1);
        }

        while (isContinue) { 
            System.out.print("Enter product ID: ");
            int productID = validateIntInput();

            System.out.print("Enter quantity:   ");
            int quantity = validateIntInput();
            System.out.println();

            // Check if product quantity is sufficient
            pst = conn.prepareStatement("SELECT Name,QuantityInStock,Status FROM products WHERE ProductID = ?");
            pst.setInt(1, productID);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                if(!resultSet.getString(3).equalsIgnoreCase("Discontinued")){
                    availableQuantity = resultSet.getInt(2);
                    if (availableQuantity >= quantity) {
                        isPlaced = true;
                        
                       

                if(isPlaced){
                   // Insert into OrderDetails table using procedure call
                    CallableStatement cst = conn.prepareCall("{CALL insert_orders(?,?,?)}");
                    cst.setInt(1, orderID);
                    cst.setInt(2, productID);
                    cst.setInt(3, quantity);
                    cst.executeUpdate();

                    // Update product quantity
                    updateProductQuantity(conn, productID, availableQuantity - quantity);
                    // Update product sales data
                    updateProductSales(resultSet.getString(1), resultSet.getInt(2));
        
                    // Update customer purchase history
                    updateCustomerPurchases(customerID, resultSet.getString(1));
                }

                
                System.out.println("product added in orders");

                //scanner.nextLine();
                System.out.println("Do you want to add more?(yes/no)?");
                String add = scanner.nextLine();
                if(add.equalsIgnoreCase("no")){
                    System.out.println("Order Placed Successfully.");
                    isContinue = false;
                }   
        }
        else {
            System.out.println("Not enough quantity available.");
        }
    }
    else{
        System.out.println("Product Discontinued");
    }  
}       
        else {
            System.out.println("Product not found.");
        }  
        
    } }catch (SQLException e) {
        System.out.println("Error placing order: " + e.getMessage());
    }
    
}


// Update product quantity
private static void updateProductQuantity(Connection conn, int productID, int quantity) throws SQLException {
    PreparedStatement pst = conn.prepareStatement("UPDATE products SET QuantityInStock = ? WHERE ProductID = ?");
    pst.setInt(1, quantity);
    pst.setInt(2, productID);
    pst.executeUpdate();
    System.out.println("Product quantity updated successfully.");
    bst.loadProductsIntoBST(conn);
}

    // Update product sales data in HashMap
    private static void updateProductSales(String productName, int quantity) {
        if (productSales.containsKey(productName)) {
            int currentSales = productSales.get(productName);
            productSales.put(productName, currentSales + quantity);
        } else {
            productSales.put(productName, quantity);
        }
    }

    // Update customer purchase history in Hashtable
    private static void updateCustomerPurchases(int customerID, String productName) {
        if (customerPurchases.containsKey(customerID)) {
            String currentPurchases = customerPurchases.get(customerID);
            customerPurchases.put(customerID, currentPurchases + ", " + productName);
        } else {
            customerPurchases.put(customerID, productName);
        }
    }

// View order
private static void viewOrder(Connection conn) throws SQLException {
    System.out.print("Enter order ID: ");
    int orderID = validateIntInput();
    PreparedStatement pst = conn.prepareStatement("SELECT o.OrderID, o.CustomerID, od.ProductID, od.Quantity, o.OrderDate " +
                "FROM orders o JOIN OrderDetails od ON o.OrderID = od.OrderID " +
                "WHERE o.OrderID = ?");
    pst.setInt(1, orderID);
    ResultSet resultSet = pst.executeQuery();
    if (resultSet.next()) {
        System.out.println("\nOrder Date: " + resultSet.getTimestamp(5));
        System.out.println("Order ID: " + resultSet.getInt(1));
        System.out.println("Customer ID: " + resultSet.getInt(2));
        System.out.println("Product ID: " + resultSet.getInt(3));
        System.out.println("Quantity: " + resultSet.getInt(4));
        
        // If there are multiple products in the order, you'll need to loop through the result set
        while (resultSet.next()) {
            System.out.println("Product ID: " + resultSet.getInt(3));
            System.out.println("Quantity: " + resultSet.getInt(4));
        }
        System.out.println();
    } else {
        System.out.println("Order not found.");
    }
}

private static void updateOrderDetails(Connection conn, int orderID) throws SQLException {
    PreparedStatement pst = conn.prepareStatement("SELECT * FROM Orders WHERE OrderID = ?");
    pst.setInt(1, orderID);
    ResultSet resultSet = pst.executeQuery();
    if (!resultSet.next()) {
        System.out.println("Order not found.");
        return;
    }

    System.out.print("Enter new customer ID (press enter to skip): ");
    String customerIDStr = scanner.nextLine();
    if (!customerIDStr.isEmpty()) {
        pst = conn.prepareStatement("UPDATE Orders SET CustomerID = ? WHERE OrderID = ?");
        pst.setInt(1, Integer.parseInt(customerIDStr));
        pst.setInt(2, orderID);
        pst.executeUpdate();
    }

    System.out.print("Enter new staff ID (press enter to skip): ");
    String staffIDStr = scanner.nextLine();
    if (!staffIDStr.isEmpty()) {
        pst = conn.prepareStatement("UPDATE Orders SET StaffID = ? WHERE OrderID = ?");
        pst.setInt(1, Integer.parseInt(staffIDStr));
        pst.setInt(2, orderID);
        pst.executeUpdate();
    }

    System.out.print("Enter new order date (press enter to skip): ");
    String orderDateStr = scanner.nextLine();
    if (!orderDateStr.isEmpty()) {
        pst = conn.prepareStatement("UPDATE Orders SET OrderDate = ? WHERE OrderID = ?");
        pst.setDate(1, java.sql.Date.valueOf(orderDateStr));
        pst.setInt(2, orderID);
        pst.executeUpdate();
    }
}

private static void updateProductQuantities(Connection conn, int orderID) throws SQLException {
    System.out.print("Enter product ID to update (or 'done' to finish): ");
    String productIDStr;
    while (!(productIDStr = scanner.nextLine()).equalsIgnoreCase("done")) {
        System.out.print("Enter new quantity (press enter to skip): ");
        String quantityStr = scanner.nextLine();
        if (!quantityStr.isEmpty()) {
            PreparedStatement pst = conn.prepareStatement("UPDATE OrderDetails SET Quantity = ? WHERE OrderID = ? AND ProductID = ?");
            pst.setInt(1, Integer.parseInt(quantityStr));
            pst.setInt(2, orderID);
            pst.setInt(3, Integer.parseInt(productIDStr));
            pst.executeUpdate();
        }
    }
}

private static void addNewProducts(Connection conn, int orderID) throws SQLException {
    System.out.print("Enter new product ID to add (or 'done' to finish): ");
    String productIDStr;
    while (!(productIDStr = scanner.nextLine()).equalsIgnoreCase("done")) {
        System.out.print("Enter quantity: ");
        String quantityStr = scanner.nextLine();
        PreparedStatement pst = conn.prepareStatement("INSERT INTO OrderDetails (OrderID, ProductID, Quantity) VALUES (?, ?, ?)");
        pst.setInt(1, orderID);
        pst.setInt(2, Integer.parseInt(productIDStr));
        pst.setInt(3, Integer.parseInt(quantityStr));
        pst.executeUpdate();
    }
}

public static void updateOrder(Connection conn) throws SQLException {
    try {
        System.out.print("Enter order ID to update: ");
        int orderID = validateIntInput();
       // scanner.nextLine(); // Consume newline left-over

        updateOrderDetails(conn, orderID);
        updateProductQuantities(conn, orderID);
        addNewProducts(conn, orderID);

        System.out.println("Order updated successfully.");
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}

// View all orders
private static void viewAllOrders(Connection conn) throws SQLException {
    PreparedStatement pst = conn.prepareStatement("SELECT o.OrderID, c.Name AS CustomerName, SUM(od.Quantity * p.Price) AS TotalAmount, GROUP_CONCAT(CONCAT(p.Name, ' x ', od.Quantity) SEPARATOR '\n') AS Products " +
            "FROM orders o JOIN OrderDetails od ON o.OrderID = od.OrderID JOIN products p ON od.ProductID = p.ProductID JOIN customers c ON o.CustomerID = c.CustomerID " +
            "GROUP BY o.OrderID, c.Name ORDER BY o.OrderID");
    ResultSet resultSet = pst.executeQuery();
    while (resultSet.next()) {
        System.out.println("\nOrder ID: " + resultSet.getInt(1));
        System.out.println("Customer Name: " + resultSet.getString(2));
        System.out.println("Products: ");
        System.out.println(resultSet.getString(4));
        System.out.println("Total Amount: " + resultSet.getDouble(3));
        System.out.println("-------------");
    }
}

private static void loadSalesAnalysis(Connection conn) throws Exception {
    PreparedStatement pst = conn.prepareStatement("SELECT o.OrderID, p.Name, od.Quantity, c.CustomerID FROM orders o JOIN OrderDetails od ON o.OrderID = od.OrderID JOIN products p ON od.ProductID = p.ProductID JOIN customers c ON o.CustomerID = c.CustomerID ORDER BY o.OrderID DESC");
    ResultSet resultSet = pst.executeQuery();
    while (resultSet.next()) {
        // Update product sales data
        updateProductSales(resultSet.getString(2), resultSet.getInt(3));
        // Update customer purchase history
        updateCustomerPurchases(resultSet.getInt(4), resultSet.getString(2));
    }
}


    // Add payment
    private static void addPayment(Connection conn) throws SQLException {
    try {
        System.out.print("Enter order ID: ");
        int orderID = validateIntInput();
        //scanner.nextLine(); // Consume newline left-over

        PreparedStatement pst = conn.prepareStatement("SELECT SUM(od.Quantity * p.Price) AS TotalAmount " +
                "FROM orders o JOIN OrderDetails od ON o.OrderID = od.OrderID " +
                "JOIN products p ON od.ProductID = p.ProductID " +
                "WHERE o.OrderID = ?");

        pst.setInt(1, orderID);
        ResultSet resultSet = pst.executeQuery();

        if (resultSet.next()) {
            double totalAmount = resultSet.getDouble(1);
            System.out.println("Total amount: " + totalAmount);

            // Check if payment already made
            pst = conn.prepareStatement("SELECT Amount FROM payments WHERE OrderID = ?");
            pst.setInt(1, orderID);
            ResultSet paymentResultSet = pst.executeQuery();

            double paidAmount = 0;
            if (paymentResultSet.next()) {
                paidAmount = paymentResultSet.getDouble(1);
            }

            double pendingAmount = totalAmount - paidAmount;
            System.out.println("Pending amount: " + pendingAmount);

            if (pendingAmount > 0) {
                System.out.print("Enter payment method: ");
                String paymentMethod = scanner.nextLine();
                System.out.print("Enter payment date (yyyy-MM-dd): ");
                String paymentDate = scanner.nextLine();
                java.sql.Date date = java.sql.Date.valueOf(paymentDate);

                pst = conn.prepareStatement("INSERT INTO payments (OrderID, PaymentMethod, PaymentDate, Amount) VALUES (?, ?, ?, ?)");
                pst.setInt(1, orderID);
                pst.setString(2, paymentMethod);
                pst.setDate(3, date);
                pst.setDouble(4, pendingAmount); // Set pending amount

                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Payment added successfully.");
                } else {
                    System.out.println("Failed to add payment.");
                }
            } else {
                System.out.println("Payment already made for this order.");
            }
        } else {
            System.out.println("Order not found.");
        }
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}

    // View payment
    private static void viewPayment(Connection conn) throws SQLException {
        System.out.print("Enter payment ID: ");
        int paymentID = validateIntInput();
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM payments WHERE PaymentID = ?");
        pst.setInt(1, paymentID);
        ResultSet resultSet = pst.executeQuery();
        if (resultSet.next()) {
            System.out.println("\nPayment ID: " + resultSet.getInt(1));
            System.out.println("Order ID: " + resultSet.getInt(2));
            System.out.println("Payment Method: " + resultSet.getString(3));
            System.out.println("Payment Date: " + resultSet.getDate(4));
            System.out.println("Amount: " + resultSet.getDouble(5));
        } else {
            System.out.println("\nPayment not found.\n");
        }
    }

// View all payments
private static void viewAllPayments(Connection conn) throws SQLException {
    PreparedStatement pst = conn.prepareStatement("SELECT * FROM payments");
    ResultSet resultSet = pst.executeQuery();
    while (resultSet.next()) {
        System.out.println("\nPayment ID: " + resultSet.getInt(1));
        System.out.println("Order ID: " + resultSet.getInt(2));
        System.out.println("Payment Method: " + resultSet.getString(3));
        System.out.println("Payment Date: " + resultSet.getDate(4));
        System.out.println("Amount: " + resultSet.getDouble(5));
        System.out.println("-------------");
    }
}

private static void addStaff(Connection conn) throws SQLException {
    System.out.print("Enter staff ID:   ");
    int staffID = validateIntInput();
    //scanner.nextLine(); // consume newline
    System.out.print("Enter staff name: ");
    String name = scanner.nextLine();
    System.out.print("Enter staff email:    ");
    String email = scanner.nextLine();
    System.out.print("Enter staff phone:    ");
    String phone = scanner.nextLine();
    System.out.print("Enter staff position: ");
    String position = scanner.nextLine();
    PreparedStatement pst = conn.prepareStatement("INSERT INTO staff VALUES (?, ?, ?, ?, ?)");
    pst.setInt(1, staffID);
    pst.setString(2, name);
    pst.setString(3, email);
    pst.setString(4, phone);
    pst.setString(5, position);
    pst.executeUpdate();
    System.out.println("\nStaff added successfully.");
}

private static void viewStaff(Connection conn) throws SQLException {
    System.out.print("Enter staff ID:   ");
    int staffID = validateIntInput();
    PreparedStatement pst = conn.prepareStatement("SELECT * FROM staff WHERE staffID = ?");
    pst.setInt(1, staffID);
    ResultSet resultSet = pst.executeQuery();
    if (resultSet.next()) {
        System.out.println("\nStaff ID: " + resultSet.getInt(1));
        System.out.println("Name: " + resultSet.getString(2));
        System.out.println("Email: " + resultSet.getString(3));
        System.out.println("Phone: " + resultSet.getString(4));
        System.out.println("Position: " + resultSet.getString(5));
    } else {
        System.out.println("\nStaff not found.");
    }
}


private static void updateStaff(Connection conn) {
    try {
        int staffID = getStaffID();
        Staff staff = getStaffDetails(staffID, conn);
        if (staff == null) {
            System.out.println("Staff not found.");
            return;
        }

        staff = updateStaffDetails(staff);
        updateStaffInDB(staff, conn);
        System.out.println("Staff updated successfully.");
    } catch (SQLException e) {
        System.out.println("Error updating staff: " + e.getMessage());
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
    }
}

private static int getStaffID() {
    System.out.print("Enter staff ID to update: ");
    return validateIntInput();
}

private static Staff getStaffDetails(int staffID, Connection conn) throws SQLException {
    PreparedStatement pst = conn.prepareStatement("SELECT * FROM Staff WHERE StaffID = ?");
    pst.setInt(1, staffID);
    ResultSet resultSet = pst.executeQuery();
    if (resultSet.next()) {
        return new Staff(
                resultSet.getInt("StaffID"),
                resultSet.getString("Name"),
                resultSet.getString("Email"),
                resultSet.getString("Phone"),
                resultSet.getString("Position")
        );
    }
    return null;
}
private static Staff updateStaffDetails(Staff staff) {
    //scanner.nextLine();
    System.out.print("Enter new name (press enter to skip): ");
    String name = scanner.nextLine();
    if (!name.isEmpty()) {
        staff.setName(name);
    }

    System.out.print("Enter new email (press enter to skip): ");
    String email = scanner.nextLine();
    if (!email.isEmpty()) {
        staff.setEmail(email);
    }

    System.out.print("Enter new phone number (press enter to skip): ");
    String phone = scanner.nextLine();
    if (!phone.isEmpty()) {
        staff.setPhone(phone);
    }
    System.out.print("Enter new position (press enter to skip): ");
    String position = scanner.nextLine();
    if (!position.isEmpty()) {
        staff.setPosition(position);
    }

    return staff;
}

private static void updateStaffInDB(Staff staff, Connection conn) throws SQLException {
    PreparedStatement pst = conn.prepareStatement("UPDATE Staff SET Name = ?, Email = ?, Phone = ?, Position = ? WHERE StaffID = ?");
    pst.setString(1, staff.getName());
    pst.setString(2, staff.getEmail());
    pst.setString(3, staff.getPhone());
    pst.setString(4, staff.getPosition());
    pst.setInt(5, staff.getStaffID());
    pst.executeUpdate();
}


private static void viewAllStaff(Connection conn) throws SQLException {
    PreparedStatement pst = conn.prepareStatement("SELECT * FROM staff");
    ResultSet resultSet = pst.executeQuery();
    while (resultSet.next()) {
        System.out.println("\nStaff ID: " + resultSet.getInt(1));
        System.out.println("Name: " + resultSet.getString(2));
        System.out.println("Email: " + resultSet.getString(3));
        System.out.println("Phone: " + resultSet.getString(4));
        System.out.println("Position: " + resultSet.getString(5));
        System.out.println("-------------");
    }
}
// Generate Bill
private static void generateBill(Connection conn) throws SQLException {
    System.out.print("Enter order ID: ");
    int orderID = validateIntInput();
    System.out.println("");
    PreparedStatement pst = conn.prepareStatement("SELECT * FROM orders WHERE OrderID = ?");
    pst.setInt(1, orderID);
    ResultSet resultSet = pst.executeQuery();

    if (resultSet.next()) {
        int customerID = resultSet.getInt(2);
        Timestamp orderDate = resultSet.getTimestamp(4);

        // Get customer name
        pst = conn.prepareStatement("SELECT Name FROM customers WHERE CustomerID = ?");
        pst.setInt(1, customerID);
        ResultSet customerResultSet = pst.executeQuery();
        String customerName = "";
        if (customerResultSet.next()) {
            customerName = customerResultSet.getString(1);
        }

        // Get order details
        pst = conn.prepareStatement("SELECT p.Name, od.Quantity, p.Price " +
                "FROM orders o JOIN OrderDetails od ON o.OrderID = od.OrderID " +
                "JOIN products p ON od.ProductID = p.ProductID " +
                "WHERE o.OrderID = ?");
        pst.setInt(1, orderID);
        ResultSet orderDetailsResultSet = pst.executeQuery();

        double total = 0.0;
        while (orderDetailsResultSet.next()) {
            String productName = orderDetailsResultSet.getString(1);
            int quantity = orderDetailsResultSet.getInt(2);
            double productPrice = orderDetailsResultSet.getDouble(3);
            double subtotal = quantity * productPrice;
            total += subtotal;
            System.out.println("\nProduct Name: " + productName);
            System.out.println("Quantity: " + quantity);
            System.out.println("Subtotal: " + subtotal);
        }

        // Get staff name
        pst = conn.prepareStatement("SELECT name,staffID FROM staff WHERE staffID = (SELECT staffID FROM orders WHERE OrderID = ?)");
        
        pst.setInt(1, orderID);
        ResultSet staffResultSet = pst.executeQuery();
        String staffName = "";
        if (staffResultSet.next()) {
            staffName = staffResultSet.getString(1);
            //System.out.println("Staff id is " + resultSet.getInt(1));
        }

        System.out.println("\nBill for Order ID: " + orderID);
        System.out.println("Customer Name: " + customerName);
        System.out.println("Total: " + total);
        System.out.println("Served by: " + staffName);
        System.out.println("Order Date: " + orderDate);
        System.out.println("");

        // Insert into payments table if not already paid
        pst = conn.prepareStatement("SELECT * FROM payments WHERE OrderID = ?");
        pst.setInt(1, orderID);
        ResultSet paymentResultSet = pst.executeQuery();
        if (!paymentResultSet.next()) {
            System.out.println("Payment Pending");
            System.out.println("Want to add Payment? (yes/no)");
            String ans = scanner.next();
            if (ans.equalsIgnoreCase("yes")) {
                addPayment(conn);
            } else if (ans.equalsIgnoreCase("no")) {
                System.out.println("Send Remainder to Customer for Payment.");
            }
        } else {
            System.out.println("Payment already made.");
        }
    } else {
        System.out.println("Order not found.");
    }
}




     // Display product sales data
     private static void displayProductSales() {
        System.out.println("Product Sales Data:");
        for (String productName : productSales.keySet()) {
            System.out.println("Product Name: " + productName + ", Sales: " + productSales.get(productName));
        }
    }

    // Display customer purchase history
    private static void displayCustomerPurchases() {
        System.out.println("Customer Purchase History:");
        for (Integer customerID : customerPurchases.keySet()) {
            System.out.println("Customer ID: " + customerID + ", Purchases: " + customerPurchases.get(customerID));
        }
    }
       
// Helper method to validate integer input
public static int validateIntInput() {
    while (true) {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer.\n");
            scanner.next(); // clear invalid input
        }
        finally{
            scanner.nextLine();
        }
    }
}

// Helper method to validate double input
private static double validateDoubleInput() {
    while (true) {
        try {
            return scanner.nextDouble();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.\n");
            scanner.next(); // clear invalid input
        }
    }
}         
}   
