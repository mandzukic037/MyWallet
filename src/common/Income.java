package common;

public class Income {
    private String date;
    private String description;
    private double amount;
    private String source;

    public Income(String date, String description, double amount, String source) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.source = source;
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

    public String getSource() { 
        return source; 
    }

    @Override
    public String toString() {
        return "Date: " + date + "\nAmount: " + amount + " RSD\n" + "Description: " + description + "\n" + "Source: " + source + "\n";
    }
}