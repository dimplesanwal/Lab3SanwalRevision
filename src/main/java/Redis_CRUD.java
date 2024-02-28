import redis.clients.jedis.Jedis;
import java.util.List;
import java.util.Scanner;

public class Redis_CRUD {
    private Jedis jedis;

    public Redis_CRUD(Jedis jedis) {
        this.jedis = jedis;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Add Customer");
            System.out.println("2. Delete Customer");
            System.out.println("3. Update Customer");
            System.out.println("4. Show All Customers");
            System.out.println("5. Remove All Data");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    addCustomer(scanner);
                    break;
                case 2:
                    deleteCustomer(scanner);
                    break;
                case 3:
                    updateCustomer(scanner);
                    break;
                case 4:
                    showAllCustomers();
                    break;
                case 5:
                    removeAllData();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addCustomer(Scanner scanner) {
        System.out.println("Enter customer name:");
        String name = scanner.nextLine();
        System.out.println("Enter customer address:");
        String address = scanner.nextLine();
        Customer customer = new Customer(name, address);
        String customerJson = customerToJson(customer); // Serialize Customer object to JSON
        jedis.rpush("customers", customerJson);
        System.out.println("Customer added successfully!");
    }

    private void deleteCustomer(Scanner scanner) {
        System.out.println("Enter the name of the customer to delete:");
        String name = scanner.nextLine();
        List<String> customers = jedis.lrange("customers", 0, -1);
        for (String customerJson : customers) {
            Customer customer = jsonToCustomer(customerJson); // Deserialize JSON to Customer object
            if (customer.getName().contains(name)) {
                jedis.lrem("customers", 0, customerJson);
                System.out.println("Customer deleted successfully!");
                return;
            }
        }
        System.out.println("Customer not found.");
    }

    private void updateCustomer(Scanner scanner) {
        System.out.println("Enter the name of the customer to update:");
        String oldName = scanner.nextLine();
        System.out.println("Enter the new name:");
        String newName = scanner.nextLine();
        System.out.println("Enter the new address:");
        String newAddress = scanner.nextLine();
        Customer updatedCustomer = new Customer(newName, newAddress);
        String updatedCustomerJson = customerToJson(updatedCustomer); // Serialize updated Customer object to JSON
        List<String> customers = jedis.lrange("customers", 0, -1);
        for (String customerJson : customers) {
            Customer customer = jsonToCustomer(customerJson); // Deserialize JSON to Customer object
            if (customer.getName().equals(oldName)) {
                jedis.lrem("customers", 0, customerJson);
                jedis.rpush("customers", updatedCustomerJson);
                System.out.println("Customer updated successfully!");
                return;
            }
        }
        System.out.println("Customer not found.");
    }

    private void showAllCustomers() {
        List<String> customers = jedis.lrange("customers", 0, -1);
        System.out.println("All Customers:");
        for (String customerJson : customers) {
            Customer customer = jsonToCustomer(customerJson); // Deserialize JSON to Customer object
            System.out.println(customer);
        }
    }

    private void removeAllData() {
        jedis.del("customers");
        System.out.println("All data removed from the database.");
    }

    private String customerToJson(Customer customer) {
        // Manually serialize Customer object to JSON string
        return "{\"name\":\"" + customer.getName() + "\",\"address\":\"" + customer.getAddress() + "\"}";
    }

    private Customer jsonToCustomer(String customerJson) {
        // Manually deserialize JSON string to Customer object
        String[] parts = customerJson.split(",");
        String name = parts[0].substring(8); // Extract name
        String address = parts[1].substring(10, parts[1].length() - 1); // Extract address
        name=name.replace("\"","");
        address=address.replace("\"","");
        return new Customer(name, address);
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        Redis_CRUD redisCrud = new Redis_CRUD(jedis);
        redisCrud.start();
    }
}
