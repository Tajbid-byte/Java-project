import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class BudgetTrackerGUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Map<String, BigDecimal> sectors;
    private BigDecimal totalBudget;
    private JLabel totalBudgetLabel;
    private JLabel remainingBudgetLabel;

    public BudgetTrackerGUI() {
        this.totalBudget = BigDecimal.ZERO;
        this.sectors = new LinkedHashMap<>();
        initializeSectors();

        setTitle("Budget Tracker");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createOverviewPanel(), "Dashboard");
        mainPanel.add(createMyExpensesPanel(), "My Expenses");
        mainPanel.add(createSettingsPanel(), "Settings");
        mainPanel.add(createAnalysisPanel(), "Analysis");
        mainPanel.add(createProfilePanel(), "Profile"); // Include Profile section

        add(createSidebar(), BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void initializeSectors() {
        sectors.put("Education", BigDecimal.ZERO);
        sectors.put("Agriculture", BigDecimal.ZERO);
        sectors.put("Local Government", BigDecimal.ZERO);
        sectors.put("Rural Development", BigDecimal.ZERO);
        sectors.put("Electricity and Fuel", BigDecimal.ZERO);
        sectors.put("Health", BigDecimal.ZERO);
        sectors.put("Defense", BigDecimal.ZERO);
        sectors.put("Public Administration", BigDecimal.ZERO);
        sectors.put("Transportation", BigDecimal.ZERO);
        sectors.put("Social Safety", BigDecimal.ZERO);
        sectors.put("Mega Projects", BigDecimal.ZERO);
    }

    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Overview", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(90, 60, 150));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(220, 240, 250));
        topPanel.add(titleLabel);

        JPanel budgetSummaryPanel = new JPanel(new GridLayout(4, 1));
        budgetSummaryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        budgetSummaryPanel.setBackground(Color.decode("#AA86FF")); // Background color

        totalBudgetLabel = new JLabel("Total Budget: " + totalBudget);
        totalBudgetLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        remainingBudgetLabel = new JLabel("Remaining Budget: " + calculateRemainingBudget());
        remainingBudgetLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton setBudgetButton = new JButton("Set Total Budget");
        setBudgetButton.setBackground(Color.decode("#010517")); // Set the previous color
        setBudgetButton.setForeground(Color.WHITE);
        setBudgetButton.addActionListener(e -> setTotalBudget());
        budgetSummaryPanel.add(totalBudgetLabel);
        budgetSummaryPanel.add(remainingBudgetLabel);
        budgetSummaryPanel.add(setBudgetButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(budgetSummaryPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMyExpensesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("My Expenses", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(90, 60, 150));

        JPanel expensesPanel = new JPanel(new GridLayout(sectors.size(), 1));
        expensesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        expensesPanel.setBackground(Color.decode("#AA86FF"));

        // Create buttons for each sector
        for (Map.Entry<String, BigDecimal> entry : sectors.entrySet()) {
            JPanel sectorPanel = new JPanel(new BorderLayout());
            sectorPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 250)));
            sectorPanel.setBackground(new Color(255, 255, 255));

            JLabel sectorLabel = new JLabel(entry.getKey() + ": $" + entry.getValue());
            sectorLabel.setFont(new Font("Arial", Font.PLAIN, 16));

            JButton allocateButton = new JButton("Allocate");
            allocateButton.setBackground(new Color(33, 150, 243));
            allocateButton.setForeground(Color.WHITE);
            allocateButton.addActionListener(e -> allocateBudget(entry.getKey())); // Allocate budget for this sector

            // Add hover effect for the button
            allocateButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    allocateButton.setBackground(new Color(29, 255, 236)); // Change color on hover
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    allocateButton.setBackground(new Color(33, 150, 243)); // Reset color
                }
            });

            sectorPanel.add(sectorLabel, BorderLayout.CENTER);
            sectorPanel.add(allocateButton, BorderLayout.EAST);
            expensesPanel.add(sectorPanel);
        }

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(expensesPanel), BorderLayout.CENTER); // Scrollable panel

        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(90, 60, 150));

        panel.add(titleLabel, BorderLayout.NORTH);
        // Add theme customization options here

        return panel;
    }

    private JPanel createAnalysisPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Analysis", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(90, 60, 150));

        // Add content for Analysis panel here
        JTextArea analysisArea = new JTextArea("Analysis content goes here...");
        analysisArea.setEditable(false);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(analysisArea), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(90, 60, 150));

        // Add content for Profile panel here
        JTextArea profileArea = new JTextArea("Profile content goes here...");
        profileArea.setEditable(false);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(profileArea), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0, 1));
        sidebar.setBackground(new Color(40, 44, 52));
        sidebar.setPreferredSize(new Dimension(150, getHeight()));
    
        String[] buttonLabels = {"Dashboard", "My Expenses", "Analysis", "Profile", "Settings"};
        String[] iconPaths = {"dashboard.png", "budget.png", "analysis.png", "user1.png", "settings.png"}; // Ensure correct icon paths
    
        for (int i = 0; i < buttonLabels.length; i++) {
            final String label = buttonLabels[i]; // Capture the current label
            ImageIcon icon = new ImageIcon(iconPaths[i]); // Load icon for each button
            JButton button = new JButton(label, icon);
            button.setHorizontalTextPosition(SwingConstants.CENTER); // Center text below the icon
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setBackground(new Color(40, 44, 52));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
    
            // Add mouse listener for zoom effect and color change on hover
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(29, 255, 236)); // Change color on hover
                }
    
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(40, 44, 52)); // Reset color
                }
    
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    cardLayout.show(mainPanel, label); // Navigate to the corresponding panel
                }
            });
    
            sidebar.add(button);
        }
    
        return sidebar;
    }

    private void setTotalBudget() {
        String input = JOptionPane.showInputDialog(this, "Enter total budget:");
        if (input != null) {
            try {
                totalBudget = new BigDecimal(input);
                totalBudgetLabel.setText("Total Budget: $" + totalBudget);
                remainingBudgetLabel.setText("Remaining Budget: $" + calculateRemainingBudget());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
            }
        }
    }

    private BigDecimal calculateRemainingBudget() {
        BigDecimal totalExpenses = sectors.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalBudget.subtract(totalExpenses);
    }

    private void allocateBudget(String sector) {
        String input = JOptionPane.showInputDialog(this, "Enter amount for " + sector + ":");
        if (input != null) {
            try {
                BigDecimal amount = new BigDecimal(input);
                sectors.put(sector, sectors.get(sector).add(amount));
                updateExpenseDisplays();
                remainingBudgetLabel.setText("Remaining Budget: $" + calculateRemainingBudget());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
            }
        }
    }

    private void updateExpenseDisplays() {
        // Refresh displays on the My Expenses panel
        // You might want to refresh the specific panel content here
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BudgetTrackerGUI::new);
    }
}
