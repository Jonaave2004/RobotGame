import java.awt.Point;
import java.util.List;

/**
 * Helper class to hold all configuration data for a game level.
 */
public class Level {
    public final int boardWidth;
    public final int boardHeight;
    public final int startX;
    public final int startY;
    public final String startDirection;
    public final Point goal;
    public final List<Point> obstacles;

    /**
     * Constructs a Level object.
     * * @param w Board width.
     * @param h Board height.
     * @param sx Robot starting X coordinate.
     * @param sy Robot starting Y coordinate.
     * @param sDir Robot starting direction ("NORTH", "EAST", etc.).
     * @param g Goal coordinates.
     * @param obs List of obstacle coordinates.
     */
    public Level(int w, int h, int sx, int sy, String sDir, Point g, List<Point> obs) {
        this.boardWidth = w;
        this.boardHeight = h;
        this.startX = sx;
        this.startY = sy;
        this.startDirection = sDir;
        this.goal = g;
        this.obstacles = obs;
    }
}
