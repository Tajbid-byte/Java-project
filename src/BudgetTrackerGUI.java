import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.event.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class BudgetTrackerGUI extends JFrame {
    // ... (previous existing code remains the same)
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Map<String, BigDecimal> sectors;
    private BigDecimal totalBudget;
    private JLabel totalBudgetLabel;
    private JLabel remainingBudgetLabel;
    private JPanel analysisPanel; // Store reference to Analysis panel for refreshing
    private JPanel sidebar; // Sidebar panel to be added later
    private static final Color PRIMARY_COLOR = new Color(63, 81, 181);    // Material Indigo
    private static final Color ACCENT_COLOR = new Color(3, 169, 244);     // Material Light Blue
    private static final Color HOVER_COLOR = new Color(83, 109, 254);     // Lighter Indigo
    private static final Color BACKGROUND_COLOR = new Color(237, 241, 247);
    private static final Color CARD_COLOR = new Color(255, 255, 255);
    
    
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

        mainPanel.add(createIntroScreen(), "Intro");
        mainPanel.add(createOverviewPanel(), "Dashboard");
        mainPanel.add(createMyExpensesPanel(), "My Expenses");
        mainPanel.add(createSettingsPanel(), "Settings");
        analysisPanel = createAnalysisPanel(); // Initialize analysisPanel here
        mainPanel.add(analysisPanel, "Analysis");
        mainPanel.add(createProfilePanel(), "Profile");

        // Initially, add only the main panel without the sidebar
        add(mainPanel, BorderLayout.CENTER);

        // Create sidebar but don't add it yet
        sidebar = createSidebar();

        // Show the intro screen initially
        cardLayout.show(mainPanel, "Intro");

        setVisible(true);
    }
    // Add this public method to calculate remaining budget
    public BigDecimal calculateRemainingBudget() {
        BigDecimal allocatedBudget = BigDecimal.ZERO;
        for (BigDecimal amount : sectors.values()) {
            allocatedBudget = allocatedBudget.add(amount);
        }
        return totalBudget.subtract(allocatedBudget);
    }

    // Add a method to get detailed budget information
    public Map<String, Object> getBudgetInformation() {
        Map<String, Object> budgetInfo = new LinkedHashMap<>();
        budgetInfo.put("Total Budget", totalBudget);
        budgetInfo.put("Remaining Budget", calculateRemainingBudget());
        budgetInfo.put("Sector Allocations", new LinkedHashMap<>(sectors));
        return budgetInfo;
    }

    // ... (rest of the previous code remains the same)

    private JPanel createIntroScreen() {
        // ... (existing implementation)
        JPanel introPanel = new JPanel();
        introPanel.setLayout(new BorderLayout());
        introPanel.setBackground(new Color(220, 240, 250));
    
        // Load the image and resize it appropriately
        ImageIcon imageIcon = new ImageIcon("icon.png"); // Provide the path to your image
        Image image = imageIcon.getImage(); // Convert to Image object
    
        // Resize the image to a more reasonable size (e.g., 600x600)
        Image resizedImage = image.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(resizedImage); // Re-create the ImageIcon with resized image
    
        // Create a label with the round image
        JLabel imageLabel = new JLabel(new ImageIcon(createRoundImage(resizedImage, 600, 600)));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the image
    
        // Set a preferred size for the JLabel to ensure it gets the correct size
        imageLabel.setPreferredSize(new Dimension(600, 600)); // Set the size of the image label
    
        // Description label
        JLabel descriptionLabel = new JLabel("<html><div style='text-align: center;'>Save your money with Expense Tracker</div></html>", SwingConstants.CENTER);
        descriptionLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));
        descriptionLabel.setForeground(new Color(0, 0, 0));
    
        // Add the image and description to the intro panel
        introPanel.add(imageLabel, BorderLayout.NORTH); // Add image at the top
        introPanel.add(descriptionLabel, BorderLayout.CENTER); // Add description below the image
    
        // Create rounded button with hover effect
        JButton startButton = new JButton("Let's Start") {
            private boolean hover = false;
    
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
                // Set color based on hover state
                if (hover) {
                    g2.setColor(new Color(29, 255, 36)); // Hover color
                } else {
                    g2.setColor(new Color(127, 0, 255)); // Default color
                }
    
                // Draw rounded rectangle background
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Rounded corners
                g2.dispose();
    
                super.paintComponent(g);
            }
        };
    
        startButton.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Font size
        startButton.setForeground(Color.WHITE); // Text color
        startButton.setFocusPainted(false);
        startButton.setContentAreaFilled(false); // Remove default background
        startButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding for rounder look
    
        // Add hover effect to button
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setPreferredSize(new Dimension(130, 40)); // Increase size slightly on hover
                startButton.setFont(new Font("Segoe UI", Font.PLAIN, 15)); // Increase font slightly on hover
                startButton.setForeground(new Color(29, 255, 36)); // Change text color on hover
                startButton.revalidate();
                startButton.repaint();
            }
    
            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setPreferredSize(new Dimension(120, 35)); // Return to original size
                startButton.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Return to original font size
                startButton.setForeground(Color.WHITE); // Return text color to white
                startButton.revalidate();
                startButton.repaint();
            }
        });
    
        // Action listener for button
        startButton.addActionListener(e -> {
            add(sidebar, BorderLayout.WEST);
            cardLayout.show(mainPanel, "Dashboard");
            revalidate();
            repaint();
        });
    
        // Center the button within a FlowLayout to control its size
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(220, 240, 250)); // Match background color
        buttonPanel.add(startButton); // Add the button to the flow layout panel
    
        introPanel.add(buttonPanel, BorderLayout.SOUTH); // Add the button panel to the intro panel
    
        return introPanel;
    }
    
    // Helper method to create a round image
    private Image createRoundImage(Image src, int width, int height) {
        BufferedImage buffered = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffered.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new Ellipse2D.Float(0, 0, width, height));
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return buffered;
    }

    private JPanel createOverviewPanel() {
        // ... (existing implementation)
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
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout(0, 20));
    mainPanel.setBackground(BACKGROUND_COLOR);
    mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

    // Create title panel
    JPanel titlePanel = new JPanel(new BorderLayout());
    titlePanel.setBackground(CARD_COLOR);
    titlePanel.setBorder(BorderFactory.createCompoundBorder(
        new RoundedBorder(15),
        new EmptyBorder(15, 20, 15, 20)
    ));

    JLabel titleLabel = new JLabel("My Expenses", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
    titleLabel.setForeground(PRIMARY_COLOR);
    titlePanel.add(titleLabel, BorderLayout.CENTER);

    // Create content panel for expenses
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.setBackground(BACKGROUND_COLOR);

    // Add expense cards
    for (Map.Entry<String, BigDecimal> entry : sectors.entrySet()) {
        contentPanel.add(createExpenseCard(entry));
        contentPanel.add(Box.createVerticalStrut(10)); // Space between cards
    }

    // Custom scroll pane
    JScrollPane scrollPane = new JScrollPane(contentPanel);
    scrollPane.setBorder(null);
    scrollPane.setBackground(BACKGROUND_COLOR);
    scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
    
    // Customize scrollbar
    JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
    verticalScrollBar.setPreferredSize(new Dimension(8, 0));
    verticalScrollBar.setUI(new ModernScrollBarUI());

    mainPanel.add(titlePanel, BorderLayout.NORTH);
    mainPanel.add(scrollPane, BorderLayout.CENTER);

    return mainPanel;
}

// Helper method to create expense cards
private JPanel createExpenseCard(Map.Entry<String, BigDecimal> entry) {
    JPanel card = new JPanel(new BorderLayout(15, 0));
    card.setBackground(CARD_COLOR);
    card.setBorder(BorderFactory.createCompoundBorder(
        new RoundedBorder(10),
        new EmptyBorder(12, 15, 12, 15)
    ));
    card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

    // Create icon panel
    JLabel iconLabel = new JLabel("\uD83D\uDCB0"); // Money bag emoji
    iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
    card.add(iconLabel, BorderLayout.WEST);

    // Create info panel
    JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 3));
    infoPanel.setBackground(CARD_COLOR);

    JLabel sectorLabel = new JLabel(entry.getKey());
    sectorLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
    sectorLabel.setForeground(new Color(33, 33, 33));

    JLabel amountLabel = new JLabel("$" + entry.getValue());
    amountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    amountLabel.setForeground(new Color(117, 117, 117));

    infoPanel.add(sectorLabel);
    infoPanel.add(amountLabel);
    card.add(infoPanel, BorderLayout.CENTER);

    // Create allocate button
    JButton allocateButton = createStyledButton("Allocate");
    allocateButton.addActionListener(e -> allocateBudget(entry.getKey()));
    card.add(allocateButton, BorderLayout.EAST);

    return card;
}

// Helper method to create styled buttons
private JButton createStyledButton(String text) {
    JButton button = new JButton(text) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isPressed()) {
                g2.setColor(HOVER_COLOR.darker());
            } else if (getModel().isRollover()) {
                g2.setColor(HOVER_COLOR);
            } else {
                g2.setColor(ACCENT_COLOR);
            }
            
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();
            
            super.paintComponent(g);
        }
    };

    button.setFont(new Font("Segoe UI", Font.BOLD, 14));
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setContentAreaFilled(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.setPreferredSize(new Dimension(100, 35));

    return button;
}

// Custom rounded border class
private static class RoundedBorder extends LineBorder {
    private final int radius;

    RoundedBorder(int radius) {
        super(new Color(230, 230, 230));
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(lineColor);
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2.dispose();
    }
}

// Modern ScrollBar UI
private static class ModernScrollBarUI extends BasicScrollBarUI {
    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = ACCENT_COLOR;
        this.trackColor = BACKGROUND_COLOR;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(thumbColor);
        g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 4, 4);
        g2.dispose();
    }
}

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(249, 250, 251));
    
        // Title Panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("App Settings", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(17, 24, 39));
        titlePanel.add(titleLabel);
    
        // Settings Categories Panel
        JPanel settingsCategoriesPanel = new JPanel();
        settingsCategoriesPanel.setLayout(new BoxLayout(settingsCategoriesPanel, BoxLayout.Y_AXIS));
        settingsCategoriesPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        settingsCategoriesPanel.setBackground(new Color(249, 250, 251));
    
        // Create individual setting sections
        settingsCategoriesPanel.add(createSettingsSection("Budget Preferences", 
            new String[]{"Default Budget Allocation", "Spending Alerts", "Budget Rollover"}));
        settingsCategoriesPanel.add(Box.createVerticalStrut(10));
        settingsCategoriesPanel.add(createSettingsSection("Appearance", 
            new String[]{"Theme Selection", "Color Scheme", "Font Size"}));
        settingsCategoriesPanel.add(Box.createVerticalStrut(10));
        settingsCategoriesPanel.add(createSettingsSection("Privacy & Security", 
            new String[]{"Data Export Settings", "Backup & Restore", "Reset Application"}));
        settingsCategoriesPanel.add(Box.createVerticalStrut(10));
        settingsCategoriesPanel.add(createSettingsSection("Notifications", 
            new String[]{"Budget Alerts", "Expense Reminders", "Monthly Summary"}));
    
        // Action Buttons Panel
        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionButtonsPanel.setOpaque(false);
    
        JButton saveButton = createStyledButton("Save Settings", Color.WHITE, new Color(79, 70, 229));
        JButton resetButton = createStyledButton("Reset to Default", Color.WHITE, new Color(220, 38, 38));
    
        saveButton.addActionListener(e -> saveApplicationSettings());
        resetButton.addActionListener(e -> resetToDefaultSettings());
    
        actionButtonsPanel.add(saveButton);
        actionButtonsPanel.add(resetButton);
    
        // Assemble the entire panel
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(settingsCategoriesPanel), BorderLayout.CENTER);
        panel.add(actionButtonsPanel, BorderLayout.SOUTH);
    
        return panel;
    }
    
    private JPanel createSettingsSection(String sectionTitle, String[] settings) {
        JPanel sectionPanel = new JPanel(new BorderLayout(10, 10));
        sectionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        sectionPanel.setBackground(Color.WHITE);
    
        // Section Title
        JLabel sectionTitleLabel = new JLabel(sectionTitle);
        sectionTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitleLabel.setForeground(new Color(17, 24, 39));
        sectionPanel.add(sectionTitleLabel, BorderLayout.NORTH);
    
        // Settings List
        JPanel settingsListPanel = new JPanel();
        settingsListPanel.setLayout(new BoxLayout(settingsListPanel, BoxLayout.Y_AXIS));
        settingsListPanel.setBackground(Color.WHITE);
    
        for (String setting : settings) {
            JPanel settingRowPanel = new JPanel(new BorderLayout());
            settingRowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            settingRowPanel.setBackground(Color.WHITE);
    
            JLabel settingLabel = new JLabel(setting);
            settingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    
            JToggleButton toggleButton = new JToggleButton("Off");
            toggleButton.setPreferredSize(new Dimension(70, 30));
            toggleButton.setBackground(Color.LIGHT_GRAY);
            toggleButton.setForeground(Color.WHITE);
    
            toggleButton.addActionListener(e -> {
                if (toggleButton.isSelected()) {
                    toggleButton.setText("On");
                    toggleButton.setBackground(new Color(79, 70, 229));
                } else {
                    toggleButton.setText("Off");
                    toggleButton.setBackground(Color.LIGHT_GRAY);
                }
            });
    
            settingRowPanel.add(settingLabel, BorderLayout.WEST);
            settingRowPanel.add(toggleButton, BorderLayout.EAST);
    
            settingsListPanel.add(settingRowPanel);
            settingsListPanel.add(Box.createVerticalStrut(10));
        }
    
        sectionPanel.add(settingsListPanel, BorderLayout.CENTER);
    
        return sectionPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isPressed()) {
                    g.setColor(getBackground().darker());
                } else {
                    g.setColor(getBackground());
                }
                g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
                super.paintComponent(g);
            }
        };
    
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(29, 255, 236)); // Hover color
            }
    
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor); // Original color
            }
        });
    
        return button;
    }
    
    private void saveApplicationSettings() {
        JOptionPane.showMessageDialog(this, 
            "Settings saved successfully!", 
            "Save Settings", 
            JOptionPane.INFORMATION_MESSAGE);
        // Placeholder for actual settings saving logic
    }
    
    private void resetToDefaultSettings() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to reset all settings to default?", 
            "Reset Settings", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, 
                "All settings have been reset to default.", 
                "Reset Complete", 
                JOptionPane.INFORMATION_MESSAGE);
            // Placeholder for actual reset logic
        }
    }

    private void refreshAnalysisPanel() {
        mainPanel.remove(analysisPanel);
        analysisPanel = createAnalysisPanel();
        mainPanel.add(analysisPanel, "Analysis");
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createAnalysisPanel() {
        // ... (existing implementation)
        JPanel panel = new JPanel(new BorderLayout());
    
        JLabel titleLabel = new JLabel("Analysis", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(90, 60, 150));
    
        // Create a container with a GridLayout for equal-width columns
        JPanel sectorPanelsContainer = new JPanel(new GridLayout(1, 2, 10, 0));
        sectorPanelsContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        sectorPanelsContainer.setBackground(new Color(230, 240, 255));
    
        // Allocated panel on the left
        JPanel allocatedPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        allocatedPanel.setBorder(BorderFactory.createTitledBorder("Allocated Sectors"));
        allocatedPanel.setBackground(Color.decode("#e0ffe0"));
    
        // Non-allocated panel on the right
        JPanel nonAllocatedPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        nonAllocatedPanel.setBorder(BorderFactory.createTitledBorder("Non-Allocated Sectors"));
        nonAllocatedPanel.setBackground(Color.decode("#ffe0e0"));
    
        // Populate allocated and non-allocated panels with labels
        for (Map.Entry<String, BigDecimal> entry : sectors.entrySet()) {
            JLabel sectorLabel = new JLabel(entry.getKey() + ": $" + entry.getValue());
            sectorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    
            if (entry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                allocatedPanel.add(sectorLabel);
            } else {
                nonAllocatedPanel.add(sectorLabel);
            }
        }
    
        // Add both panels to the container for equal widths
        sectorPanelsContainer.add(allocatedPanel);
        sectorPanelsContainer.add(nonAllocatedPanel);
    
        // Centered visualization button below the panels
        JPanel visualizationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton visualizationButton = new JButton("Visualization");
        visualizationButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        visualizationButton.setBackground(new Color(33, 150, 243));
        visualizationButton.setForeground(Color.WHITE);
        visualizationButton.setFocusPainted(false);
        visualizationButton.setPreferredSize(new Dimension(150, 50)); // Adjust size as needed
    
        // Add hover effect to the visualization button
        Color originalColor = visualizationButton.getBackground();
        visualizationButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                visualizationButton.setBackground(new Color(29, 255, 236)); // Hover color
            }
    
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                visualizationButton.setBackground(originalColor); // Revert to original color
            }
        });
    
        // Action listener for visualization button
        visualizationButton.addActionListener(e -> {
            showProgressBar();
        });
    
        visualizationPanel.add(visualizationButton);
    
        // Add components to the main analysis panel
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(sectorPanelsContainer, BorderLayout.CENTER);
        panel.add(visualizationPanel, BorderLayout.SOUTH);
    
        return panel;
    }

    private void showProgressBar() {
        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
        progressPanel.setBackground(Color.WHITE);

        // Calculate total and allocated budget
        BigDecimal allocatedBudget = BigDecimal.ZERO;
        for (BigDecimal amount : sectors.values()) {
            allocatedBudget = allocatedBudget.add(amount);
        }

        // Calculate remaining budget
        BigDecimal remainingBudget = totalBudget.subtract(allocatedBudget);

        // Loop over sectors to create progress bars
        for (Map.Entry<String, BigDecimal> entry : sectors.entrySet()) {
            if (entry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                // Calculate percentage for each sector based on total budget
                int percentage = entry.getValue().multiply(BigDecimal.valueOf(100))
                        .divide(totalBudget, 0, BigDecimal.ROUND_HALF_UP).intValue();

                // Create a custom progress bar for each allocated sector
                JProgressBar sectorProgressBar = new JProgressBar(0, 100);
                sectorProgressBar.setValue(percentage);
                sectorProgressBar.setStringPainted(true);

                // Modern custom design (rounded corners, gradient color)
                sectorProgressBar.setOpaque(false);
                sectorProgressBar.setForeground(new Color(33, 150, 243));
                sectorProgressBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                sectorProgressBar.setUI(new BasicProgressBarUI() {
                    @Override
                    protected Color getSelectionBackground() {
                        return new Color(33, 150, 243);
                    }

                    @Override
                    public void paint(Graphics g, JComponent c) {
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                        JProgressBar progressBar = (JProgressBar) c;
                        int value = progressBar.getValue();
                        int max = progressBar.getMaximum();
                        int percentage = (int) ((double) value / max * 100);

                        GradientPaint gradient = new GradientPaint(0, 0, new Color(33, 150, 243), 0, c.getHeight(), new Color(29, 255, 236));
                        g2d.setPaint(gradient);
                        g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 15, 15);

                        g2d.setColor(new Color(33, 150, 243));
                        g2d.fillRoundRect(0, 0, (int) (c.getWidth() * ((double) value / max)), c.getHeight(), 15, 15);

                        g2d.setColor(Color.WHITE);
                        Font percentageFont = new Font("Segoe UI", Font.BOLD, 24);
                        g2d.setFont(percentageFont);

                        String text = percentage + "%";
                        FontMetrics metrics = g2d.getFontMetrics();
                        int textWidth = metrics.stringWidth(text);
                        int textHeight = metrics.getAscent();

                        int availableWidth = (int) (c.getWidth() * ((double) value / max));
                        if (textWidth > availableWidth) {
                            int newFontSize = Math.max(20, (int) (availableWidth * 0.8));
                            g2d.setFont(new Font("Segoe UI", Font.BOLD, newFontSize));
                            metrics = g2d.getFontMetrics();
                            textWidth = metrics.stringWidth(text);
                            textHeight = metrics.getAscent();
                        }

                        int x = (availableWidth - textWidth) / 2;
                        int y = (c.getHeight() + textHeight) / 2;
                        g2d.drawString(text, x, y);
                        super.paint(g, c);
                    }
                });

                JPanel sectorPanel = new JPanel(new BorderLayout());
                sectorPanel.setOpaque(false);
                sectorPanel.add(new JLabel(entry.getKey() + ": $" + entry.getValue()), BorderLayout.NORTH);
                sectorPanel.add(sectorProgressBar, BorderLayout.CENTER);
                progressPanel.add(sectorPanel);
            }
        }

        JLabel progressLabel = new JLabel(String.format("Allocated: $%.2f / Remaining: $%.2f", allocatedBudget, remainingBudget));
        progressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        progressPanel.add(progressLabel);

        mainPanel.add(progressPanel, "Progress");
        cardLayout.show(mainPanel, "Progress");
        revalidate();
        repaint();
    }

    private void animateProgressBar(JProgressBar progressBar, int targetValue) {
        final int step = 2;
        final int delay = 50;

        Timer timer = new Timer(delay, new ActionListener() {
            int currentValue = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentValue < targetValue) {
                    currentValue += step;
                    progressBar.setValue(currentValue);
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.start();
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
    public Map<String, BigDecimal> getSectors() {
        return this.sectors;
    }
  
    private JPanel createProfilePanel() {
        return new ProfileSettingsPanel(this);
    }
    private JPanel createSidebar() {
        // ... (existing implementation)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(5, 1, 5, 5)); // Add spacing for alignment
        sidebar.setBackground(new Color(40, 44, 52));
        sidebar.setPreferredSize(new Dimension(150, getHeight()));  // Set the preferred size
    
        // Define button labels and corresponding icon paths
        String[] buttonLabels = {"Dashboard", "My Expenses", "Analysis", "Profile", "Settings"};
        String[] iconPaths = {"dashboard.png", "budget.png", "analysis.png", "user1.png", "settings.png"};
    
        for (int i = 0; i < buttonLabels.length; i++) {
            final String label = buttonLabels[i];
            ImageIcon icon = new ImageIcon(iconPaths[i]); // Load icon for each button
            JButton button = new JButton(label, icon);
            
            // Set the button's layout and appearance
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setBackground(new Color(40, 44, 52));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5)); // Adjust padding
    
            // Change button background color when hovered over
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(29, 255, 236));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(40, 44, 52));
                }
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    cardLayout.show(mainPanel, label); // Switch to the panel corresponding to the button clicked
                }
            });
    
            sidebar.add(button); // Add the button to the sidebar
        }
    
        return sidebar;
    }

    private void setTotalBudget() {
        // ... (existing implementation)
        String input = JOptionPane.showInputDialog(this, "Enter Total Budget:");
        try {
            totalBudget = new BigDecimal(input);
            totalBudgetLabel.setText("Total Budget: " + totalBudget);
            remainingBudgetLabel.setText("Remaining Budget: " + calculateRemainingBudget());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
        }
    }
    public BigDecimal getTotalBudget() {
        return this.totalBudget;
    }
  private void allocateBudget(String sector) {
    if (sector.equals("Education")) {
        showEducationSubsectorAllocation();
    } else {
        // Existing general allocation logic
        String input = JOptionPane.showInputDialog(this, "Enter amount to allocate to " + sector + ":");
        try {
            BigDecimal allocation = new BigDecimal(input);
            BigDecimal allocatedBudget = calculateAllocatedBudget();
            BigDecimal potentialTotal = allocatedBudget.add(allocation);

            if (potentialTotal.compareTo(totalBudget) > 0) {
                JOptionPane.showMessageDialog(this, "Allocation exceeds total budget limit.");
            } else {
                sectors.put(sector, allocation);
                refreshAnalysisPanel();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
        }
    }
}

private void showEducationSubsectorAllocation() {
    JDialog dialog = new JDialog(this, "Education Subsector Allocation", true);
    dialog.setLayout(new BorderLayout());
    dialog.setSize(500, 400);

    // Education Subsectors
    String[] subsectors = {
        "Primary and Mass Education", 
        "Secondary and Higher Education", 
        "Technical and Madrasa Education"
    };

    JPanel subsectorPanel = new JPanel(new GridLayout(subsectors.length, 2, 10, 10));
    subsectorPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    Map<String, JTextField> subsectorAllocationFields = new HashMap<>();

    for (String subsector : subsectors) {
        JLabel label = new JLabel(subsector);
        JTextField allocationField = new JTextField(10);
        subsectorPanel.add(label);
        subsectorPanel.add(allocationField);
        subsectorAllocationFields.put(subsector, allocationField);
    }

    JButton detailButton = new JButton("Detailed Allocation");
    detailButton.addActionListener(e -> {
        String selectedSubsector = (String) JOptionPane.showInputDialog(
            dialog, 
            "Select Subsector for Detailed Allocation", 
            "Detailed Allocation", 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            subsectors, 
            subsectors[0]
        );

        if (selectedSubsector != null) {
            showDetailedEducationAllocation(selectedSubsector);
        }
    });

    JButton allocateButton = new JButton("Allocate");
    allocateButton.addActionListener(e -> {
        try {
            BigDecimal totalEducationAllocation = BigDecimal.ZERO;
            for (JTextField field : subsectorAllocationFields.values()) {
                BigDecimal allocation = new BigDecimal(field.getText());
                totalEducationAllocation = totalEducationAllocation.add(allocation);
            }

            BigDecimal allocatedBudget = calculateAllocatedBudget();
            BigDecimal potentialTotal = allocatedBudget.add(totalEducationAllocation);

            if (potentialTotal.compareTo(totalBudget) > 0) {
                JOptionPane.showMessageDialog(dialog, "Allocation exceeds total budget limit.");
            } else {
                // Update sectors with subsector allocations
                sectors.put("Education - Primary and Mass Education", 
                    new BigDecimal(subsectorAllocationFields.get("Primary and Mass Education").getText()));
                sectors.put("Education - Secondary and Higher Education", 
                    new BigDecimal(subsectorAllocationFields.get("Secondary and Higher Education").getText()));
                sectors.put("Education - Technical and Madrasa Education", 
                    new BigDecimal(subsectorAllocationFields.get("Technical and Madrasa Education").getText()));

                refreshAnalysisPanel();
                dialog.dispose();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Invalid input. Please enter valid numbers.");
        }
    });

    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.add(detailButton);
    buttonPanel.add(allocateButton);

    dialog.add(subsectorPanel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
}

private void showDetailedEducationAllocation(String subsector) {
    JDialog detailDialog = new JDialog(this, subsector + " - Detailed Allocation", true);
    detailDialog.setLayout(new BorderLayout());
    detailDialog.setSize(500, 400);

    Map<String, BigDecimal> detailedAllocations = new HashMap<>();

    switch (subsector) {
        case "Technical and Madrasa Education":
            String[] technicalDetails = {
                "Training for Teachers",
                "Modernizing and Digitizing Education System",
                "Improving Educational Institution Aesthetics",
                "Advancing Technical and Madrasa Education",
                "Increasing Inclusion for Small Ethnic Groups and Third Gender Students"
            };
            detailDialog = createDetailAllocationPanel(subsector, technicalDetails, detailedAllocations);
            break;
        case "Primary and Mass Education":
            // Add specific details for this subsector
            String[] primaryDetails = { /* your details here */ };
            detailDialog = createDetailAllocationPanel(subsector, primaryDetails, detailedAllocations);
            break;
        case "Secondary and Higher Education":
            // Add specific details for this subsector
            String[] secondaryDetails = { /* your details here */ };
            detailDialog = createDetailAllocationPanel(subsector, secondaryDetails, detailedAllocations);
            break;
        default:
            JOptionPane.showMessageDialog(this, "Detailed allocation not supported for this subsector.");
            return;
    }

    detailDialog.setLocationRelativeTo(this);
    detailDialog.setVisible(true);
}

private JDialog createDetailAllocationPanel(String subsector, String[] details, Map<String, BigDecimal> detailedAllocations) {
    JDialog detailDialog = new JDialog(this, subsector + " - Detailed Allocation", true);
    detailDialog.setLayout(new BorderLayout());
    detailDialog.setSize(500, 400);

    JPanel detailPanel = new JPanel(new GridLayout(details.length, 2, 10, 10));
    detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    Map<String, JTextField> detailAllocationFields = new HashMap<>();

    for (String detail : details) {
        JLabel label = new JLabel(detail);
        JTextField allocationField = new JTextField(10);
        detailPanel.add(label);
        detailPanel.add(allocationField);
        detailAllocationFields.put(detail, allocationField);
    }

    JButton allocateButton = new JButton("Allocate Details");
    allocateButton.addActionListener(e -> {
        try {
            for (String detail : details) {
                detailedAllocations.put(detail, new BigDecimal(detailAllocationFields.get(detail).getText()));
            }
            detailDialog.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(detailDialog, "Invalid input. Please enter valid numbers.");
        }
    });

    detailDialog.add(detailPanel, BorderLayout.CENTER);
    detailDialog.add(allocateButton, BorderLayout.SOUTH);

    return detailDialog;
}

    private BigDecimal calculateAllocatedBudget() {
        // ... (existing implementation)
        BigDecimal allocatedBudget = BigDecimal.ZERO;
        for (BigDecimal amount : sectors.values()) {
            allocatedBudget = allocatedBudget.add(amount);  // Sum up all allocated amounts
        }
        return allocatedBudget;

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BudgetTrackerGUI());
    }

}

class ProfileSettingsPanel extends JPanel {
    private JTextField nameTextField;
    private JTextField emailTextField;
    private JTextField contactNumberTextField;
    private BudgetTrackerGUI parentFrame;

    public ProfileSettingsPanel(BudgetTrackerGUI parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(249, 250, 251));
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setOpaque(false);

        ImageIcon userIcon = new ImageIcon("profileicon.png");
        JLabel titleLabel = new JLabel("Profile Settings", userIcon, JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(17, 24, 39));

        JLabel subtitleLabel = new JLabel("Update your profile information");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(107, 114, 128));

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createHorizontalStrut(10));
        headerPanel.add(subtitleLabel);

        // Form fields
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 0, 16));
        formPanel.setOpaque(false);

        formPanel.add(createFormField("Full Name", "Enter your full name"));
        formPanel.add(createFormField("Email", "Enter your email address"));
        formPanel.add(createFormField("Contact Number", "Enter your contact number"));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton exportButton = createStyledButton("Export Data", Color.WHITE, new Color(79, 70, 229));
        exportButton.addActionListener(e -> exportDataToCSV());

        buttonPanel.add(exportButton);

        // Assemble the panel
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createFormField(String label, String placeholder) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        fieldPanel.setOpaque(false);

        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        fieldLabel.setForeground(new Color(17, 24, 39));
        fieldLabel.setPreferredSize(new Dimension(100, 40));

        JTextField textField = new JTextField(12);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        textField.setPreferredSize(new Dimension(200, 40));

        // Store reference to the text fields
        if (label.equals("Full Name")) {
            nameTextField = textField;
        } else if (label.equals("Email")) {
            emailTextField = textField;
        } else if (label.equals("Contact Number")) {
            contactNumberTextField = textField;
        }

        // Add placeholder behavior
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });

        fieldPanel.add(fieldLabel);
        fieldPanel.add(Box.createHorizontalStrut(16));
        fieldPanel.add(textField);

        return fieldPanel;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isPressed()) {
                    g.setColor(getBackground().darker());
                } else {
                    g.setColor(getBackground());
                }
                g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(29, 255, 236)); // Hover color
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor); // Original color
            }
        });

        return button;
    }


    private void exportDataToCSV() {
        // Validate input fields
        String fullName = nameTextField.getText();
        String email = emailTextField.getText();
        String contactNumber = contactNumberTextField.getText();

        if (fullName.equals("Enter your full name") || 
            email.equals("Enter your email address") || 
            contactNumber.equals("Enter your contact number")) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all fields before exporting!", 
                "Missing Data", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Fetch dynamic budget data from parent BudgetTrackerGUI
        Map<String, Object> budgetInfo = parentFrame.getBudgetInformation();

        // Prepare export
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV File");
        fileChooser.setSelectedFile(new File("budget_profile_data.csv"));
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try (FileWriter writer = new FileWriter(fileToSave)) {
                // Write headers
                writer.write("Profile Information\n");
                writer.write("Full Name," + fullName + "\n");
                writer.write("Email," + email + "\n");
                writer.write("Contact Number," + contactNumber + "\n\n");

                // Write budget summary
                writer.write("Budget Summary\n");
                writer.write("Total Budget," + budgetInfo.get("Total Budget") + "\n");
                writer.write("Remaining Budget," + budgetInfo.get("Remaining Budget") + "\n\n");

                // Write sector allocations
                writer.write("Sector Allocations\n");
                writer.write("Sector,Allocated Amount\n");
                
                @SuppressWarnings("unchecked")
                Map<String, BigDecimal> sectors = (Map<String, BigDecimal>) budgetInfo.get("Sector Allocations");
                for (Map.Entry<String, BigDecimal> entry : sectors.entrySet()) {
                    writer.write(entry.getKey() + "," + entry.getValue() + "\n");
                }

                JOptionPane.showMessageDialog(this, 
                    "Data exported successfully!", 
                    "Export", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error exporting data: " + e.getMessage(), 
                    "Export Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}