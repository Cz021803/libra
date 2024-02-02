import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BorrowView extends JFrame {

    private Borrow borrowModel = new Borrow();
    private Connection connection;

    public BorrowView()
    {
        try{
            connection = Config.connectDB();
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        setTitle("Borrow Page");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create a panel for the borrow form
        JPanel borrowPanel = new JPanel(new FlowLayout());

        JTextField bookID = new JTextField(20);
        JTextField memberID = new JTextField(20);

        borrowPanel.add(createPanel("Book ID", bookID));
        borrowPanel.add(createPanel("Member ID", memberID));

        //Button
        JButton borrowBtn = new JButton("Borrow");
        borrowBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bID = bookID.getText();
                String mID = memberID.getText();

                if(validateBorrow(bID, mID) == -1)
                {
                    JOptionPane.showMessageDialog(BorrowView.this, "Book ID/MemberID might be invalid");
                }
                else if(validateBorrow(bID, mID) == 1)
                {
                    JOptionPane.showMessageDialog(BorrowView.this, "This book is borrowed");
                }
                else if(validateBorrow(bID, mID) == 2)
                {
                    JOptionPane.showMessageDialog(BorrowView.this, "You have unpaid penalty");
                }
                else if(validateBorrow(bID,mID) == 3)
                {
                    JOptionPane.showMessageDialog(BorrowView.this, "You exceeded your borrowing limit");
                }
                else if(validateBorrow(bID, mID) == 4)
                {
                    JOptionPane.showMessageDialog(BorrowView.this, "You status does not exist");
                }
                else
                {
                    try(PreparedStatement studentLevel = connection.prepareStatement("select * from member where memberID = ?");)
                    {
                        //to determine the return date based on the member's level
                        studentLevel.setString(1, mID);
                        ResultSet level = studentLevel.executeQuery();

                        if(level.next() && level.getString("level").equals("Student"))
                        {
                            //get the return date
                            LocalDateTime Bdate = LocalDateTime.now();
                            LocalDateTime Rdate = Bdate.plusDays(7); // Add 7 day

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                            //convert the date to string
                            String formatBdate = Bdate.format(formatter);
                            String formatRdate = Rdate.format(formatter);

                            //insert the record
                            borrowModel.insertRecord(bID, mID, formatBdate, formatRdate);
                            JOptionPane.showMessageDialog(BorrowView.this, "Borrowed Successfully");
                        }
                        else if(level.getString("level").equals("Vip Student"))
                        {
                            //get the return date
                            LocalDateTime Bdate = LocalDateTime.now();
                            LocalDateTime Rdate = Bdate.plusDays(14); // Add 7 day

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd ");

                            //convert the date to string
                            String formatBdate = Bdate.format(formatter);
                            String formatRdate = Rdate.format(formatter);

                            //insert the record
                            borrowModel.insertRecord(bID, mID, formatBdate, formatRdate);
                            JOptionPane.showMessageDialog(BorrowView.this, "Borrowed Successfully");
                        }
                        else if(level.getString("level").equals("Teacher"))
                        {
                            //get the return date
                            LocalDateTime Bdate = LocalDateTime.now();
                            LocalDateTime Rdate = Bdate.plusDays(14); // Add 7 day

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd ");

                            //convert the date to string
                            String formatBdate = Bdate.format(formatter);
                            String formatRdate = Rdate.format(formatter);

                            borrowModel.insertRecord(bID, mID, formatBdate, formatRdate);
                            JOptionPane.showMessageDialog(BorrowView.this, "Borrowed Successfully");
                        }
                        else
                        {
                            //get the return date
                            LocalDateTime Bdate = LocalDateTime.now();
                            LocalDateTime Rdate = Bdate.plusDays(21); // Add 7 day

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd ");

                            //convert the date to string
                            String formatBdate = Bdate.format(formatter);
                            String formatRdate = Rdate.format(formatter);

                            borrowModel.insertRecord(bID, mID, formatBdate, formatRdate);
                            JOptionPane.showMessageDialog(BorrowView.this, "Borrowed Successfully");
                        }

                    }catch(Exception err)
                    {
                        err.printStackTrace();
                    }

                }

                bookID.setText("");
                memberID.setText("");
            }
        });
        borrowPanel.add(borrowBtn);

        JPanel homepage = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton homeBtn = new JButton("Home");
        homeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
                new Home();
            }
        });
        homepage.add(homeBtn);

        setLayout(new BorderLayout());
        add(borrowPanel, BorderLayout.CENTER);
        add(homepage,BorderLayout.NORTH);

        setLocationRelativeTo(null);
        setVisible(true);

    }


    private JPanel createPanel(String label, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.add(new JLabel(label));
        panel.add(component);
        return panel;
    }

    private int validateBorrow(String bookID, String memberID)
    {
        return borrowModel.validateBorrow(bookID, memberID);

    }
}
