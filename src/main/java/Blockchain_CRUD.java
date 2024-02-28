import java.util.Scanner;

public class Blockchain_CRUD {
    private Blockchain blockchain;

    public Blockchain_CRUD() {
        blockchain = new Blockchain();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("Blockchain CRUD Operations");
            System.out.println("1. Add Customer Block");
            System.out.println("2. View Blockchain");
            System.out.println("3. Update Customer Block");
            System.out.println("4. Delete Customer Block");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addCustomerBlock(scanner);
                    break;
                case 2:
                    viewBlockchain();
                    break;
                case 3:
                    updateCustomerBlock(scanner);
                    break;
                case 4:
                    deleteCustomerBlock(scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        } while (choice != 5);
        scanner.close();
    }

    private void addCustomerBlock(Scanner scanner) {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter customer address: ");
        String address = scanner.nextLine();

        Customer customer = new Customer(blockchain.getChain().size() + 1, name, address);
        blockchain.addBlock(customer);
        System.out.println("Customer block added successfully.");
    }

    private void updateCustomerBlock(Scanner scanner) {
        System.out.print("Enter the index of the customer block to update: ");
        int index = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (index >= 0 && index < blockchain.getChain().size()) {
            System.out.print("Enter new customer name: ");
            String newName = scanner.nextLine();
            System.out.print("Enter new customer address: ");
            String newAddress = scanner.nextLine();

            Customer updatedCustomer = new Customer(index, newName, newAddress);
            blockchain.updateBlock(index, updatedCustomer);
            System.out.println("Customer block updated successfully.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    private void deleteCustomerBlock(Scanner scanner) {
        System.out.print("Enter the index of the customer block to delete: ");
        int index = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (index >= 0 && index < blockchain.getChain().size()) {
            blockchain.deleteBlock(index);
            System.out.println("Customer block deleted successfully.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    private void viewBlockchain() {
        System.out.println("Blockchain:");
        for (Block block : blockchain.getChain()) {
            System.out.println("Block #" + block.getIndex());
            //System.out.println("Timestamp: " + block.getTimestamp());
            //System.out.println("Previous Hash: " + block.getPreviousHash());
            //System.out.println("Hash: " + block.getHash());
            System.out.println("Customer: " + block.getCustomer());
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Blockchain_CRUD crud = new Blockchain_CRUD();
        crud.start();
    }
}
