import com.mongodb.*;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.Scanner;

public class MongoDB_CRUD {
    private MongoClient mongoClient;
    private MongoCollection<Document> collection;

    public MongoDB_CRUD(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        // Connect to the database
        MongoDatabase database = mongoClient.getDatabase("myDB");
        // Get the collection
        collection = database.getCollection("customers");
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Show All Customers");
            System.out.println("2. Drop and Create Collection with Dummy Data");
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
                    dropCreateCollection();
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
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            System.out.println("Name ,\tAddress");
            while (cursor.hasNext()) {
                Document document = cursor.next();
                String name = document.getString("Name");
                String address = document.getString("Address");
                System.out.println(name + " ,\t" + address);
            }
        } finally {
            cursor.close();
        }
    }

    private void dropCreateCollection() {
        // Drop the existing collection
        collection.drop();

        // Create the collection
        mongoClient.getDatabase("myDB").createCollection("customers");

        // Insert dummy data into the collection
        addCustomer(new Customer("Jane Audi", "NewYork"));
        addCustomer(new Customer("Smith", "Kentecky"));
        addCustomer(new Customer("Johnson", "London"));

        System.out.println("Collection dropped and recreated with dummy data successfully!");
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
        System.out.println("Enter the name of the customer to update:");
        String oldName = scanner.nextLine();
        System.out.println("Enter the new name:");
        String newName = scanner.nextLine();
        System.out.println("Enter the new address:");
        String newAddress = scanner.nextLine();

        Customer updatedCustomer = new Customer(newName, newAddress);
        updateCustomer(oldName, updatedCustomer);

        System.out.println("Customer updated successfully!");
    }


    private void deleteCustomer(Scanner scanner) {
        System.out.println("Enter the name of the customer to delete:");
        String name = scanner.nextLine();

        deleteCustomer(name);

        System.out.println("Customer deleted successfully!");
    }

    private void addCustomer(Customer customer) {
        Document document = new Document("Name", customer.getName())
                .append("Address", customer.getAddress());
        collection.insertOne(document);
    }

    private void updateCustomer(String oldName, Customer updatedCustomer) {
        Document filter = new Document("Name", oldName);
        Document update = new Document("$set", new Document("Name", updatedCustomer.getName()).append("Address", updatedCustomer.getAddress()));
        collection.updateOne(filter, update);
    }


    private void deleteCustomer(String name) {
        Document filter = new Document("Name", name);
        collection.deleteOne(filter);
    }

    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDB_CRUD mongoDB_CRUD = new MongoDB_CRUD(mongoClient);
        mongoDB_CRUD.start();
    }
}
