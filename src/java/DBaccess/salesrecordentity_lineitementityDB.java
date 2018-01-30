package DBaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class salesrecordentity_lineitementityDB {
    public boolean linkSrLiDB(int srId, int liId) throws Exception{
        int changesMade = 0;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            String connURL = "jdbc:mysql://localhost:3306/islandfurniture-it07?user=root&password=12345";
            Connection conn = DriverManager.getConnection(connURL);
            String stmt = "Insert into salesrecordentity_lineitementity("
                + "SalesRecordEntity_ID, itemsPurchased_ID)"
                + "Values (?, ?)";
            PreparedStatement ps = conn.prepareStatement(stmt);

            ps.setInt(1, srId);
            ps.setInt(2, liId);
            changesMade = ps.executeUpdate();
            if(changesMade == 0)
                System.out.println("linkSrLiDB error.");
        }
        catch(Exception e){
            throw e;
        }
        return (changesMade > 0);
    }
}
