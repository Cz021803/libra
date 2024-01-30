import java.sql.Connection;
import java.sql.DriverManager;

public class Admin {

    //connect to database
    private Connection connection;

    public Admin()
    {
        try
        {
            connection = Config.connectDB();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

  public boolean editMember()
  {
      return false;
  }

  public boolean editBook()
  {
      return false;
  }

  public boolean editAdmin()
  {
      return false;
  }

  public boolean registerAdmin()
  {
      return false;
  }

  public boolean deleteMember()
  {
      return false;
  }

  public boolean deleteBook()
  {
      return false;
  }




}
