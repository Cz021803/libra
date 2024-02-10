import at.favre.lib.crypto.bcrypt.BCrypt;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;


public class RegisterView extends JFrame {

    private Book bookModel = new Book();
    private Member memberModel = new Member();
    private Admin adminModel = new Admin();

    private JTextField memberIdField;
    private JTextField memberNameField;
    private JTextField memberEmailField;
    private JTextField majorField;
    private JComboBox<String> typeField;
    private JTextField phoneNumberField;

    private JTextField bookIdField;
    private JTextField titleField;
    private JTextField authorField;
    private JTextField publisherField;
    private JComboBox<String> typeComboBox;
    private JTextField priceField;
    private JTextField isbnField;

    private JTextField adminIDField;
    private JTextField adminNameField;
    private JTextField passwordField;
    private JComboBox<String> type;


    public RegisterView() {

        setTitle("Registration Page");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        String[] registrationOptions = {"Select Option", "Register Member", "Register Book", "Register Admin"};
        JComboBox<String> registrationChoiceBox = new JComboBox<>(registrationOptions);


        // Set layout for the main frame
        setLayout(new BorderLayout());

        // Create panel for components with a grid layout
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton homeBtn = new JButton("Home");
        controlPanel.add(new JLabel("Select Registration Type:"));
        controlPanel.add(registrationChoiceBox);

        // Add action listener to choice box
        registrationChoiceBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectedOption = (String) registrationChoiceBox.getSelectedItem();
                // Show the corresponding registration form based on the selected option
                switch (selectedOption) {
                    case "Register Member":
                        getContentPane().removeAll();
                        revalidate();
                        repaint();
                        add(controlPanel, BorderLayout.NORTH);
                        memberRegisterForm();
                        break;
                    case "Register Book":
                        getContentPane().removeAll();
                        revalidate();
                        repaint();
                        add(controlPanel, BorderLayout.NORTH);
                        bookRegisterForm();
                        break;
                    case "Register Admin":
                        getContentPane().removeAll();
                        revalidate();
                        repaint();
                        add(controlPanel, BorderLayout.NORTH);

                        if(Login.getAdminType().equals("Junior Librarian"))
                        {
                            JOptionPane.showMessageDialog(null, "Only senior librarian can register new admin");
                        }
                        else
                        {
                            adminRegisterForm();
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        homeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
                new Home();
            }
        });
        controlPanel.add(homeBtn);

        // Add components to the main frame
        add(controlPanel, BorderLayout.NORTH);
        // Center the form on the screen
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public void memberRegisterForm() {

        setTitle("Member Registration Form");
        setSize(1000, 800);  // Increased initial size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        memberIdField = new JTextField(20);
        memberNameField = new JTextField(20);
        memberEmailField = new JTextField(20);
        majorField = new JTextField(20);
        typeField = new JComboBox<>(new String[]{"Student", "Vip Student", "Teacher", "Vip Teacher"});
        phoneNumberField = new JTextField(20);

        JButton submitButton = new JButton("Submit");
        JButton homeButton = new JButton("Home");

        // Create panel for components with a grid layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Add header label
        JLabel headerLabel = new JLabel("Member Registration");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(headerLabel, gbc);

        // Reset gridwidth
        gbc.gridwidth = 1;

        // Add components to the panel
        gbc.gridy++;
        addLabelAndField("Member ID:", memberIdField, gbc, mainPanel);
        gbc.gridy++;
        addLabelAndField("Member Name:", memberNameField, gbc, mainPanel);
        gbc.gridy++;
        addLabelAndField("Member Email:", memberEmailField, gbc, mainPanel);
        gbc.gridy++;
        addLabelAndField("Major:", majorField, gbc, mainPanel);
        gbc.gridy++;
        addLabelAndComboBox("Type:", typeField, gbc, mainPanel);
        gbc.gridy++;
        addLabelAndField("Phone Number:", phoneNumberField, gbc, mainPanel);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(submitButton, gbc);

        gbc.gridy++;
        mainPanel.add(homeButton, gbc);

        // Add action listeners
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle submit button action
                String memberID = memberIdField.getText();
                String memberName = memberNameField.getText();
                String major = majorField.getText();
                String type = (String) typeField.getSelectedItem();
                String phone = phoneNumberField.getText();
                String memberEmail = memberEmailField.getText();

                boolean result = memberModel.insertRecord(memberID, memberName, major, type, phone, memberEmail);

                if(result)
                {
                    memberIdField.setText("");
                    memberNameField.setText("");
                    majorField.setText("");
                    typeField.setSelectedIndex(1);
                    phoneNumberField.setText("");
                    memberEmailField.setText("");
                    JOptionPane.showMessageDialog(null, memberName + " is registered successfully");
                }
                else
                {
                    memberIdField.setText("");
                    memberNameField.setText("");
                    majorField.setText("");
                    typeField.setSelectedIndex(1);
                    phoneNumberField.setText("");
                    memberEmailField.setText("");
                    JOptionPane.showMessageDialog(null, "Failed to register " + memberName);
                }
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle home button action
                dispose();
                new Home();
            }
        });

        // Set layout for the main frame
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        // Center the form on the screen
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void bookRegisterForm() {

        setTitle("Book Registration Form");
        setSize(1000, 800);  // Initial size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        bookIdField = new JTextField(20);
        titleField = new JTextField(20);
        authorField = new JTextField(20);
        publisherField = new JTextField(20);
        typeComboBox = new JComboBox<>(new String[]{"Fiction", "Non-Fiction", "Journal", "Reference"});
        priceField = new JTextField(20);
        isbnField = new JTextField(20);

        JButton submitButton = new JButton("Submit");
        JButton homeButton = new JButton("Home");

        // Create panel for components with a grid layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Add header label
        JLabel headerLabel = new JLabel("Book Registration");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(headerLabel, gbc);

        // Reset gridwidth
        gbc.gridwidth = 1;

        // Add components to the panel
        gbc.gridy++;
        addLabelAndField("Book ID:", bookIdField, gbc, mainPanel);
        gbc.gridy++;
        addLabelAndField("Title:", titleField, gbc, mainPanel);
        gbc.gridy++;
        addLabelAndField("Author:", authorField, gbc, mainPanel);
        gbc.gridy++;
        addLabelAndField("Publisher:", publisherField, gbc, mainPanel);
        gbc.gridy++;
        addLabelAndComboBox("Type:", typeComboBox, gbc, mainPanel);
        gbc.gridy++;
        addLabelAndField("Price:", priceField, gbc, mainPanel);
        gbc.gridy++;
        addLabelAndField("ISBN:", isbnField, gbc, mainPanel);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(submitButton, gbc);

        gbc.gridy++;
        mainPanel.add(homeButton, gbc);

        // Add action listeners
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle submit button action
                String bookID = bookIdField.getText();
                String title = titleField.getText();
                String author = authorField.getText();
                String publisher = publisherField.getText();
                String type = (String) typeComboBox.getSelectedItem();
                double price = Double.parseDouble(priceField.getText());
                String isbn = isbnField.getText();

                boolean result = bookModel.insertRecord(bookID, title, author, publisher, type, price, isbn);

                if(result)
                {
                    bookIdField.setText("");
                    titleField.setText("");
                    authorField.setText("");
                    publisherField.setText("");
                    typeComboBox.setSelectedIndex(1);
                    priceField.setText("");
                    isbnField.setText("");
                    JOptionPane.showMessageDialog(null, "Book registered successfully");
                }
                else
                {
                    bookIdField.setText("");
                    titleField.setText("");
                    authorField.setText("");
                    publisherField.setText("");
                    typeComboBox.setSelectedIndex(1);
                    priceField.setText("");
                    isbnField.setText("");
                    JOptionPane.showMessageDialog(null, "Failed to register book");
                }
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle home button action
                dispose();
                new Home();;
            }
        });

        // Set layout for the main frame
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        // Center the form on the screen
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void adminRegisterForm()
    {
        setTitle("Admin Registration Form");
        setSize(1000, 800);  // Initial size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        adminIDField = new JTextField(20);
        adminNameField = new JTextField(20);
        passwordField = new JTextField(20);
        typeField = new JComboBox<>(new String[]{"Junior Librarian", "Senior Librarian"});

        JButton submitButton = new JButton("Submit");
        JButton homeButton = new JButton("Home");

        // Create panel for components with a grid layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Add header label
        JLabel headerLabel = new JLabel("Admin Registration");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(headerLabel, gbc);

        // Reset gridwidth
        gbc.gridwidth = 1;

        // Add components to the panel
        gbc.gridy++;
        addLabelAndField("Admin ID:", adminIDField, gbc, mainPanel);
        gbc.gridy++;
        addLabelAndField("Admin Name:", adminNameField, gbc, mainPanel);
        gbc.gridy++;
        addLabelAndField("Password", passwordField, gbc, mainPanel);
        gbc.gridy++;
        addLabelAndComboBox("Type:", typeField, gbc, mainPanel);
        typeField.setSelectedIndex(0);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(submitButton, gbc);

        gbc.gridy++;
        mainPanel.add(homeButton, gbc);

        // Add action listeners
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle submit button action
                String adminID = adminIDField.getText();
                String adminName = adminNameField.getText();
                String password = passwordField.getText();
                String type = (String) typeField.getSelectedItem();

                //Hashing the password before inserting to the DB
                String hashPw = BCrypt.withDefaults().hashToString(12,password.toCharArray());
                boolean result = adminModel.registerAdmin(adminID, adminName, hashPw,type);

                if(result)
                {
                    adminIDField.setText("");
                    adminNameField.setText("");
                    passwordField.setText("");
                    typeField.setSelectedIndex(1);
                    JOptionPane.showMessageDialog(null, adminID + " is registered");
                }
                else
                {
                    adminIDField.setText("");
                    adminNameField.setText("");
                    passwordField.setText("");
                    typeField.setSelectedIndex(1);
                    JOptionPane.showMessageDialog(null, "Failed to register " + adminID);
                }
            }
        });

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle home button action
                dispose();
                new Home();;
            }
        });

        // Set layout for the main frame
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        // Center the form on the screen
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addLabelAndField(String labelText, JTextField textField, GridBagConstraints gbc, JPanel panel) {
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(textField, gbc);
    }

    private void addLabelAndComboBox(String labelText, JComboBox<String> comboBox, GridBagConstraints gbc, JPanel panel) {
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(comboBox, gbc);
    }

}
