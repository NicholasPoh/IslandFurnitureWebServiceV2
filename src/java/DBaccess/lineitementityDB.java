package DBaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

//Author: Koh Ding Yuan
public class lineitementityDB {

    public boolean createLineItemRecordDB(int srId, int itemId, int itemQuantity) throws Exception {
        int changesMade = 0, liId = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connURL = "jdbc:mysql://localhost:3306/islandfurniture-it07?user=root&password=12345";
            Connection conn = DriverManager.getConnection(connURL);
            String stmt = "Insert into lineitementity("
                    + "ITEM_ID, QUANTITY)"
                    + "Values (?, ?)";
            PreparedStatement ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, itemId);
            ps.setInt(2, itemQuantity);
            changesMade = ps.executeUpdate();
            if (changesMade == 0) {
                System.out.println("createLineItemRecordDB error.");
            }
            ResultSet resultSet = ps.getGeneratedKeys();
            resultSet.next();
            liId = resultSet.getInt(1);
            // liId = ps.getGeneratedKeys().getInt(1);
        } catch (Exception e) {
            System.out.println("Create line error");
            throw e;
        }
        salesrecordentity_lineitementityDB sldb
                = new salesrecordentity_lineitementityDB();
        return sldb.linkSrLiDB(srId, liId);
    }

    public boolean updateStockDB(int itemId, int itemQuantity, String itemName) throws Exception {
        int[] storeIds = {59, 60, 61};
        int storeOption = 0;
        while (itemQuantity != 0 && storeOption < 3) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String connURL = "jdbc:mysql://localhost:3306/islandfurniture-it07?user=root&password=12345";
                Connection conn = DriverManager.getConnection(connURL);

                //Get Store Stock
                String stmt
                        = "Select QUANTITY from lineitementity where ITEM_ID = ? and ID IN"
                        + "(Select lineItems_ID from storagebinentity_lineitementity where StorageBinEntity_ID="
                        + "(Select ID from storagebinentity where NAME = ? and WAREHOUSE_ID="
                        + "(Select WAREHOUSE_ID from storeentity where ID = ?"
                        + ")))";
                PreparedStatement ps = conn.prepareStatement(stmt);
                int currentStore = storeOption;
                ps.setInt(1, itemId);
                ps.setString(2, itemName + " Storage Area");
                ps.setInt(3, storeIds[storeOption]);
                //int storeStock = ps.executeQuery().getInt("QUANTITY");
                ResultSet resultSet = ps.executeQuery();
                resultSet.next();
                int storeStock = resultSet.getInt("QUANTITY");

                //Calculate Store Stock
                if (storeStock < itemQuantity) {
                    itemQuantity -= storeStock;
                    storeStock = 0;
                    storeOption++;
                } else if (storeStock == itemQuantity) {
                    storeStock = 0;
                    itemQuantity = 0;
                } else {
                    storeStock -= itemQuantity;
                    itemQuantity = 0;
                }

                //Update Store Stock
                stmt
                        = "Update lineitementity set QUANTITY = ? where ITEM_ID = ? and ID in"
                        + "(Select lineItems_ID from storagebinentity_lineitementity where StorageBinEntity_ID="
                        + "(Select ID from storagebinentity where NAME = ? and WAREHOUSE_ID="
                        + "(Select WAREHOUSE_ID from storeentity where ID = ?"
                        + ")))";
                ps = conn.prepareStatement(stmt);
                ps.setInt(1, storeStock);
                ps.setString(2, itemName + " Storage Area");
                ps.setInt(3, itemId);
                ps.setInt(4, storeIds[currentStore]);
            } catch (Exception e) {
                System.out.println("update line error");
                throw e;
            }
        }
        return true;
    }
}
