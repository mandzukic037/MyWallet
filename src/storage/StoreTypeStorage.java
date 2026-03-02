package storage;

import common.StoreType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StoreTypeStorage {
    private static final String DB_URL = "jdbc:sqlite:C:/Users/PC/Desktop/GitAleksa/MyWallet/MyWallet/MyWallet.db";

        public static ArrayList<StoreType> loadStoreTypes() {

            String selectSQL =
                    "SELECT st.Name AS StoreType, ROUND(SUM(t.Amount), 2) AS TotalSpent " +
                    "FROM Transactions t " +
                    "JOIN Store s ON t.IdStore = s.IdStore " +
                    "JOIN StoreType st ON s.IdType = st.IdType " +
                    "GROUP BY st.Name " +
                    "ORDER BY TotalSpent DESC";

            ArrayList<StoreType> storeTypes = new ArrayList<>();

            try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(selectSQL);
                ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    storeTypes.add(new StoreType(
                            rs.getString("StoreType"),
                            rs.getDouble("TotalSpent")
                    ));
                    System.out.println("Loaded store type: " + rs.getString("StoreType") + " with total spent: " + rs.getDouble("TotalSpent"));
                }

            } catch (SQLException e) {
                System.out.println("Error loading stores: " + e.getMessage());
            }

            return storeTypes;
        }
}
