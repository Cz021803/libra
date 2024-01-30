import javax.xml.transform.Result;
import java.sql.*;

public class Member {

    private Connection connection;

    public Member(){

        try{
            connection = Config.connectDB();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public double getPenalty(String memberID)
    {
        try(PreparedStatement getPenalty = connection.prepareStatement("select penalty from member where memberID = ?"))
        {
            //set parameter
            getPenalty.setString(1, memberID);

            ResultSet result = getPenalty.executeQuery();

            if(result.next())
            {
                return result.getDouble("penalty");
            }

        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean payPenalty(String memberID, double penalty)
    {
        try(PreparedStatement payPenalty = connection.prepareStatement("update member set penalty = penalty - ? where memberID = ?"))
        {
            payPenalty.setDouble(1, penalty);
            payPenalty.setString(2, memberID);

            if(payPenalty.executeUpdate() == 1)
            {
                return true;
            }

        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }


    public boolean insertRecord(String memberID, String memberName, String major, String type, String phone, String memberEmail)
    {
        try(PreparedStatement insert = connection.prepareStatement("insert into member(memberID, name, major, level, telephone, email) values(?,?,?,?,?,?)")){

            insert.setString(1, memberID);
            insert.setString(2, memberName);
            insert.setString(3, major);
            insert.setString(4, type);
            insert.setString(5, phone);
            insert.setString(6, memberEmail);

            if(insert.executeUpdate() == 1)
            {
                return true;
            };

        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

}
