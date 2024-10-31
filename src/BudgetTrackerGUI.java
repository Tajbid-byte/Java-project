
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BudgetTrackerGUI extends JFrame {
    private Map<String, BigDecimal> sectors = new LinkedHashMap<>();
    private BigDecimal totalBudget;
    private JTextField totalBudgetField;
    private Stack<Map<String, BigDecimal>> historyStack = new Stack<>();
    private JComboBox<String> themeDropdown;

    public BudgetTrackerGUI() {
        this(BigDecimal.ZERO);
    }

    public BudgetTrackerGUI(BigDecimal initialBudget) {
        this.totalBudget = initialBudget;
        initializeSectors();

        setTitle("Budget Tracker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main panel setup with light purple theme
        JPanel mainPanel = new JPanel(new GridLayout(8, 2));
        mainPanel.setBackground(new Color(240, 230, 255)); // Light purple

        JLabel title = new JLabel("Budget Tracker", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(100, 60, 120)); // Darker purple text

        // Total Budget
        JLabel totalBudgetLabel = new JLabel("Total Budget: ");
        totalBudgetField = new JTextField(totalBudget.toString());
        totalBudgetField.setEditable(false);
        JButton setBudgetButton = new JButton("Set Budget");
        mainPanel.add(totalBudgetLabel);
        mainPanel.add(totalBudgetField);
        mainPanel.add(new JLabel());
        mainPanel.add(setBudgetButton);

        // Theme Selection
        JLabel themeLabel = new JLabel("Select Theme: ");
        themeDropdown = new JComboBox<>(new String[]{"Light", "Dark"});
        themeDropdown.addActionListener(e -> changeTheme(mainPanel));
        mainPanel.add(themeLabel);
        mainPanel.add(themeDropdown);

        // Sector Allocation
        JLabel sectorLabel = new JLabel("Select Sector:");
        JComboBox<String> sectorDropdown = new JComboBox<>(sectors.keySet().toArray(new String[0]));
        mainPanel.add(sectorLabel);
        mainPanel.add(sectorDropdown);

        JLabel allocationLabel = new JLabel("Allocation Amount: ");
        JTextField allocationField = new JTextField();
        mainPanel.add(allocationLabel);
        mainPanel.add(allocationField);

        JButton allocateButton = new JButton("Allocate Budget");
        allocateButton.setBackground(new Color(170, 140, 200)); // Muted purple for button
        allocateButton.setForeground(Color.WHITE);
        allocateButton.setFocusPainted(false);
        mainPanel.add(allocateButton);

        JButton undoButton = new JButton("Undo Last Allocation");
        mainPanel.add(undoButton);

        JButton exportButton = new JButton("Export to CSV");
        mainPanel.add(exportButton);

        JTextArea displayArea = new JTextArea(15, 40);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        add(title, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Set Budget Button
        setBudgetButton.addActionListener(e -> setNewBudget());

        // Allocation Button
        allocateButton.addActionListener(e -> allocateBudget(sectorDropdown, allocationField, displayArea));

        // Undo Button
        undoButton.addActionListener(e -> undoLastAllocation(displayArea));

        // Export to CSV Button
        exportButton.addActionListener(e -> exportDataToCSV());
    }

    private void initializeSectors() {
        sectors.put("1. Education", BigDecimal.ZERO);
        sectors.put("2. Agriculture", BigDecimal.ZERO);
        sectors.put("3. Local Government", BigDecimal.ZERO);
        sectors.put("4. Rural Development", BigDecimal.ZERO);
        sectors.put("5. Electricity and Fuel", BigDecimal.ZERO);
        sectors.put("6. Health", BigDecimal.ZERO);
        sectors.put("7. Defense", BigDecimal.ZERO);
        sectors.put("8. Public Administration", BigDecimal.ZERO);
        sectors.put("9. Transportation and Communication", BigDecimal.ZERO);
        sectors.put("10. Social Safety", BigDecimal.ZERO);
        sectors.put("11. Upcoming Mega Project", BigDecimal.ZERO);
    }

    private void setNewBudget() {
        String budgetInput = JOptionPane.showInputDialog(this, "Enter new total budget:", "Set Budget", JOptionPane.PLAIN_MESSAGE);
        if (budgetInput != null) {
            try {
                BigDecimal newBudget = new BigDecimal(budgetInput);
                if (newBudget.signum() > 0) {
                    totalBudget = newBudget;
                    totalBudgetField.setText(totalBudget.toString());
                } else {
                    JOptionPane.showMessageDialog(this, "Budget must be positive.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void allocateBudget(JComboBox<String> sectorDropdown, JTextField allocationField, JTextArea displayArea) {
        String selectedSector = (String) sectorDropdown.getSelectedItem();
        BigDecimal allocationAmount;
        try {
            allocationAmount = new BigDecimal(allocationField.getText());
            if (allocationAmount.signum() <= 0) throw new NumberFormatException("Amount must be positive");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid allocation amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (allocationAmount.compareTo(totalBudget) <= 0) {
            historyStack.push(new LinkedHashMap<>(sectors)); // Save state before allocation
            sectors.put(selectedSector, sectors.get(selectedSector).add(allocationAmount));
            totalBudget = totalBudget.subtract(allocationAmount);
            totalBudgetField.setText(totalBudget.toString());
            displayArea.append("Allocated " + allocationAmount + " to " + selectedSector + "\n");

            BigDecimal lowBudgetThreshold = totalBudget.multiply(BigDecimal.valueOf(0.1));
            if (totalBudget.compareTo(lowBudgetThreshold) <= 0) {
                JOptionPane.showMessageDialog(null, "Warning: Budget is below 10% threshold!", "Low Budget", JOptionPane.WARNING_MESSAGE);
            }

            allocationField.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "Insufficient budget!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void undoLastAllocation(JTextArea displayArea) {
        if (!historyStack.isEmpty()) {
            sectors = historyStack.pop();
            updateDisplay(displayArea);
            JOptionPane.showMessageDialog(null, "Last allocation undone.", "Undo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No more actions to undo.", "Undo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateDisplay(JTextArea displayArea) {
        displayArea.setText("");
        sectors.forEach((sector, amount) -> displayArea.append(sector + ": " + amount + "\n"));
    }

    private void exportDataToCSV() {
        try {
            StringBuilder data = new StringBuilder("Sector,Allocation\n");
            for (Map.Entry<String, BigDecimal> entry : sectors.entrySet()) {
                data.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
            }
            Files.write(Paths.get("budget_data.csv"), data.toString().getBytes());
            JOptionPane.showMessageDialog(null, "Data exported to CSV successfully.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changeTheme(JPanel mainPanel) {
        if (themeDropdown.getSelectedItem().equals("Dark")) {
            mainPanel.setBackground(new Color(60, 60, 60));
        } else {
            mainPanel.setBackground(new Color(240, 230, 255)); // Light purple
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BudgetTrackerGUI(new BigDecimal(10000)).setVisible(true));
    }
}
