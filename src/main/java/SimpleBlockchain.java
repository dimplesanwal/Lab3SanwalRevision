import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Define a Block class
class Block {
    private int index;
    private long timestamp;
    private String previousHash;
    private String hash;
    private Customer customer;

    // Constructor
    public Block(int index, String previousHash, Customer customer) {
        this.index = index;
        this.timestamp = new Date().getTime();
        this.previousHash = previousHash;
        this.customer = customer;
        this.hash = calculateHash();
    }

    // Calculate the hash of the block
    public String calculateHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = index + timestamp + previousHash + customer.toString();
            byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    // Getters
    public int getIndex() {
        return index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    // Setters
    public void setIndex(int index) {
        this.index = index;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }
}

// Define a Blockchain class

class Blockchain {
    private List<Block> chain;

    // Constructor
    public Blockchain() {
        chain = new ArrayList<>();
        // Create the genesis block (the first block in the chain)
        chain.add(new Block(0, "0", new Customer(0, "Genesis", "Block")));
    }

    // Add a new block to the blockchain
    public void addBlock(Customer customer) {
        Block previousBlock = chain.get(chain.size() - 1);
        Block newBlock = new Block(previousBlock.getIndex() + 1, previousBlock.getHash(), customer);
        chain.add(newBlock);
    }

    // Update a block in the blockchain
    public void updateBlock(int index, Customer updatedCustomer) {
        if (index >= 0 && index < chain.size()) {
            Block blockToUpdate = chain.get(index);
            blockToUpdate.getCustomer().setName(updatedCustomer.getName());
            blockToUpdate.getCustomer().setAddress(updatedCustomer.getAddress());
            blockToUpdate.setHash(blockToUpdate.calculateHash());
            if (index > 0) {
                blockToUpdate.setPreviousHash(chain.get(index - 1).getHash());
            }
        }
    }

    // Delete a block from the blockchain
    public void deleteBlock(int index) {
        if (index >= 0 && index < chain.size()) {
            chain.remove(index);
            rehashBlocksFrom(index);
        }
    }

    // Rehash blocks from the specified index onwards
    private void rehashBlocksFrom(int startIndex) {
        for (int i = startIndex; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            currentBlock.setIndex(i);
            currentBlock.setHash(currentBlock.calculateHash());
            if (i > 0) {
                currentBlock.setPreviousHash(chain.get(i - 1).getHash());
            }
        }
    }

    public List<Block> getChain() {
        return chain;
    }
}

public class SimpleBlockchain {
    public static void main(String[] args) {
        // Create a new blockchain
        Blockchain blockchain = new Blockchain();

        // Add some blocks to the blockchain with customers
        blockchain.addBlock(new Customer(1, "Alice", "New York"));
        blockchain.addBlock(new Customer(2, "Bob", "London"));
        blockchain.addBlock(new Customer(3, "Charlie", "Tokyo"));

        // Print the blockchain
        for (int i = 1; i < blockchain.getChain().size(); i++) {
            Block block = blockchain.getChain().get(i);
            //System.out.println("Block #" + block.getIndex());
            //System.out.println("Timestamp: " + block.getTimestamp());
            //System.out.println("Previous Hash: " + block.getPreviousHash());
            //System.out.println("Hash: " + block.getHash());
            System.out.println("Customer: " + block.getCustomer());
            System.out.println();
        }
    }
}
