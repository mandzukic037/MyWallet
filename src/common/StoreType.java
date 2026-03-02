package common;

public class StoreType {;
    private String storeType;
    private double amount;

    public StoreType(String storeType, double amount) {
        this.storeType = storeType;
        this.amount = amount;       
    }

    public String getStoreType() {
        return storeType;
    }
    
    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Store type: " + storeType + "\nSpent amount: " + amount + "\n";
    }
}
