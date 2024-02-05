import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Home extends JFrame
{
    public Home()
    {
        setSize(800,600);
        setTitle("Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Create a panel for the menu with grid layout
        JPanel menu = new JPanel(new GridLayout(1,5,10,10));

        //Create the menu options
        JButton borrowBtn = createCardButton("Borrow Book");
        JButton returnBtn = createCardButton("Return Book");
        JButton penaltyBtn = createCardButton("Pay Penalty");
        JButton searchBtn = createCardButton("Search");
        JButton registerBtn = createCardButton("Register");
        JButton editDeleteBtn = createCardButton("Edit or Delete");

        menu.add(borrowBtn);
        menu.add(returnBtn);
        menu.add(penaltyBtn);
        menu.add(searchBtn);
        menu.add(registerBtn);
        menu.add(editDeleteBtn);

        JPanel logout = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutBtn = new JButton("Logout");
        JButton importBtn = new JButton("Import");
        JButton exportBtn = new JButton("Export");

        logoutBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logoutBtn.setBackground(Color.GRAY); // Change to desired hover color
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logoutBtn.setBackground(UIManager.getColor("Button.background")); // Reset to default color

            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logoutBtn.setBackground(Color.YELLOW);
                int option = JOptionPane.showConfirmDialog(logoutBtn, "Are you sure you want to logout?", "Logout Confirmation", JOptionPane.YES_NO_OPTION);

                if(option == JOptionPane.YES_OPTION)
                {
                    JOptionPane.showMessageDialog(logoutBtn, "Logout Successful");
                    dispose();
                    Login login = new Login();
                }
            }
        });

        importBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                importBtn.setBackground(Color.GRAY); // Change to desired hover color
            }

            @Override
            public void mouseExited(MouseEvent e) {
                importBtn.setBackground(UIManager.getColor("Button.background")); // Reset to default color
            }
        });


        importBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(e.getSource() == importBtn)
                {
                    dispose();
                    ImportFileView importpage = new ImportFileView();
                }
            }
        });

        exportBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exportBtn.setBackground(Color.GRAY); // Change to desired hover color
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exportBtn.setBackground(UIManager.getColor("Button.background")); // Reset to default color
            }
        });


        exportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == exportBtn)
                {
                    dispose();
                    ExportFileView exportpage = new ExportFileView();
                }
            }
        });
        logout.add(importBtn);
        logout.add(exportBtn);
        logout.add(logoutBtn);


        setLayout(new BorderLayout());
        add(menu, BorderLayout.CENTER);
        add(logout, BorderLayout.NORTH);
        setVisible(true);
    }

    private JButton createCardButton(String buttonText) {
        JButton button = new JButton(buttonText);
        button.setPreferredSize(new Dimension(100, 80)); // Set preferred size for the button
        // Add action listeners for each card button (replace with your logic)

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setFont(new Font("Arial", Font.BOLD, 18)); // Increase font size on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setFont(new Font("Arial", Font.PLAIN, 13)); // Reset to initial font size
            }
        });


        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == button && buttonText.equals("Borrow Book"))
                {
                    dispose();
                    BorrowView borrowview = new BorrowView();
                }
                else if(e.getSource() == button && buttonText.equals("Return Book"))
                {
                    dispose();
                    ReturnView returnview = new ReturnView();
                }
                else if(e.getSource() == button && buttonText.equals("Pay Penalty"))
                {
                    dispose();
                    PenaltyView penaltyview = new PenaltyView();
                }
                else if(e.getSource() == button && buttonText.equals("Search Member/Book/Borrow Record"))
                {
                    dispose();
                    SearchView searchview = new SearchView();
                }
                else if(e.getSource() == button && buttonText.equals("Register Member/Book"))
                {
                    dispose();
                    RegisterView registerview = new RegisterView();

                }
                else
                {
                    dispose();
                    EditDeleteView editdeleteview = new EditDeleteView();
                }
            }
        });
        return button;
    }

}