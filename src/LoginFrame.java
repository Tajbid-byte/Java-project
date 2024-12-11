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

        // Sign Up button
        JButton signUpButton = new JButton("Sign Up");
        configureButton(signUpButton, gbc, panel, 6);

        // Action listener for Sign Up button
        signUpButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passField.getPassword());

            if (isValidEmail(email)) {
                if (!users.containsKey(email)) {
                    users.put(email, password);
                    saveUser(email, password); // Save user to file
                    JOptionPane.showMessageDialog(null, "Sign Up Successful! You can now sign in.");
                } else {
                    JOptionPane.showMessageDialog(null, "User already exists.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid email format.");
            }
        });

        // Add the panel to a new panel that centers it
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(panel, new GridBagConstraints());

        // Add center panel to frame
        getContentPane().add(centerPanel, BorderLayout.CENTER);
    }

    private void setTextFieldProperties(JTextField textField, GridBagConstraints gbc, JPanel panel, int gridY) {
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField.setForeground(Color.WHITE);
        textField.setOpaque(false);
        textField.setBackground(new Color(255, 255, 255, 50));
        textField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        panel.add(textField, gbc);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals("Email") || textField.getText().equals("Password")) {
                    textField.setText(""); // Clear the placeholder text
                    textField.setForeground(Color.WHITE); // Keep text white
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.WHITE); // Restore placeholder color to white
                    textField.setText(textField instanceof JPasswordField ? "Password" : "Email");
                }
            }
        });
    }

    private void configureButton(JButton button, GridBagConstraints gbc, JPanel panel, int gridY) {
        button.setBackground(new Color(255, 51, 153));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        gbc.gridx = 0;
        gbc.gridy = gridY;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(button, gbc);
    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(userFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]); // Add user to the map
                }
            }
        } catch (IOException e) {
            // Handle the case where the file does not exist or cannot be read
            System.err.println("Could not load users: " + e.getMessage());
        }
    }

    private void saveUser(String email, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFilePath, true))) {
            writer.write(email + ":" + password);
            writer.newLine(); // New line for next user
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving user data: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    // Custom panel for background image
    private class ImagePanel extends JPanel {
        private BufferedImage image;

        public ImagePanel(BufferedImage image) {
            this.image = image;
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Main method for the login system
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}