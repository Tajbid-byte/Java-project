import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.event.*;
import java.awt.*;
import java.math.BigDecimal;
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
        // ... (existing implementation)
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
        // ... (existing implementation)
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
        // ... (existing implementation)
        String input = JOptionPane.showInputDialog(this, "Enter amount to allocate to " + sector + ":");
        try {
            BigDecimal allocation = new BigDecimal(input);
            BigDecimal allocatedBudget = calculateAllocatedBudget();  // Calculate already allocated budget
            BigDecimal potentialTotal = allocatedBudget.add(allocation);  // Calculate the total if this allocation is added
    
            // Check if the allocation exceeds the total budget
            if (potentialTotal.compareTo(totalBudget) > 0) {
                JOptionPane.showMessageDialog(this, "Allocation exceeds total budget limit. Please allocate within the limit.");
            } else {
                sectors.put(sector, allocation);
                refreshAnalysisPanel();  // Refresh the panel to show the updated allocation
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
        }
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