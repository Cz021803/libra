import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class ExportFileView extends JFrame {

    private Connection connection;

    // Get current date
    LocalDate currentDate = LocalDate.now();

    // Define format
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Format date as string
    String date = currentDate.format(formatter);


    public ExportFileView()
    {
        try{
            connection = Config.connectDB();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        setTitle("Export Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        JComboBox<String> exportChoiceBox = new JComboBox<>(new String[]{"Member", "Book", "Borrow Record"});
        JButton exportButton = new JButton("Export");
        JButton homeButton = new JButton("Home");

        // Create panel for components with a grid layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        JPanel home = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle home button action
                dispose();
                new Home();
            }
        });
        home.add(homeButton);

        // Add components to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(new JLabel("Select Option:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(exportChoiceBox, gbc);

        gbc.gridx = 2;
        mainPanel.add(exportButton, gbc);

        gbc.gridy++;

        // Add action listeners

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle submit button action
                String option = (String) exportChoiceBox.getSelectedItem();

                switch(option)
                {
                    case "Member":
                    {
                        try
                        {
                            String fileName = "MemberBackup_" + date + ".csv";
                            File file = new File(fileName);
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                            FileWriter output = new FileWriter(fileName);

                            //csv file header
                            output.append("Member ID,Member Name,Member Major,Member Type,Member Phone Number,Member Email,Member Penalty\n");

                            //Fetch the data
                            PreparedStatement getMember = connection.prepareStatement("select * from member");
                            ResultSet result = getMember.executeQuery();

                            //Write the data
                            while(result.next())
                            {
                                output.append(result.getString("memberID"));
                                output.append(",");
                                output.append(result.getString("name"));
                                output.append(",");
                                output.append(result.getString("major"));
                                output.append(",");
                                output.append(result.getString("level"));
                                output.append(",");
                                output.append(result.getString("telephone"));
                                output.append(",");
                                output.append(result.getString("email"));
                                output.append(",");
                                output.append(String.valueOf(result.getDouble("penalty")));
                                output.append("\n");

                            }

                            output.flush();
                            output.close();

                            JOptionPane.showMessageDialog(null, fileName + " created successfully");

                        }catch(Exception err)
                        {
                            err.printStackTrace();
                        }

                        break;
                    }

                    case "Book":
                    {
                        try
                        {
                            String fileName = "BookBackup_" + date + ".csv";
                            File file = new File(fileName);
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                            FileWriter output = new FileWriter(fileName);

                            //csv file header
                            output.append("Book ID,Title,Author,Publisher,Type,Price,ISBN,Added DateTime,Status\n");

                            //Fetch the data
                            PreparedStatement getBook = connection.prepareStatement("select * from book");
                            ResultSet result = getBook.executeQuery();

                            //Write the data
                            while(result.next())
                            {
                                output.append(result.getString("bookID"));
                                output.append(",");
                                output.append(result.getString("title"));
                                output.append(",");
                                output.append(result.getString("author"));
                                output.append(",");
                                output.append(result.getString("publisher"));
                                output.append(",");
                                output.append(result.getString("type"));
                                output.append(",");
                                output.append(result.getString("price"));
                                output.append(",");
                                output.append(String.valueOf(result.getString("ISBN")));
                                output.append(",");
                                output.append(String.valueOf(result.getString("datetime")));
                                output.append(",");
                                output.append(String.valueOf(result.getString("stats")));
                                output.append("\n");

                            }

                            output.flush();
                            output.close();

                            JOptionPane.showMessageDialog(null, fileName + " created successfully");

                        }catch(Exception err)
                        {
                            err.printStackTrace();
                        }

                        break;
                    }

                    case "Borrow Record":
                    {
                        try
                        {
                            String fileName = "BorrowRecordBackUp_" + date + ".csv";
                            File file = new File(fileName);
                            if (!file.exists()) {
                                file.createNewFile();
                            }
                            FileWriter output = new FileWriter(fileName);

                            //csv file header
                            output.append("Book ID,Member ID,Borrow Date,Expected Return Date,Status,Penalty\n");

                            //Fetch the data
                            PreparedStatement getBorrow = connection.prepareStatement("select * from borrow");
                            ResultSet result = getBorrow.executeQuery();

                            //Write the data
                            while(result.next())
                            {
                                output.append(result.getString("bookID"));
                                output.append(",");
                                output.append(result.getString("memberID"));
                                output.append(",");
                                output.append(result.getString("Bdate"));
                                output.append(",");
                                output.append(result.getString("Rdate"));
                                output.append(",");
                                output.append(result.getString("status"));
                                output.append(",");
                                output.append(result.getString("penalty"));
                                output.append("\n");

                            }

                            output.flush();
                            output.close();

                            JOptionPane.showMessageDialog(null, fileName + " created successfully");

                        }catch(Exception err)
                        {
                            err.printStackTrace();
                        }

                        break;
                    }
                }

            }
        });

        // Set layout for the main frame
        setLayout(new BorderLayout());
        add(home, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Center the form on the screen
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
