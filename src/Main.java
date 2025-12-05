import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainFrame extends JFrame {

    private GamePanel gamePanel;
    private Robot robot;
    private JPanel mainPanel; // Panel using CardLayout to switch between Menu and Game
    private JPanel gameContainer; // Reference to the game container panel

    // Level Management
    private int currentLevelIndex = 0;
    private final List<Level> levels = createLevels();

    public MainFrame() {
        super("Simple Robot Programming Game 🤖");

        // Initialize the robot with default position from level 1
        Level firstLevel = levels.get(0);
        this.robot = new Robot(firstLevel.startX, firstLevel.startY, firstLevel.startDirection, null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        this.mainPanel = new JPanel(new CardLayout());
        add(mainPanel, BorderLayout.CENTER);

        createMainMenu();

        // Finalize Configuration
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // --- I. LEVEL DEFINITIONS ---
    private List<Level> createLevels() {
        // Define obstacles for 5 levels
        List<Point> obs1 = Arrays.asList(new Point(3, 3), new Point(4, 3), new Point(5, 3), new Point(6, 3));
        List<Point> obs2 = Arrays.asList(new Point(1, 4), new Point(2, 4), new Point(4, 4), new Point(5, 4), new Point(3, 1), new Point(3, 2), new Point(3, 3), new Point(3, 5));
        List<Point> obs3 = Arrays.asList(new Point(1, 1), new Point(2, 1), new Point(3, 1), new Point(4, 1), new Point(5, 1), new Point(7, 1), new Point(8, 1), new Point(9, 1), new Point(10, 1));
        List<Point> obs4 = new ArrayList<>();
        for (int i = 1; i < 7; i++) { for (int j = 1; j < 7; j++) { obs4.add(new Point(i, j)); } }
        obs4.remove(new Point(1, 1)); obs4.remove(new Point(6, 6));
        List<Point> obs5 = Arrays.asList(new Point(1, 1), new Point(2, 2), new Point(3, 3), new Point(5, 5), new Point(6, 6), new Point(7, 7));

        // Create 5 level configurations
        List<Level> lvls = new ArrayList<>();
        lvls.add(new Level(10, 10, 1, 1, "NORTH", new Point(8, 8), obs1));
        lvls.add(new Level(7, 7, 0, 0, "EAST", new Point(6, 6), obs2));
        lvls.add(new Level(12, 6, 0, 5, "SOUTH", new Point(11, 0), obs3));
        lvls.add(new Level(8, 8, 0, 7, "SOUTH", new Point(7, 0), obs4));
        lvls.add(new Level(9, 9, 0, 8, "SOUTH", new Point(8, 0), obs5));

        return lvls;
    }

    // --- II. MENU SETUP ---
    private void createMainMenu() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        JLabel title = new JLabel("Robot Command Challenge");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnPlay = new JButton("PLAY GAME");
        JButton btnCustomizeRobot = new JButton("CUSTOMIZE ROBOT");
        JButton btnCustomizeMap = new JButton("MAP THEME");
        JButton btnExit = new JButton("EXIT");

        btnPlay.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnCustomizeRobot.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnCustomizeMap.setFont(new Font("SansSerif", Font.PLAIN, 18));
        btnExit.setFont(new Font("SansSerif", Font.PLAIN, 18));

        btnPlay.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCustomizeRobot.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCustomizeMap.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExit.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnPlay.addActionListener(e -> startGame());
        btnCustomizeRobot.addActionListener(e -> openColorSelectorDialog());
        btnCustomizeMap.addActionListener(e -> openThemeSelectorDialog());
        btnExit.addActionListener(e -> System.exit(0));

        menuPanel.add(title);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        menuPanel.add(btnPlay);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        menuPanel.add(btnCustomizeRobot);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        menuPanel.add(btnCustomizeMap);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        menuPanel.add(btnExit);

        mainPanel.add(menuPanel, "Menu");

        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "Menu");
    }

    // --- III. CUSTOMIZATION DIALOGS ---

    /**
     * Opens a dialog with theme buttons for map customization.
     */
    private void openThemeSelectorDialog() {
        // KEY FIX 1: Initialize GamePanel if null. This allows pre-game theme setting.
        if (gamePanel == null) {
            Level firstLevel = levels.get(0);
            this.gamePanel = new GamePanel(robot, firstLevel.goal, firstLevel.obstacles);
            this.robot.setGamePanel(gamePanel);
            // We do NOT add the game container here, only in startGame().
        }

        GamePanel.GameTheme[] themes = GamePanel.GameTheme.values();
        String[] themeNames = {"Default (Light)", "Dark Mode", "Retro Green"};

        JPanel themePanel = new JPanel(new GridLayout(themes.length, 1, 10, 10));
        themePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JDialog themeDialog = new JDialog(this, "Select Map Theme", true);
        themeDialog.setLayout(new BorderLayout());

        for (int i = 0; i < themes.length; i++) {
            JButton btn = new JButton(themeNames[i]);
            final GamePanel.GameTheme selectedTheme = themes[i];

            btn.addActionListener(e -> {
                gamePanel.setTheme(selectedTheme);
                gamePanel.update(); // Redraw with new theme immediately
                themeDialog.dispose();
                JOptionPane.showMessageDialog(this, "Map theme changed to " + btn.getText() + "!", "Customization Complete", JOptionPane.INFORMATION_MESSAGE);
            });
            themePanel.add(btn);
        }

        themeDialog.add(themePanel, BorderLayout.CENTER);
        themeDialog.pack();
        themeDialog.setLocationRelativeTo(this);
        themeDialog.setVisible(true);
    }

    /**
     * Opens a dialog with preset color buttons for robot customization.
     */
    private void openColorSelectorDialog() {
        // ... (Robot color selection remains the same) ...
        Color[] availableColors = {
                Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK.darker()
        };
        String[] colorNames = {
                "Blue", "Red", "Green", "Yellow", "Cyan", "Magenta", "Orange", "Pink"
        };

        JPanel colorPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        colorPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JDialog colorDialog = new JDialog(this, "Select Robot Color", true);
        colorDialog.setLayout(new BorderLayout());

        for (int i = 0; i < availableColors.length; i++) {
            JButton btn = new JButton(colorNames[i]);
            btn.setBackground(availableColors[i]);
            btn.setOpaque(true);
            btn.setBorderPainted(false);

            Color bgColor = availableColors[i];
            if (bgColor.getRed() < 100 && bgColor.getGreen() < 100 && bgColor.getBlue() < 100) {
                btn.setForeground(Color.WHITE);
            } else {
                btn.setForeground(Color.BLACK);
            }

            final Color selectedColor = availableColors[i];

            btn.addActionListener(e -> {
                robot.setRobotColor(selectedColor);
                if (gamePanel != null) {
                    gamePanel.update();
                }
                colorDialog.dispose();
                JOptionPane.showMessageDialog(this, "Robot color changed to " + btn.getText() + "!", "Customization Complete", JOptionPane.INFORMATION_MESSAGE);
            });
            colorPanel.add(btn);
        }

        colorDialog.add(colorPanel, BorderLayout.CENTER);
        colorDialog.pack();
        colorDialog.setLocationRelativeTo(this);
        colorDialog.setVisible(true);
    }


    // --- IV. GAME INITIALIZATION ---
    private void startGame() {
        currentLevelIndex = 0;

        // 1. Ensure GamePanel is initialized (if not already done by theme selector)
        if (gamePanel == null) {
            Level firstLevel = levels.get(currentLevelIndex);
            this.gamePanel = new GamePanel(robot, firstLevel.goal, firstLevel.obstacles);
            this.robot.setGamePanel(gamePanel);
        }

        // 2. KEY FIX 2: Ensure the GameContainer (the card "Game") is only added ONCE.
        if (gameContainer == null) {
            this.gameContainer = createGameContainer(); // Initialize and store reference
            mainPanel.add(gameContainer, "Game");
        }

        // 3. Load the first level
        loadLevel(currentLevelIndex);

        // 4. Show the game card
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, "Game");
    }

    /**
     * Creates the main game area panel, including the game board and the command panel.
     */
    private JPanel createGameContainer() {
        JPanel container = new JPanel(new BorderLayout());

        JPanel gameArea = new JPanel(new BorderLayout());
        gameArea.add(gamePanel, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        JButton btnMenu = new JButton("Menu");
        btnMenu.setMargin(new Insets(2, 5, 2, 5));

        btnMenu.addActionListener(e -> {
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "Menu");
            this.setTitle("Simple Robot Programming Game 🤖");
        });

        topPanel.add(btnMenu);

        gameArea.add(topPanel, BorderLayout.NORTH);

        container.add(gameArea, BorderLayout.CENTER);
        container.add(createCommandPanel(), BorderLayout.SOUTH);

        return container;
    }

    // --- V. COMMAND PANEL ---
    private JPanel createCommandPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton btnForward = new JButton("FORWARD");
        JButton btnTurnRight = new JButton("TURN RIGHT");
        JButton btnTurnLeft = new JButton("TURN LEFT");

        ActionListener movementListener = e -> {
            if (robot == null) return;

            JButton button = (JButton) e.getSource();
            String command = button.getText();

            if (command.equals("FORWARD")) {
                robot.moveForward();
            } else if (command.equals("TURN RIGHT")) {
                robot.turnRight();
            } else if (command.equals("TURN LEFT")) {
                robot.turnLeft();
            }

            gamePanel.update();
            checkGoal();
        };

        btnForward.addActionListener(movementListener);
        btnTurnRight.addActionListener(movementListener);
        btnTurnLeft.addActionListener(movementListener);

        panel.add(btnForward);
        panel.add(btnTurnRight);
        panel.add(btnTurnLeft);

        return panel;
    }

    // --- VI. LEVEL MANAGEMENT LOGIC ---
    private void loadLevel(int index) {
        if (index < 0 || index >= levels.size()) {
            JOptionPane.showMessageDialog(this, "All levels completed! Game over!", "FINAL VICTORY", JOptionPane.INFORMATION_MESSAGE);
            CardLayout cl = (CardLayout) mainPanel.getLayout();
            cl.show(mainPanel, "Menu");
            this.setTitle("Simple Robot Programming Game 🤖");
            return;
        }

        Level level = levels.get(index);
        this.setTitle("Robot Programming Game - Level " + (index + 1) + "/" + levels.size());

        gamePanel.loadLevel(level.goal, level.obstacles, level.boardWidth, level.boardHeight);
        robot.setPosition(level.startX, level.startY, level.startDirection);

        // Repack the frame to adjust for new board dimensions
        pack();
        gamePanel.update();
    }

    private void checkGoal() {
        if (gamePanel.isGoalReached()) {
            JOptionPane.showMessageDialog(this, "Level " + (currentLevelIndex + 1) + " Complete!", "VICTORY!", JOptionPane.INFORMATION_MESSAGE);

            currentLevelIndex++;
            loadLevel(currentLevelIndex);
        }
    }

    /**
     * The Main method that starts the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
