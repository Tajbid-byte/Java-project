import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.geom.Ellipse2D;

public class BudgetTrackerGUI extends JFrame {
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

        mainPanel.add(createIntroScreen(), "Intro");  // Added intro screen
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
    private JPanel createIntroScreen() {
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
                allocatedPanel.add(sectorLabel);
            } else {
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

        panel.add(titleLabel, BorderLayout.NORTH);

        return panel;
    }
    private JPanel createSidebar() {
        // Create the sidebar and set up layout
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
        String input = JOptionPane.showInputDialog(this, "Enter Total Budget:");
        try {
            totalBudget = new BigDecimal(input);
            totalBudgetLabel.setText("Total Budget: " + totalBudget);
            remainingBudgetLabel.setText("Remaining Budget: " + calculateRemainingBudget());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
        }
    }

    private BigDecimal calculateRemainingBudget() {
        BigDecimal allocatedBudget = BigDecimal.ZERO;
        for (BigDecimal amount : sectors.values()) {
            allocatedBudget = allocatedBudget.add(amount);
        }
        return totalBudget.subtract(allocatedBudget);
    }

    private void allocateBudget(String sector) {
        String input = JOptionPane.showInputDialog(this, "Enter amount to allocate to " + sector + ":");
        try {
            BigDecimal allocation = new BigDecimal(input);
            sectors.put(sector, allocation);
            refreshAnalysisPanel();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BudgetTrackerGUI());
    }
}
