import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import java.util.regex.Pattern;

public class LoginFrame extends JFrame {
    private Map<String, String> users = new HashMap<>();
    private final String userFilePath = "users.txt"; // File to store user data
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    public LoginFrame() {
        loadUsers();
        setTitle("LoginFrame");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        setUndecorated(false);

        // Set a custom background image
        try {
            BufferedImage backgroundImage = ImageIO.read(new File("wallpaper.jpg"));
            setContentPane(new ImagePanel(backgroundImage));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome back");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(welcomeLabel, gbc);

        // Label for "Budget Tracking App"
        JLabel budgetLabel = new JLabel("Budget Tracking App");
        budgetLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        budgetLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        panel.add(budgetLabel, gbc);

        // Username text field with placeholder
        JTextField emailField = new JTextField("Email", 15);
        setTextFieldProperties(emailField, gbc, panel, 2);

        // Password text field with placeholder
        JPasswordField passField = new JPasswordField("Password", 15);
        setTextFieldProperties(passField, gbc, panel, 3);

        // Sign In button
        JButton signInButton = new JButton("Sign In");
        configureButton(signInButton, gbc, panel, 5);

        // Action listener for Sign In button
        signInButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passField.getPassword());

            if (isValidEmail(email) && users.containsKey(email) && users.get(email).equals(password)) {
                // Successful login
                JOptionPane.showMessageDialog(null, "Login Successful!");

                // Launch the BudgetTrackerGUI without any initial budget
                BudgetTrackerGUI budgetTrackerGUI = new BudgetTrackerGUI(); // No initial budget needed
                budgetTrackerGUI.setVisible(true);
                dispose(); // Close the login frame
            } else {
                // Failed login
                JOptionPane.showMessageDialog(null, "Invalid email or password.");
            }
        });

  