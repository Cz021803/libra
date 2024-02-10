import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditDeleteView extends JFrame {

    private JTextField searchField;
    private Admin adminModel = new Admin();

    public EditDeleteView()
    {
        setTitle("Registration Page");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        String[] registrationOptions = {"Select Option", "Edit Member", "Edit Book", "Edit Admin", "Delete Member", "Delete Book", "Delete Admin"};
        JComboBox<String> registrationChoiceBox = new JComboBox<>(registrationOptions);


        // Set layout for the main frame
        setLayout(new BorderLayout());

        // Create panel for components with a grid layout
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton homeBtn = new JButton("Home");
        controlPanel.add(new JLabel("Select Registration Type:"));
        controlPanel.add(registrationChoiceBox);

        // Create a panel for the search field
        JPanel searchFieldPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(20);
        searchFieldPanel.add(new JLabel("Search Criteria:"));
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        searchFieldPanel.add(searchField);

        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectedOption = (String) registrationChoiceBox.getSelectedItem();
                if(e.getSource() == editBtn && selectedOption.equals("Edit Member"))
                {
                    dispose();
                    adminModel.editMember(searchField.getText());
                }
                else if(e.getSource() == editBtn && selectedOption.equals("Edit Book"))
                {
                    dispose();
                    adminModel.editBook(searchField.getText());
                }
                else if(e.getSource() == editBtn && selectedOption.equals("Edit Admin"))
                {
                    dispose();
                    adminModel.editAdmin(searchField.getText());
                }
                else
                {
                    getContentPane().removeAll();
                    revalidate();
                    repaint();
                    add(controlPanel, BorderLayout.NORTH);
                    return;
                }
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) registrationChoiceBox.getSelectedItem();
                if(e.getSource() == deleteBtn && selectedOption.equals("Delete Member"))
                {
                    getContentPane().removeAll();
                    revalidate();
                    repaint();
                    add(controlPanel, BorderLayout.NORTH);
                    adminModel.deleteMember(searchField.getText());
                }
                else if(e.getSource() == deleteBtn && selectedOption.equals("Delete Book"))
                {
                    getContentPane().removeAll();
                    revalidate();
                    repaint();
                    add(controlPanel, BorderLayout.NORTH);
                    adminModel.deleteBook(searchField.getText());
                }
                else if(e.getSource() == deleteBtn && selectedOption.equals("Delete Admin"))
                {
                    getContentPane().removeAll();
                    revalidate();
                    repaint();
                    add(controlPanel, BorderLayout.NORTH);
                    adminModel.deleteAdmin(searchField.getText());
                }
                else
                {
                    getContentPane().removeAll();
                    revalidate();
                    repaint();
                    add(controlPanel, BorderLayout.NORTH);
                    return;
                }
            }
        });

        // Add action listener to choice box
        registrationChoiceBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectedOption = (String) registrationChoiceBox.getSelectedItem();
                // Show the corresponding registration form based on the selected option
                switch (selectedOption) {
                    case "Edit Member":
                    case "Edit Book":
                    case "Edit Admin":
                        getContentPane().removeAll();
                        revalidate();
                        repaint();
                        add(controlPanel, BorderLayout.NORTH);
                        searchFieldPanel.remove(deleteBtn);
                        searchFieldPanel.add(editBtn);
                        add(searchFieldPanel, BorderLayout.CENTER);
                        setVisible(true);
                        break;
                    case "Delete Member":
                    case "Delete Book":
                    case "Delete Admin":
                        getContentPane().removeAll();
                        revalidate();
                        repaint();
                        add(controlPanel, BorderLayout.NORTH);
                        searchFieldPanel.remove(editBtn);
                        searchFieldPanel.add(deleteBtn);
                        add(searchFieldPanel, BorderLayout.CENTER);
                        setVisible(true);
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


}
