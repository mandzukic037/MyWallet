package storage;

import common.Expense;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import service.AIService;
import tools.DateUtils;

public class ExpenseStorage {
    private static final String DB_URL = "jdbc:sqlite:C:/Users/PC/Desktop/GitAleksa/MyWallet/MyWallet/MyWallet.db";

    public static void clearDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM Transactions");
                stmt.executeUpdate("DELETE FROM Store");
                stmt.executeUpdate("DELETE FROM StoreType");
                stmt.executeUpdate("DELETE FROM Income");
            }
            System.out.println("Database cleared successfully.");
        } catch (SQLException e) {
            System.out.println("Error clearing database: " + e.getMessage());
        }
    }

    public static void saveExpense(Expense e) {
        String date = DateUtils.toDatabaseFormat(e.getDate());
        if (date == null) {
            System.out.println("Invalid date format, expense not saved: " + e.getDate());
            return;
        }

        LocalDate expenseDate = LocalDate.parse(date);
        if (expenseDate.isAfter(LocalDate.now())) {
            System.out.println("Cannot save expense in the future: " + e.getDate());
            return;
        }

        String insertTransaction = "INSERT OR IGNORE INTO Transactions (Date, Description, Amount, IdStore) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            int idStore;
            String storeName = e.getMarketName();

            // Check if store exists
            String selectStore = "SELECT IdStore FROM Store WHERE Name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(selectStore)) {
                stmt.setString(1, storeName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    idStore = rs.getInt("IdStore");
                } else {
                    // Predict store type
                    String storeTypeName = AIService.predictMarketType(storeName);
                    int storeTypeId;

                    // Check if store type exists
                    String selectType = "SELECT IdType FROM StoreType WHERE Name = ?";
                    try (PreparedStatement stmtType = conn.prepareStatement(selectType)) {
                        stmtType.setString(1, storeTypeName);
                        ResultSet typeRs = stmtType.executeQuery();
                        if (typeRs.next()) {
                            storeTypeId = typeRs.getInt("IdType");
                        } else {
                            // Insert new store type
                            String insertStoreType = "INSERT INTO StoreType (Name) VALUES (?)";
                            try (PreparedStatement insertTypeStmt =
                                         conn.prepareStatement(insertStoreType, Statement.RETURN_GENERATED_KEYS)) {
                                insertTypeStmt.setString(1, storeTypeName);
                                insertTypeStmt.executeUpdate();
                                ResultSet keys = insertTypeStmt.getGeneratedKeys();
                                keys.next();
                                storeTypeId = keys.getInt(1);
                            }
                        }
                    }

                    // Insert new store
                    String insertStore = "INSERT INTO Store (Name, IdType) VALUES (?, ?)";
                    try (PreparedStatement insertStoreStmt = conn.prepareStatement(insertStore, Statement.RETURN_GENERATED_KEYS)) {
                        insertStoreStmt.setString(1, storeName);
                        insertStoreStmt.setInt(2, storeTypeId);
                        insertStoreStmt.executeUpdate();
                        ResultSet keys = insertStoreStmt.getGeneratedKeys();
                        keys.next();
                        idStore = keys.getInt(1);
                    }
                }
            }

            // Insert transaction
            try (PreparedStatement insertStmt = conn.prepareStatement(insertTransaction)) {
                insertStmt.setString(1, date);
                insertStmt.setString(2, e.getDescription());
                insertStmt.setDouble(3, e.getAmount());
                insertStmt.setInt(4, idStore);
                insertStmt.executeUpdate();
                System.out.println("Expense saved successfully: " + e);
            }

        } catch (SQLException ex) {
            System.out.println("Error saving expense: " + ex.getMessage());
        }
    }

    public static ArrayList<Expense> loadExpenses() {
        ArrayList<Expense> expenses = new ArrayList<>();
        String selectSQL = """
                SELECT t.Date, t.Description, t.Amount, s.Name 
                FROM Transactions t 
                JOIN Store s ON t.IdStore = s.IdStore 
                ORDER BY t.Date DESC, t.IdTrans DESC
                """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(selectSQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                expenses.add(new Expense(
                        DateUtils.fromDatabaseFormat(rs.getString("Date")),
                        rs.getString("Description"),
                        rs.getDouble("Amount"),
                        rs.getString("Name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error loading expenses: " + e.getMessage());
        }
        return expenses;
    }

    public static ArrayList<Expense> loadExpensesBetween(String from, String to) {
        String sql =
            "SELECT t.Date, t.Description, t.Amount, s.Name " +
            "FROM Transactions t " +
            "JOIN Store s ON t.IdStore = s.IdStore " +
            "WHERE t.Date BETWEEN ? AND ? " +
            "ORDER BY t.Date DESC, t.IdTrans DESC";

        ArrayList<Expense> expenses = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, from);
            stmt.setString(2, to);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                expenses.add(new Expense(
                        DateUtils.fromDatabaseFormat(rs.getString("Date")),
                        rs.getString("Description"),
                        rs.getDouble("Amount"),
                        rs.getString("Name")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error loading expenses by period: " + e.getMessage());
        }

        return expenses;
    }
}