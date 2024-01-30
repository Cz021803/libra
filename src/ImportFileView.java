import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImportFileView extends JFrame {

    private Book bookModel = new Book();
    private Member memberModel = new Member();
    private Borrow borrowModel = new Borrow();

    private String fileName = "";
    private String line = "";
    private boolean output = false;

    public ImportFileView()
    {
        setTitle("Import Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        JComboBox<String> importChoiceBox = new JComboBox<>(new String[]{"Member", "Book", "Borrow Record"});
        JButton chooseFileButton = new JButton("Choose File");
        JButton importButton = new JButton("Import");
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
                Home homepage = new Home();
            }
        });
        home.add(homeButton);

        // Add components to the panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(new JLabel("Select Option:"), gbc);

        gbc.gridx = 1;
        mainPanel.add(importChoiceBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(chooseFileButton, gbc);

        gbc.gridx = 1;
        mainPanel.add(importButton, gbc);

        gbc.gridy++;

        // Add action listeners
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle choose file button action

                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    fileName = selectedFile.getName();
                }
            }
        });

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle import button action
                String option = (String) importChoiceBox.getSelectedItem();

                switch(option)
                {
                    case "Member":
                    {
                        try(BufferedReader input = new BufferedReader(new FileReader(fileName))){

                            input.readLine();

                            while((line = input.readLine()) != null)
                            {
                                String data[] = line.split(",");
                                output = memberModel.insertRecord(data[0], data[1], data[2], data[3], data[4], data[5]);
                            }

                            if(output)
                            {
                                JOptionPane.showMessageDialog(null, "Member Record imported successfully");
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null, "Failed to import member record");
                            }

                        }catch(Exception err)
                        {
                            err.printStackTrace();
                        }

                        break;
                    }

                    case "Book":
                    {
                        try(BufferedReader input = new BufferedReader(new FileReader(fileName))){

                            input.readLine();

                            while((line = input.readLine()) != null)
                            {
                                String data[] = line.split(",");

                                output = bookModel.insertRecord(data[0], data[1], data[2], data[3], data[4], Double.parseDouble(data[5]), data[6]);
                            }

                            if(output)
                            {
                                JOptionPane.showMessageDialog(null, "Book Record imported successfully");
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null, "Failed to import book record");
                            }

                        }catch(Exception err)
                        {
                            err.printStackTrace();
                        }

                        break;
                    }

                    case "Borrow Record":
                    {
                        try(BufferedReader input = new BufferedReader(new FileReader(fileName))){

                            input.readLine();

                            while((line = input.readLine()) != null)
                            {
                                String data[] = line.split(",");
                                output = borrowModel.insertRecord(data[0], data[1], data[2], data[3]);
                            }

                            if(output)
                            {
                                JOptionPane.showMessageDialog(null, "Borrow Record imported successfully");
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(null, "Failed to import borrow record");
                            }

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

    public static void main(String[] args)
    {
        new ImportFileView();
    }
}
