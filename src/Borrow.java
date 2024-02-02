import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Borrow {

    //connect to database
    private Connection connection;

    public Borrow(){

        try{
            connection = Config.connectDB();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public boolean insertRecord(String bookID, String memberID, String Bdate, String Rdate)
    {
        try (PreparedStatement updateStatus = connection.prepareStatement("update book set stats = 'Borrowed' where bookID = ?");
             PreparedStatement insert = connection.prepareStatement("INSERT INTO borrow(bookID, memberID, Bdate, Rdate) VALUES(?,?,?,?)")){

            insert.setString(1, bookID);
            insert.setString(2, memberID);
            insert.setString(3, Bdate);
            insert.setString(4, Rdate);

            if(insert.executeUpdate() == 1)
            {
                updateStatus.setString(1, bookID);
                updateStatus.executeUpdate();
                return true;
            };
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public void returnBook(String bookID)
    {
        try(PreparedStatement returnbook = connection.prepareStatement("update borrow set status = 'Returned' where bookID = ? AND status = 'Borrowing'");
            PreparedStatement getBorrowRecord = connection.prepareStatement("select * from borrow where bookID = ? AND status = 'Borrowing'");
            PreparedStatement updatePenalty = connection.prepareStatement("update member set penalty = penalty + ? where memberID = ?");
            PreparedStatement updateStatus = connection.prepareStatement("update book set stats = 'Available' where bookID = ?"))
        {

            getBorrowRecord.setString(1, bookID);
            ResultSet borrowRecord = getBorrowRecord.executeQuery();

            if(borrowRecord.next())
            {
                //get the current date
                LocalDate currentDate = LocalDate.now();

                //Format the expected return date from the result to LocalDateTime format
                String expectRdate = borrowRecord.getString("Rdate");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate exactRdate= LocalDate.parse(expectRdate, formatter);

                long days = ChronoUnit.DAYS.between(exactRdate,currentDate);

                if(days > 0)
                {
                    double penalty = 1 * days;

                    //return the book
                    returnbook.setString(1, bookID);
                    returnbook.executeUpdate();

                    //update the penalty field in member
                    updatePenalty.setDouble(1, penalty);
                    updatePenalty.setString(2, borrowRecord.getString("memberID"));

                    //Update the book status
                    updateStatus.setString(1, bookID);
                    updateStatus.executeUpdate();
                }
                else
                {
                    //return the book
                    returnbook.setString(1, bookID);
                    returnbook.executeUpdate();

                    //Update the book status
                    updateStatus.setString(1, bookID);
                    updateStatus.executeUpdate();
                }

            }


        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }




    public ResultSet getRecord(String memberID)
    {
        ResultSet result = null;

        try{
            PreparedStatement getBorrow = connection.prepareStatement("select * from borrow");
            result =  getBorrow.executeQuery();


        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        return result;
    }
    //error code: 0(OK), 1 (book is borrowed), 2(member has unpaid penalty), 3(member exceeded borrow limit), 4(other)
    public int validateBorrow(String bookID, String memberID)
    {
        try (
                PreparedStatement isValid = connection.prepareStatement("SELECT * FROM book WHERE bookID = ?");
                PreparedStatement hasPenalty = connection.prepareStatement("SELECT * FROM member WHERE memberID = ?");
                PreparedStatement studentLevel = connection.prepareStatement("SELECT * FROM member WHERE memberID = ?");
                PreparedStatement exceedLimit = connection.prepareStatement("SELECT COUNT(*) FROM borrow WHERE memberID = ? AND status = 'Borrowing'")
        ) {
            // Set parameters for the first three queries
            isValid.setString(1, bookID);
            hasPenalty.setString(1, memberID);
            studentLevel.setString(1, memberID);

            // Execute the first three queries
            ResultSet checkBorrowed = isValid.executeQuery();
            ResultSet checkPenalty = hasPenalty.executeQuery();
            ResultSet level = studentLevel.executeQuery();

            // Check if the book is borrowed
            if (checkBorrowed.next() && "Borrowed".equals(checkBorrowed.getString("stats"))) {
                return 1;
            }


            // Check if there is any unpaid penalty
            else if (checkPenalty.next() && checkPenalty.getInt("penalty") > 0) {
                return 2;
            }

            // Check if member exceeded their borrow limit
            else if (level.next()) {
                int borrowLimit = 0;

                // Set the borrow limit based on the member's level
                switch (level.getString("level")) {
                    case "Student":
                        borrowLimit = 2;
                        break;
                    case "Vip Student":
                    case "Teacher":
                        borrowLimit = 4;
                        break;
                    case "Vip Teacher":
                        borrowLimit = 6;
                        break;
                }

                // Execute the fourth query
                exceedLimit.setString(1, memberID);
                ResultSet checkLimit = exceedLimit.executeQuery();

                // Check if the borrow limit is exceeded
                if (checkLimit.next() && checkLimit.getInt(1) >= borrowLimit) {
                    return 3;
                } else {
                    return 0;
                }
            }
            else{
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return 0;
    }

    public double validateReturn(String bookID)
    {
        try (
                PreparedStatement getBorrowRecord = connection.prepareStatement("SELECT * FROM borrow WHERE bookID = ? AND status = 'Borrowing'");
                PreparedStatement updateMemberPenalty = connection.prepareStatement("UPDATE member SET penalty = penalty + ? WHERE memberID = ?");
                PreparedStatement getPenalty = connection.prepareStatement("SELECT penalty FROM member WHERE memberID = ?");
                PreparedStatement updateBorrowPenalty = connection.prepareStatement("UPDATE borrow set penalty = ? where bookID = ? AND status = 'Borrowing'")
        ) {
            // Set parameters for the first query
            getBorrowRecord.setString(1, bookID);

            // Execute the first query
            ResultSet borrowRecord = getBorrowRecord.executeQuery();

            // Check if there is a borrow record
            if (borrowRecord.next()) {
                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Format the expected return date from the result to LocalDateTime format
                String expectRdate = borrowRecord.getString("Rdate");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate exactRdate = LocalDate.parse(expectRdate, formatter);

                long days = ChronoUnit.DAYS.between(exactRdate, currentDate);

                // Check if there are overdue days
                if (days > 0) {
                    double penalty = 1.0 * days;

                    // Update the penalty field in member table
                    updateMemberPenalty.setDouble(1, penalty);
                    updateMemberPenalty.setString(2, borrowRecord.getString("memberID"));
                    updateMemberPenalty.executeUpdate();

                    //Update the penalty field in borrow table
                    updateBorrowPenalty.setDouble(1, penalty);
                    updateBorrowPenalty.setString(2, bookID);
                    updateBorrowPenalty.executeUpdate();

                    // Return the total penalty of the member
                    getPenalty.setString(1, borrowRecord.getString("memberID"));
                    ResultSet totalPenalty = getPenalty.executeQuery();

                    if (totalPenalty.next()) {
                        returnBook(bookID);
                        return totalPenalty.getDouble("penalty");
                    }
                } else {
                    returnBook(bookID);
                    return 0;
                }
            }
            else
            {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return 0;
    }

}
