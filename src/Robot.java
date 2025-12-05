import java.awt.Color;
import java.awt.Point;

public class Robot {

    private int x;
    private int y;
    private String direction; // "NORTH", "EAST", "SOUTH", "WEST"
    private GamePanel gamePanel;
    private Color robotColor; // Custom color for the robot

    public Robot(int x, int y, String direction, GamePanel gamePanel) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.gamePanel = gamePanel;
        this.robotColor = Color.BLUE; // Default color
    }

    /**
     * Sets the associated GamePanel instance.
     * @param gamePanel The panel where the robot is drawn.
     */
    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Sets the robot's position and direction for a new level.
     */
    public void setPosition(int x, int y, String direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    /**
     * Moves the robot one step forward, checking for collisions (walls and obstacles).
     */
    public void moveForward() {
        if (gamePanel == null) return;

        int nextX = this.x;
        int nextY = this.y;

        // Calculate next position based on current direction
        if (this.direction.equals("NORTH")) {
            nextY++;
        } else if (this.direction.equals("SOUTH")) {
            nextY--;
        } else if (this.direction.equals("EAST")) {
            nextX++;
        } else if (this.direction.equals("WEST")) {
            nextX--;
        }

        // 1. Check board boundaries
        if (nextX < 0 || nextX >= gamePanel.getBoardWidth() ||
                nextY < 0 || nextY >= gamePanel.getBoardHeight()) {
            System.out.println("Movement blocked by boundary!");
            return;
        }

        // 2. Check obstacles
        if (gamePanel.isObstacle(nextX, nextY)) {
            System.out.println("Movement blocked by obstacle!");
            return;
        }

        // If safe, update position
        this.x = nextX;
        this.y = nextY;
    }

    /**
     * Rotates the robot 90 degrees right.
     */
    public void turnRight() {
        switch (this.direction) {
            case "NORTH": this.direction = "EAST"; break;
            case "EAST": this.direction = "SOUTH"; break;
            case "SOUTH": this.direction = "WEST"; break;
            case "WEST": this.direction = "NORTH"; break;
        }
    }

    /**
     * Rotates the robot 90 degrees left.
     */
    public void turnLeft() {
        switch (this.direction) {
            case "NORTH": this.direction = "WEST"; break;
            case "WEST": this.direction = "SOUTH"; break;
            case "SOUTH": this.direction = "EAST"; break;
            case "EAST": this.direction = "NORTH"; break;
        }
    }

    // --- Getters and Setters for Customization ---
    public Color getRobotColor() {
        return robotColor;
    }

    public void setRobotColor(Color color) {
        this.robotColor = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getDirection() {
        return direction;
    }
}
