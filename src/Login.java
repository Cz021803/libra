import at.favre.lib.crypto.bcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame {

    private JTextField userID;
    private JPasswordField password;
    private Connection connection;
    public static boolean superAdmin;

    public Login()
    {
        try{
            connection = Config.connectDB();
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

        setTitle("Login Form");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1,2));

        // Load the GIF image
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/login.jpg"));
        JLabel label = new JLabel(icon);
        label.setSize(100,150);
        add(label);


        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);


        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(titleLabel, gbc);

        JLabel userIDLabel = new JLabel("User ID");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(userIDLabel, gbc);

        userID = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        loginPanel.add(userID, gbc);

        JLabel pwdLabel = new JLabel("Password");
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(pwdLabel, gbc);

        password = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        loginPanel.add(password, gbc);



        password.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    String uID = userID.getText();
                    String pwd = password.getText();

                    if(validLogin(uID, pwd))
                    {

                        JOptionPane.showMessageDialog(password, "Login Successful");
                        dispose();
                        Home homepage = new Home();

                    }
                    else
                    {
                        JOptionPane.showMessageDialog(password, "User ID or Password may be incorrect");
                    }
                }
            }
        });

        JButton loginBtn = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(loginBtn, gbc);

        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginBtn.setBackground(Color.GRAY); // Change to desired hover color
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginBtn.setBackground(UIManager.getColor("Button.background")); // Reset to default color
            }
        });


        loginBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String uID = userID.getText();
                String pwd = password.getText();
                loginBtn.setBackground(Color.YELLOW);
                if(validLogin(uID, pwd))
                {
                    JOptionPane.showMessageDialog(loginBtn, "Login Successful");
                    dispose();
                    Home homepage = new Home();

                }
                else
                {
                    JOptionPane.showMessageDialog(loginBtn, "User ID or Password may be incorrect");
                }
            }
        });

        add(loginPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private boolean validLogin(String uID, String pwd)
    {
        try{
            PreparedStatement statement = connection.prepareStatement("select * from admin where adminID = ?");
            statement.setString(1,uID);
            ResultSet result = statement.executeQuery();

            if(result.next())
            {
                BCrypt.Result verify = BCrypt.verifyer().verify(pwd.toCharArray(), result.getString("password"));

                if(verify.verified)
                {
                    return true;
                }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

}
