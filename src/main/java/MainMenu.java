import redis.clients.jedis.Jedis;
import com.mongodb.client.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class MainMenu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select Database:");
        System.out.println("1. MySQL");
        System.out.println("2. MongoDB");
        System.out.println("3. Redis");
        System.out.println("4. Blockchain");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        // Connect to the selected database
        switch (choice) {
            case 1:
                connectToMySQL(scanner);
                break;
            case 2:
                connectToMongoDB(scanner);
                break;
            case 3:
                connectToRedis(scanner);
                break;
            case 4:
                connectToBlockchain();
                break;
            default:
                System.out.println("Invalid choice. Exiting...");
        }
    }

    private static void connectToMySQL(Scanner scanner) {
        System.out.print("Enter MySQL Host: ");
        String host = scanner.nextLine();
        System.out.print("Enter MySQL Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter MySQL Password: ");
        String password = scanner.nextLine();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + "/myDB", username, password);
            System.out.println("Connected to MySQL database successfully!");
            new MySQL_CRUD(connection).start();
        } catch (SQLException ex) {
            System.out.println("Failed to connect to MySQL database: " + ex.getMessage());
        }
    }

    private static void connectToMongoDB(Scanner scanner) {
        System.out.print("Enter MongoDB Host: ");
        String host = scanner.nextLine();
        try {
            MongoClient mongoClient = MongoClients.create("mongodb://" + host + ":27017"); // Assuming default MongoDB port
            System.out.println("Connected to MongoDB successfully!");
            new MongoDB_CRUD(mongoClient).start();
        } catch (Exception ex) {
            System.out.println("Failed to connect to MongoDB: " + ex.getMessage());
        }
    }

    private static void connectToRedis(Scanner scanner) {
        System.out.print("Enter Redis Host: ");
        String host = scanner.nextLine();
        try {
            Jedis jedis = new Jedis(host);
            jedis.connect();
            System.out.println("Connected to Redis successfully!");
            new Redis_CRUD(jedis).start();
        } catch (Exception ex) {
            System.out.println("Failed to connect to Redis: " + ex.getMessage());
        }
    }
    private static void connectToBlockchain() {
        System.out.println("Connected to Blockchain successfully!");
        new Blockchain_CRUD().start();
    }
}
