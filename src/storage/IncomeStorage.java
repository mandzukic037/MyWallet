package storage;

import common.Income;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import tools.DateUtils;

public class IncomeStorage {
    private static final String DB_URL = "jdbc:sqlite:C:/Users/PC/Desktop/GitAleksa/MyWallet/MyWallet/MyWallet.db";

    public static void saveIncome(Income i) {
        String date = DateUtils.toDatabaseFormat(i.getDate());

        if (date == null ) {
            System.out.println("Invalid date format, income not saved: " + i.getDate());
            return;
        }

        LocalDate incomeDate = LocalDate.parse(date);
        if (incomeDate.isAfter(LocalDate.now())) {
            System.out.println("Cannot save income in the future: " + i.getDate());
            return;
        }

        String insertIncome = "INSERT INTO Income (Date, Description, Amount, Source) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt = conn.prepareStatement(insertIncome)) {

            stmt.setString(1, date);
            stmt.setString(2, i.getDescription());
            stmt.setDouble(3, i.getAmount());
            stmt.setString(4, i.getSource());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Database error saving income: " + ex.getMessage());
        }
    }

    public static ArrayList<Income> loadIncomes() {
        String selectSQL =
                "SELECT Date, Description, Amount, Source " +
                "FROM Income " +
                "ORDER BY Date DESC";

        ArrayList<Income> incomes = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt = conn.prepareStatement(selectSQL);
            ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                incomes.add(new Income(
                        DateUtils.fromDatabaseFormat(rs.getString("Date")),
                        rs.getString("Description"),
                        rs.getDouble("Amount"),
                        rs.getString("Source")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error loading incomes: " + e.getMessage());
        }
        
        return incomes;
    }

    public static ArrayList<Income> loadIncomesBetween(String from, String to) {
        String sql =
            "SELECT Date, Description, Amount, Source " +
            "FROM Income " +
            "WHERE Date BETWEEN ? AND ? " +
            "ORDER BY Date DESC";

        ArrayList<Income> incomes = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, from);
            stmt.setString(2, to);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                incomes.add(new Income(
                        DateUtils.fromDatabaseFormat(rs.getString("Date")),
                        rs.getString("Description"),
                        rs.getDouble("Amount"),
                        rs.getString("Source")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error loading incomes by period: " + e.getMessage());
        }

        return incomes;
    }
}
