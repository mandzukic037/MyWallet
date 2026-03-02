    package storage;

    import common.Store;
    import java.sql.*;
    import java.util.ArrayList;

    public class StoreStorage {
        private static final String DB_URL = "jdbc:sqlite:C:/Users/PC/Desktop/GitAleksa/MyWallet/MyWallet/MyWallet.db";

        public static ArrayList<Store> loadStores() {

            String selectSQL =
                    "SELECT s.Name, st.Name AS Type " +
                    "FROM Store s " +
                    "JOIN StoreType st ON s.IdType = st.IdType";

            ArrayList<Store> stores = new ArrayList<>();

            try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(selectSQL);
                ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    stores.add(new Store(
                            rs.getString("Name"),
                            rs.getString("Type")
                    ));
                }

            } catch (SQLException e) {
                System.out.println("Error loading stores: " + e.getMessage());
            }

            return stores;
        }
    }