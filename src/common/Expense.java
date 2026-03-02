package common;


public class Expense {
    private String date;
    private String description;
    private double amount;
    private int idProd;
    public String marketName;

    public Expense(String date, String description, double amount, String marketName) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.marketName = marketName;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public int getIdProd() {
        return idProd;
    }

    public String getMarketName() {
        return marketName;
    }

    @Override
    public String toString() {
        return "Date: " + date + "\nAmount: " + amount + " RSD\n" + "Description: " + description + "\n" + "Market: " + marketName + "\n";
    }
}