package DBaccess;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

//Author: Koh Ding Yuan
public class salesrecordentityDB {
    public int createSalesRecordDB(
        int memberId, int storeId, double transactionAmount) throws Exception{
        int changesMade = 0, srId = 0;
        try{
            Date now = new Date();
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
            Class.forName("com.mysql.jdbc.Driver");
            String connURL = "jdbc:mysql://localhost:3306/islandfurniture-it07?user=root&password=12345";
            Connection conn = DriverManager.getConnection(connURL);
            String stmt = "Insert into salesrecordentity("
                + "AMOUNTDUE, AMOUNTPAID, CREATEDDATE, CURRENCY, SERVEDBYSTAFF, MEMBER_ID, STORE_ID)"
                + "Values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(stmt,Statement.RETURN_GENERATED_KEYS);
            
            ps.setDouble(1, transactionAmount);
            ps.setDouble(2, transactionAmount);
            ps.setString(3, date);
            ps.setString(4, "SGD");
            ps.setString(5, "IslandFurniture");
            ps.setInt(6, memberId);
            ps.setInt(7, storeId);
            changesMade = ps.executeUpdate();
            if(changesMade == 0)
                System.out.println("createSalesRecordDB error.");
             ResultSet resultSet = ps.getGeneratedKeys();
        resultSet.next();
        srId = resultSet.getInt(1);
            
        }
        catch(Exception e){
            throw e;
        }
        return srId;
    }
}
