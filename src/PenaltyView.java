import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PenaltyView extends JFrame {

    private Member memberModel = new Member();

    public PenaltyView()
    {
        setTitle("Penalty Page");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel penaltyPanel = new JPanel(new FlowLayout());

        JTextField memberID = new JTextField(20);
        penaltyPanel.add(createPanel("Member ID", memberID));

        //Button
        JButton searchBtn = new JButton("Search");

        searchBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                searchBtn.setBackground(Color.GRAY); // Change to desired hover color
            }

            @Override
            public void mouseExited(MouseEvent e) {
                searchBtn.setBackground(UIManager.getColor("Button.background")); // Reset to default color
            }
        });

        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(e.getSource() == searchBtn)
                {
                    String mID = memberID.getText();

                    double penalty = memberModel.getPenalty(mID);

                    //display the penalty
                    if(penalty == -1)
                    {
                        searchBtn.setBackground(Color.YELLOW); // Change to desired click color
                        JOptionPane.showMessageDialog(searchBtn, "Please enter a valid member ID");
                    }
                    else if(penalty > 0)
                    {
                        searchBtn.setBackground(Color.YELLOW); // Change to desired click color
                        JOptionPane.showMessageDialog(searchBtn, "You have a penalty due of RM " + penalty);
                    }
                    else
                    {
                        searchBtn.setBackground(Color.YELLOW); // Change to desired click color
                        JOptionPane.showMessageDialog(searchBtn, "You have no penalty due");
                    }
                }


            }
        });
        penaltyPanel.add(searchBtn);

        memberID.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    String mID = memberID.getText();

                    double penalty = memberModel.getPenalty(mID);

                    //display the penalty
                    if(penalty > 0)
                    {
                        JOptionPane.showMessageDialog(searchBtn, "You have a penalty due of RM " + penalty);
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(searchBtn, "You have no penalty due");
                    }
                }
            }
        });

        JTextField amount = new JTextField(20);

        amount.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    String mID = amount.getText();

                    try{
                        Double.parseDouble(amount.getText());
                    }catch(NumberFormatException nfe)
                    {
                        JOptionPane.showMessageDialog(null, "Please enter only numbers for amount");
                    }

                    double penalty = Double.parseDouble(amount.getText());


                    if(penalty <= 0)
                    {
                        JOptionPane.showMessageDialog(null, "Amount must be greater than 0");
                    }

                    boolean result = memberModel.payPenalty(mID, penalty);

                    if(result)
                    {
                        penalty = memberModel.getPenalty(mID);

                        if(penalty > 0)
                        {
                            amount.setText("");
                            JOptionPane.showMessageDialog(null, "You still need to pay RM " + penalty);
                        }
                        else
                        {
                            amount.setText("");
                            JOptionPane.showMessageDialog(null, "You have no penalty due now. Thank you");
                        }
                    }
                    else{

                        amount.setText("");
                        amount.setText("");
                        JOptionPane.showMessageDialog(null, "Please enter a valid Member ID");

                    }
                }
            }
        });
        penaltyPanel.add(createPanel("Amount", amount));

        //Button
        JButton payBtn = new JButton("Pay");
        payBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(e.getSource() == payBtn)
                {
                    String mID = memberID.getText();

                    try{
                        Double.parseDouble(amount.getText());
                    }catch(NumberFormatException nfe)
                    {
                        JOptionPane.showMessageDialog(null, "Please enter only numbers for amount");
                    }

                    double penalty = Double.parseDouble(amount.getText());


                    if(penalty <= 0)
                    {
                        JOptionPane.showMessageDialog(payBtn, "Amount must be greater than 0");
                    }

                    boolean result = memberModel.payPenalty(mID, penalty);

                    if(result)
                    {
                        penalty = memberModel.getPenalty(mID);

                        if(penalty > 0)
                        {
                            amount.setText("");
                            JOptionPane.showMessageDialog(null, "You still need to pay RM " + penalty);
                        }
                        else
                        {
                            amount.setText("");
                            JOptionPane.showMessageDialog(null, "You have no penalty due now. Thank you");
                        }
                    }
                    else{

                        amount.setText("");
                        amount.setText("");
                        JOptionPane.showMessageDialog(null, "Please enter a valid Member ID");

                    }

                }


            }
        });
        penaltyPanel.add(payBtn);

        amount.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    String mID = amount.getText();

                    try{
                        Double.parseDouble(amount.getText());
                    }catch(NumberFormatException nfe)
                    {
                        JOptionPane.showMessageDialog(null, "Please enter only numbers for amount");
                    }

                    double penalty = Double.parseDouble(amount.getText());


                    if(penalty < 0)
                    {
                        JOptionPane.showMessageDialog(payBtn, "Amount must be greater than 0");
                    }

                    boolean result = memberModel.payPenalty(mID, penalty);

                    if(result)
                    {
                        penalty = memberModel.getPenalty(mID);

                        if(penalty > 0)
                        {
                            amount.setText("");
                            JOptionPane.showMessageDialog(null, "You still need to pay RM " + penalty);
                        }
                        else
                        {
                            amount.setText("");
                            JOptionPane.showMessageDialog(null, "You have no penalty due now. Thank you");
                        }
                    }
                    else{

                        amount.setText("");
                        amount.setText("");
                        JOptionPane.showMessageDialog(null, "Please enter a valid Member ID");

                    }
                }
            }
        });

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
        add(penaltyPanel, BorderLayout.CENTER);
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


}