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
    private JPanel analysisPanel; // Store reference to Analysis panel for refreshing

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
        analysisPanel = createAnalysisPanel(); // Initialize analysisPanel here
        mainPanel.add(analysisPanel, "Analysis");
        mainPanel.add(createProfilePanel(), "Profile");

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
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(90, 60, 150));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(220, 240, 250));
        topPanel.add(titleLabel);

        JPanel budgetSummaryPanel = new JPanel(new GridLayout(4, 1));
        budgetSummaryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        budgetSummaryPanel.setBackground(Color.decode("#ccd5ff"));

        totalBudgetLabel = new JLabel("Total Budget: " + totalBudget);
        totalBudgetLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        remainingBudgetLabel = new JLabel("Remaining Budget: " + calculateRemainingBudget());
        remainingBudgetLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton setBudgetButton = new JButton("Set Total Budget");
        setBudgetButton.setBackground(Color.decode("#0e0725"));
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
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(90, 60, 150));

        JPanel expensesPanel = new JPanel(new GridLayout(sectors.size(), 1));
        expensesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        expensesPanel.setBackground(Color.decode("#ccd5ff"));

        for (Map.Entry<String, BigDecimal> entry : sectors.entrySet()) {
            JPanel sectorPanel = new JPanel(new BorderLayout());
            sectorPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 250)));
            sectorPanel.setBackground(new Color(255, 255, 255));

            JLabel sectorLabel = new JLabel(entry.getKey() + ": $" + entry.getValue());
            sectorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

            JButton allocateButton = new JButton("Allocate");
            allocateButton.setBackground(new Color(33, 150, 243));
            allocateButton.setForeground(Color.WHITE);
            allocateButton.addActionListener(e -> allocateBudget(entry.getKey()));

            allocateButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    allocateButton.setBackground(new Color(29, 255, 236));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    allocateButton.setBackground(new Color(33, 150, 243));
                }
            });

            sectorPanel.add(sectorLabel, BorderLayout.CENTER);
            sectorPanel.add(allocateButton, BorderLayout.EAST);
            expensesPanel.add(sectorPanel);
        }

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(expensesPanel), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(90, 60, 150));

        panel.add(titleLabel, BorderLayout.NORTH);

        return panel;
    }

    private void refreshAnalysisPanel() {
        mainPanel.remove(analysisPanel);
        analysisPanel = createAnalysisPanel();
        mainPanel.add(analysisPanel, "Analysis");
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createAnalysisPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Analysis", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(90, 60, 150));

        JPanel analysisContentPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        analysisContentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        analysisContentPanel.setBackground(new Color(230, 240, 255));

        JPanel allocatedPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        allocatedPanel.setBorder(BorderFactory.createTitledBorder("Allocated Sectors"));
        allocatedPanel.setBackground(Color.decode("#e0ffe0"));

        JPanel nonAllocatedPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        nonAllocatedPanel.setBorder(BorderFactory.createTitledBorder("Non-Allocated Sectors"));
        nonAllocatedPanel.setBackground(Color.decode("#ffe0e0"));

        for (Map.Entry<String, BigDecimal> entry : sectors.entrySet()) {
            JLabel sectorLabel = new JLabel(entry.getKey() + ": $" + entry.getValue());
            sectorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

            if (entry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                sectorLabel.setForeground(Color.decode("#006400"));
                allocatedPanel.add(sectorLabel);
            } else {
                sectorLabel.setForeground(Color.decode("#8b0000"));
                nonAllocatedPanel.add(sectorLabel);
            }
        }

        analysisContentPanel.add(allocatedPanel);
        analysisContentPanel.add(nonAllocatedPanel);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(analysisContentPanel), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(90, 60, 150));

        JTextArea profileArea = new JTextArea("Profile content goes here...");
        profileArea.setEditable(false);
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(profileArea), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(5, 1, 5, 5)); // Add spacing for alignment
        sidebar.setBackground(new Color(40, 44, 52));
        sidebar.setPreferredSize(new Dimension(150, getHeight()));
    
        String[] buttonLabels = {"Dashboard", "My Expenses", "Analysis", "Profile", "Settings"};
        String[] iconPaths = {"dashboard.png", "budget.png", "analysis.png", "user1.png", "settings.png"};
    
        for (int i = 0; i < buttonLabels.length; i++) {
            final String label = buttonLabels[i];
            ImageIcon icon = new ImageIcon(iconPaths[i]);
            JButton button = new JButton(label, icon);
            
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setBackground(new Color(40, 44, 52));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5)); // Adjust padding
    
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(29, 255, 236));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(40, 44, 52));
                }
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    cardLayout.show(mainPanel, label);
                }
            });
    
            sidebar.add(button);
        }
    
        return sidebar;
    }
    
    private void setTotalBudget() {
        String input = JOptionPane.showInputDialog(this, "Enter total budget:", totalBudget);
        try {
            totalBudget = new BigDecimal(input);
            totalBudgetLabel.setText("Total Budget: $" + totalBudget);
            remainingBudgetLabel.setText("Remaining Budget: $" + calculateRemainingBudget());
            refreshAnalysisPanel(); // Refresh Analysis panel after setting the total budget
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid budget amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void allocateBudget(String sector) {
        String input = JOptionPane.showInputDialog(this, "Allocate budget to " + sector + ":", sectors.get(sector));
        try {
            BigDecimal allocation = new BigDecimal(input);
            sectors.put(sector, allocation);
            remainingBudgetLabel.setText("Remaining Budget: $" + calculateRemainingBudget());
            refreshAnalysisPanel(); // Refresh Analysis panel after budget allocation
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid allocation amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private BigDecimal calculateRemainingBudget() {
        BigDecimal allocatedSum = sectors.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalBudget.subtract(allocatedSum);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BudgetTrackerGUI::new);
    }
}
