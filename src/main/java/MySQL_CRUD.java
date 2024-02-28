import java.sql.*;
import java.util.Scanner;

public class MySQL_CRUD {
    private Connection connection;

    public MySQL_CRUD(Connection connection) {
        this.connection = connection;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Show All Customers");
            System.out.println("2. Drop and Create Table with Dummy Data");
            System.out.println("3. Add New Customer");
            System.out.println("4. Update Customer");
            System.out.println("5. Delete Customer");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    showAllCustomers();
                    break;
                case 2:
                    dropCreateTable();
                    break;
                case 3:
                    addNewCustomer(scanner);
                    break;
                case 4:
                    updateCustomer(scanner);
                    break;
                case 5:
                    deleteCustomer(scanner);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showAllCustomers() {
        try {
            // Fetch customers from the database
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customers");

            System.out.println("All Customers:");
            System.out.println("CustomerID \t Name \t Address");
            while (resultSet.next()) {
                int customerId = resultSet.getInt("CustomerID");
                String name = resultSet.getString("Name");
                String address = resultSet.getString("Address");
                System.out.println(customerId + "\t " + name + " \t " + address);
            }
        } catch (SQLException ex) {
            System.out.println("Failed to fetch customers: " + ex.getMessage());
        }
    }

    private void dropCreateTable() {
        try {
            Statement statement = connection.createStatement();
            // Drop table if exists
            statement.executeUpdate("DROP TABLE IF EXISTS customers");
            // Create table
            statement.executeUpdate("CREATE TABLE customers ("
                    + "CustomerID INT AUTO_INCREMENT PRIMARY KEY,"
                    + "Name VARCHAR(255) NOT NULL,"
                    + "Address VARCHAR(255) NOT NULL)");
            // Insert dummy data
            statement.executeUpdate("INSERT INTO customers (Name, Address) VALUES "
                    + "('Jane Audi', 'NewYork'), "
                    + "('Smith', 'Kentcky'), "
                    + "('Johnson', 'HOlland')");
            System.out.println("Table dropped and recreated with dummy data successfully!");
        } catch (SQLException ex) {
            System.out.println("Failed to drop and recreate table: " + ex.getMessage());
        }
    }

    private void addNewCustomer(Scanner scanner) {
        System.out.println("Enter customer name:");
        String name = scanner.nextLine();
        System.out.println("Enter customer address:");
        String address = scanner.nextLine();

        Customer customer = new Customer(name, address);
        addCustomer(customer);

        System.out.println("Customer added successfully!");
    }

    private void updateCustomer(Scanner scanner) {
        System.out.println("Enter the CustomerID of the customer to update:");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        System.out.println("Enter the new name:");
        String newName = scanner.nextLine();
        System.out.println("Enter the new address:");
        String newAddress = scanner.nextLine();

        Customer updatedCustomer = new Customer(customerId, newName, newAddress);
        updateCustomer(updatedCustomer);

        System.out.println("Customer updated successfully!");
    }

    private void deleteCustomer(Scanner scanner) {
        System.out.println("Enter the CustomerID of the customer to delete:");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        deleteCustomer(customerId);

        System.out.println("Customer deleted successfully!");
    }

    private void addCustomer(Customer customer) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO customers (Name, Address) VALUES (?, ?)");
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getAddress());
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Failed to add customer: " + ex.getMessage());
        }
    }

    private void updateCustomer(Customer customer) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE customers SET Name=?, Address=? WHERE CustomerID=?");
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getAddress());
            statement.setInt(3, customer.getCustomerId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Failed to update customer: " + ex.getMessage());
        }
    }

    private void deleteCustomer(int customerId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM customers WHERE CustomerID=?");
            statement.setInt(1, customerId);
            statement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Failed to delete customer: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        // Connect to MySQL database
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myDB", "root", "");
            MySQL_CRUD mySQL_CRUD = new MySQL_CRUD(connection);
            mySQL_CRUD.start();
        } catch (SQLException ex) {
            System.out.println("Failed to connect to MySQL database: " + ex.getMessage());
        }
    }
}
