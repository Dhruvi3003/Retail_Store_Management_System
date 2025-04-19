//BST.java
import java.sql.*;
public class BST {

    // Root node of the binary search tree
    public static BSTNode root;

    // Load products from the database into the BST
    public void loadProductsIntoBST(Connection conn) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM Products ORDER BY QuantityInStock");
        ResultSet resultSet = pst.executeQuery();

        while (resultSet.next()) {
            // Create a new Product object from the result set
            Product product = new Product(resultSet.getInt(1),resultSet.getString(2),resultSet.getDouble(3),resultSet.getInt(4),resultSet.getString(5));

            // Insert the product into the BST
            insert(product);
        }
    }

    // Insert a product into the BST
    public void insert(Product product) {
        root = insertRec(root, product);
    }

    // Recursive insert function
    private BSTNode insertRec(BSTNode root, Product product) {
        if (root == null) {

            // If the root is null, create a new node
            root = new BSTNode(product);
            return root;
        }

        // If the product ID is less than the root's product ID, insert into the left subtree
        if (product.productId < root.product.productId)
            root.left = insertRec(root.left, product);

        // If the product ID is greater than the root's product ID, insert into the right subtree
        else if (product.productId > root.product.productId)
            root.right = insertRec(root.right, product);
        return root;
    }

    // View a product by ID
    public void viewProduct() {
        System.out.print("Enter id to search:   ");
        int id = RetailStoreManagement.validateIntInput();
        Product product = searchRec(root,id);
        if(product!=null){ 
            System.out.println("Product ID: " + product.productId);
            System.out.println("Name: " + product.name);
            System.out.println("Price: " + product.price);
            System.out.println("Quantity in Stock: " + product.quantityInStock);
            System.out.println("Status: " + product.Status);

        } else {
            System.out.println("Product not found.");
        }
    }

    // Recursive search function
    public Product searchRec(BSTNode root, int productId) {
        if (root == null || root.product.productId == productId)
            return (root != null) ? root.product : null;

        // If the product ID is less than the root's product ID, search in the left subtree
        if (productId < root.product.productId)
            return searchRec(root.left, productId);
        
        // If the product ID is greater than the root's product ID, search in the right subtree
        return searchRec(root.right, productId);
    }

    // Delete a product by ID
    public void deleteProduct(Connection conn) throws Exception{
        try {
            conn.setAutoCommit(false);
            System.out.print("Enter id to delete:   ");
            int id = RetailStoreManagement.validateIntInput();
            root = deleteRec(root, id);
            
            // Update the product status to 'Discontinued' and quantity to 0
            PreparedStatement pst = conn.prepareStatement("UPDATE Products SET Status = 'Discontinued', QuantityInStock = 0, dateTo = ? WHERE ProductID = ?");
            pst.setDate(1, new Date(System.currentTimeMillis()));
            pst.setInt(2, id);
            
            conn.setAutoCommit(true);
            if(pst.executeUpdate()>0){
                System.out.println("Product dicontinued Successfully");
            }
            else{
                System.out.println("Unable to delete Product");
            }
            conn.setAutoCommit(true);
        
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    // Recursive delete function
    private BSTNode deleteRec(BSTNode root, int productId) {
        if (root == null) return root;

        // If the product ID is less than the root's product ID, delete from the left subtree
        if (productId < root.product.productId)
            root.left = deleteRec(root.left, productId);

        // If the product ID is greater than the root's product ID, delete from the right subtree
        else if (productId > root.product.productId)
            root.right = deleteRec(root.right, productId);

        // If the product ID matches the root's product ID, delete the node
        else {
            if (root.left == null) return root.right;
            else if (root.right == null) return root.left;
            root.product = minValue(root.right);
            root.right = deleteRec(root.right, root.product.productId);
        }
        return root;
    }

    // Find the product with the minimum value in the BST
    private Product minValue(BSTNode root) {
        Product min = root.product;

        // Traverse the left subtree to find the minimum value
        while (root.left != null) {
            min = root.left.product;
            root = root.left;
        }
        return min;
    }

    // Get the total number of products in the BST
    public int getTotalProducts() {
        return getTotalProductsRec(root,false);
    }

    // Recursive function to count total products
    private int getTotalProductsRec(BSTNode root,boolean includeDiscontinued) {
        if (root == null) {
            return 0;
        }
        int count = 0;

        // If the product is not discontinued or includeDiscontinued is true, increment count
        if (includeDiscontinued || !root.product.Status.equals("Discontinued")) {
            count = 1;
        }

        // Recursively count products in left and right subtrees
        return count + getTotalProductsRec(root.left, includeDiscontinued) + getTotalProductsRec(root.right, includeDiscontinued);
    }

    // Get the total stock value of all products in the BST
    public double getTotalStockValue() {
        return getTotalStockValueRec(root,false);
    }

    // Recursive function to calculate total stock value
    private double getTotalStockValueRec(BSTNode root, boolean includeDiscontinued) {
        if (root == null) {
            return 0;
        }
        double value = 0;

        // If the product is not discontinued or includeDiscontinued is true, add its stock value
        if (includeDiscontinued || !root.product.Status.equals("Discontinued")) {
            value = root.product.price * root.product.quantityInStock;
        }

        // Recursively calculate stock value in left and right subtrees
        return value + getTotalStockValueRec(root.left, includeDiscontinued) + getTotalStockValueRec(root.right, includeDiscontinued);

    }

    // Get the average price of all products in the BST
    public double getAverageProductPrice() {
        int totalProducts = getTotalProducts();
        if (totalProducts == 0) {
            return 0;
        }
        double totalProductPrice = getTotalProductPriceRec(root);
        return totalProductPrice / totalProducts;
    }

    // Recursive function to calculate total product price
    private double getTotalProductPriceRec(BSTNode root) {
        if (root == null) {
            return 0;
        }

        // Add the product's price to the total price
        return root.product.price + getTotalProductPriceRec(root.left) + getTotalProductPriceRec(root.right);
    }

    // Count the number of discontinued products in the BST
    public int countDiscontinuedProducts() {
        return countDiscontinuedProductsRec(root);
    }
    
    // Recursive function to count discontinued products
    private int countDiscontinuedProductsRec(BSTNode root) {
        if (root == null) {
            return 0;
        }
        int count = root.product.Status.equals("Discontinued") ? 1 : 0;

        // Recursively count discontinued products in left and right subtrees
        return count + countDiscontinuedProductsRec(root.left) + countDiscontinuedProductsRec(root.right);
    }
    
    
}



class BSTNode {
    Product product;
    BSTNode left, right;

    public BSTNode(Product product) {
        this.product = product;
        left = right = null;
    }
}

class Product {
    int productId;
    String name;
    double price;
    int quantityInStock;
    String Status;
    
    public Product(){
        
    }
    public Product(int productID, String name, double price, int quantityInStock, String Status) {
        this.productId = productID;
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.Status = Status;
    }
    public int getProductID() {
        return productId;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantityInStock() {
        return quantityInStock;
    }
    public String getStatus() {
        return Status;
    }


    public void setProductID(int productID) {
        this.productId = productID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
    public void setStatus(String status) {
        Status = status;
    }
    

    
    
}