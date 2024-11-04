import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        setSize(400, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createOverviewPanel(), "Overview");
        mainPanel.add(createBudgetPanel(), "Budget Categories");
        mainPanel.add(createSettingsPanel(), "Settings");

        add(mainPanel, BorderLayout.CENTER);
        add(createNavigationPanel(), BorderLayout.SOUTH);

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
        budgetSummaryPanel.setBackground(Color.WHITE);

        totalBudgetLabel = new JLabel("Total Budget: " + totalBudget);
        totalBudgetLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        remainingBudgetLabel = new JLabel("Remaining Budget: " + calculateRemainingBudget());
        remainingBudgetLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        budgetSummaryPanel.add(totalBudgetLabel);
        budgetSummaryPanel.add(remainingBudgetLabel);

        JButton setBudgetButton = new JButton("Set Total Budget");
        setBudgetButton.setBackground(new Color(0, 150, 136));
        setBudgetButton.setForeground(Color.WHITE);
        setBudgetButton.addActionListener(e -> setTotalBudget());
        budgetSummaryPanel.add(setBudgetButton);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(budgetSummaryPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBudgetPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Budget Categories", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(90, 60, 150));

        JPanel categoryPanel = new JPanel(new GridLayout(sectors.size(), 1, 5, 5));
        categoryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        categoryPanel.setBackground(new Color(245, 245, 250));

        for (String sector : sectors.keySet()) {
            JPanel sectorPanel = new JPanel(new BorderLayout());
            sectorPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 250)));
            sectorPanel.setBackground(new Color(255, 255, 255));

            JLabel sectorLabel = new JLabel(sector);
            sectorLabel.setFont(new Font("Arial", Font.PLAIN, 16));

            JButton allocateButton = new JButton("Allocate");
            allocateButton.setBackground(new Color(33, 150, 243));
            allocateButton.setForeground(Color.WHITE);
            allocateButton.addActionListener(e -> allocateBudget(sector));

            sectorPanel.add(sectorLabel, BorderLayout.WEST);
            sectorPanel.add(allocateButton, BorderLayout.EAST);
            categoryPanel.add(sectorPanel);
        }

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(categoryPanel, BorderLayout.CENTER);

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

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel(new GridLayout(1, 3));
        navPanel.setBackground(new Color(220, 220, 235));

        JButton overviewButton = new JButton("Overview");
        JButton budgetButton = new JButton("Categories");
        JButton settingsButton = new JButton("Settings");

        overviewButton.addActionListener(e -> cardLayout.show(mainPanel, "Overview"));
        budgetButton.addActionListener(e -> cardLayout.show(mainPanel, "Budget Categories"));
        settingsButton.addActionListener(e -> cardLayout.show(mainPanel, "Settings"));

        navPanel.add(overviewButton);
        navPanel.add(budgetButton);
        navPanel.add(settingsButton);

        return navPanel;
    }

    private void setTotalBudget() {
        String input = JOptionPane.showInputDialog(this, "Enter total budget:");
        if (input != null && !input.trim().isEmpty()) {
            try {
                totalBudget = new BigDecimal(input);
                totalBudgetLabel.setText("Total Budget: " + totalBudget);
                remainingBudgetLabel.setText("Remaining Budget: " + calculateRemainingBudget());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
            }
        }
    }

    private void allocateBudget(String sector) {
        String input = JOptionPane.showInputDialog(this, "Allocate budget for " + sector + ":");
        if (input != null && !input.trim().isEmpty()) {
            try {
                BigDecimal allocation = new BigDecimal(input);
                if (allocation.compareTo(totalBudget.subtract(calculateAllocatedBudget())) <= 0) {
                    sectors.put(sector, allocation);
                    remainingBudgetLabel.setText("Remaining Budget: " + calculateRemainingBudget());
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient remaining budget for this allocation.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
            }
        }
    }

    private BigDecimal calculateRemainingBudget() {
        return totalBudget.subtract(calculateAllocatedBudget());
    }

    private BigDecimal calculateAllocatedBudget() {
        return sectors.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BudgetTrackerGUI::new);
    }
}