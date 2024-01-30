import javax.xml.transform.Result;
import java.sql.*;
import java.util.Date;

public class Book {

    //connect to database
    private Connection connection;

    public Book(){

        try{
            connection = Config.connectDB();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public ResultSet getRecord()
    {
        ResultSet result = null;
        try
        {
            PreparedStatement getBook = connection.prepareStatement("select * from book where bookID = ?");
            result = getBook.executeQuery();


        }catch(Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public boolean insertRecord(String bookID, String title, String author, String publisher, String type, double price, String isbn)
    {
        try(PreparedStatement insert = connection.prepareStatement("insert into book(bookID, title, author, publisher, type, price, ISBN) values(?,?,?,?,?,?,?)")){

            insert.setString(1, bookID);
            insert.setString(2, title);
            insert.setString(3, author);
            insert.setString(4, publisher);
            insert.setString(5, type);
            insert.setDouble(6, price);
            insert.setString(7, isbn);

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

    public boolean importRecord(String bookID, String title, String author, String publisher, String type, double price, String isbn, String datetime, String status)
    {
        try(PreparedStatement insert = connection.prepareStatement("insert into book(bookID, title, author, publisher, type, price, ISBN, datetime, stats) values(?,?,?,?,?,?,?,?,?)")){

            insert.setString(1, bookID);
            insert.setString(2, title);
            insert.setString(3, author);
            insert.setString(4, publisher);
            insert.setString(5, type);
            insert.setDouble(6, price);
            insert.setString(7, isbn);
            insert.setString(8, datetime);
            insert.setString(9, status);

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
