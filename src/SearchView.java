import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchView extends JFrame {

    private JTextField searchField;
    private JTable resultTable = new JTable();
    private Connection connection;

    public SearchView() {

        try{
            connection = Config.connectDB();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        setTitle("Search Page");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel for the components with a grid layout
        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        // Create a panel for the search type selection and home button
        JPanel selectAndHomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JComboBox<String> searchTypeComboBox = new JComboBox<>(new String[]{"Member Record", "Book Record", "Borrow Record"});
        selectAndHomePanel.add(new JLabel("Select Search Type:"));
        selectAndHomePanel.add(searchTypeComboBox);

        // Create a panel for the home button
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Home homepage = new Home();
            }
        });
        selectAndHomePanel.add(homeButton);

        // Create a panel for the search field
        JPanel searchFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(20);
        searchFieldPanel.add(new JLabel("Search Criteria:"));
        searchFieldPanel.add(searchField);

        // Create a panel for the search button
        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String keyword = searchField.getText();
                String record = searchTypeComboBox.getSelectedItem().toString();

                if(record.equals("Member Record"))
                {
                    try
                    {
                        PreparedStatement getMember = connection.prepareStatement("select * from member where memberID LIKE ? or name LIKE ?");
                        getMember.setString(1, keyword + "%");
                        getMember.setString(2, keyword + "%");
                        ResultSet result = getMember.executeQuery();

                        DefaultTableModel tableModel= new DefaultTableModel(
                                new Object[][]{},
                                new Object[]{"No.", "Member ID", "Name", "Major", "Type", "Telephone", "Email", "Penalty"}
                        );

                        if(result.next())
                        {
                            Object[] rowData = new Object[]{
                                    "1", result.getString("memberID"), result.getString("name"),
                                    result.getString("major"), result.getString("level"), result.getString("telephone"),
                                    result.getString("email"), result.getDouble("penalty")
                            };

                            //Add the row to the table model
                            tableModel.addRow(rowData);
                            resultTable.setModel(tableModel);
                        }


                    }catch (SQLException err)
                    {
                        err.printStackTrace();
                    }
                }
                else if(record.equals("Book Record"))
                {
                    try
                    {
                        PreparedStatement getBook = connection.prepareStatement("select * from book where title LIKE ? OR author LIKE ?");
                        getBook.setString(1, keyword + "%");
                        getBook.setString(2, keyword + "%");
                        ResultSet result = getBook.executeQuery();

                        DefaultTableModel tableModel= new DefaultTableModel(
                                new Object[][]{},
                                new Object[]{"No.", "Book ID", "Title", "Author", "Type", "Price", "Status"}
                        );

                        int i = 1;
                        while(result.next())
                        {
                            Object[] rowData = new Object[]{
                                    i, result.getString("bookID"), result.getString("title"),
                                    result.getString("author"), result.getString("type"),
                                    result.getString("price"), result.getString("stats")
                            };

                            //Add the row to the table model
                            tableModel.addRow(rowData);
                            i++;
                        }
                        resultTable.setModel(tableModel);

                    }catch (SQLException err)
                    {
                        err.printStackTrace();
                    }
                }
                else
                {
                    try
                    {
                        PreparedStatement getBorrow = connection.prepareStatement("select * from borrow where memberID = ? AND status = 'Borrowing'");
                        getBorrow.setString(1, keyword);
                        ResultSet result = getBorrow.executeQuery();

                        DefaultTableModel tableModel= new DefaultTableModel(
                                new Object[][]{},
                                new Object[]{"No.", "Book ID", "Member ID", "Borrowed Date", "Expect Return Date"}
                        );

                        int i = 1;
                        while(result.next())
                        {
                            Object[] rowData = new Object[]{
                                    i, result.getString("bookID"), result.getString("memberID"),
                                    result.getString("Bdate"), result.getString("Rdate")
                            };

                            //Add the row to the table model
                            tableModel.addRow(rowData);
                            i++;
                        }
                        resultTable.setModel(tableModel);

                    }catch (SQLException err)
                    {
                        err.printStackTrace();
                    }
                }
            }
        });
        searchButtonPanel.add(searchButton);

        // Create a scroll pane for the table
        JScrollPane tableScrollPane = new JScrollPane(resultTable);

        // Add components to the main panel
        mainPanel.add(selectAndHomePanel);
        mainPanel.add(searchFieldPanel);
        mainPanel.add(searchButtonPanel);

        // Set a new layout with a vertical box layout
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }



}


