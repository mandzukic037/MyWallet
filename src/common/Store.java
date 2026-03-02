package common;

public class Store {
    private String storeName;
    private String storeType;

    public Store(String storeName, String storeType) {
        this.storeName = storeName;
        this.storeType = storeType;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreType() {
        return storeType;
    }

    @Override
    public String toString() {
        return "Store: " + storeName + "\nType: " + storeType + "\n";
    }
}
