package game.ui.gameplay;

import game.map.Board;
import game.map.City;
import game.map.Route;
import game.strategy.routeclaiming.FerryRouteClaimStrategy;
import game.ui.utils.ThemeUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;

/**
 * Primary View component responsible for rendering the map,cities, routes, and
 * handling user interactions (clicks) on the board.
 *
 * @author Hew Jia Jing
 */
public class GameBoardPanel extends JPanel {

    private final Board board;
    private String selectedRouteId;

    private Consumer<String> onRouteSelected;

    private static final int DESIGN_WIDTH = 1100;
    private static final int DESIGN_HEIGHT = 750;

    private double currentScale = 1.0;

    /**
     * Constructs a new GameBoardPanel.
     *
     * @param board The data model containing cities and routes to render.
     */
    public GameBoardPanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension(DESIGN_WIDTH, DESIGN_HEIGHT));

        // Listen for user clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                handleClick(event.getPoint());
                repaint();
            }
        });
    }

    /**
     * This method registers a callback listener to trigger when a route is
     * successfully clicked.
     *
     * @param listener A Consumer that accepts the ID of the selected route.
     */
    public void setOnRouteSelected(Consumer<String> listener) {
        this.onRouteSelected = listener;
    }

    /**
     * This method returns ID of the currently selected route.
     *
     * @return The ID of the currently selected route.
     */
    public String getSelectedRouteId() {
        return selectedRouteId;
    }

    /**
     * reset the selectedRouteId
     */
    public void resetRouteId() {
        selectedRouteId = null;
    }

    /**
     * This method handles drawing the responsive background,
     * applying the zoom/scale transformations, and rendering the game pieces.
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
        enableQuality(g2);

        // Draw the static background behind the scalable board
        drawBackground(g2);

        // Calculate aspect ratio scaling to ensure the board doesn't stretch.
        double scaleX = (double) getWidth() / DESIGN_WIDTH;
        double scaleY = (double) getHeight() / DESIGN_HEIGHT;
        currentScale = Math.min(scaleX, scaleY);

        // Calculate padding to perfectly center the board in the window
        int offsetX = (int) (getWidth() - (DESIGN_WIDTH * currentScale)) / 2;
        int offsetY = (int) (getHeight() - (DESIGN_HEIGHT * currentScale)) / 2;

        AffineTransform originalTransform = g2.getTransform();

        g2.translate(offsetX, offsetY);
        g2.scale(currentScale, currentScale);

        // Render game elements within the scaled design space
        drawRiver(g2);
        drawRoutes(g2);
        drawCities(g2);

        g2.setTransform(originalTransform);
    }

    /**
     * This method draws the dynamic background gradient and decorative borders.
     */
    private void drawBackground(Graphics2D g2) {
        int w = getWidth();
        int h = getHeight();

        // Base gradient background
        GradientPaint bg = new GradientPaint(
                0, 0, new Color(245, 232, 198),
                w, h, new Color(214, 188, 139)
        );
        g2.setPaint(bg);
        g2.fillRect(0, 0, w, h);

        g2.setColor(new Color(110, 75, 42, 35));
        for (int x = 60; x < w; x += 80) {
            g2.drawLine(x, 35, x - 40, h - 35);
        }

        // Draw outer dark shadow border
        g2.setColor(new Color(70, 40, 20, 45));
        g2.setStroke(new BasicStroke(10));
        g2.drawRoundRect(18, 18, w - 36, h - 36, 32, 32);

        // Draw main inner gold border
        g2.setColor(new Color(177, 125, 58));
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(24, 24, w - 48, h - 48, 26, 26);

        // Draw inner bright highlight border
        g2.setColor(new Color(255, 245, 210, 120));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(34, 34, w - 68, h - 68, 22, 22);
    }

    /**
     * This method iterates through all routes on the board and draws them.
     */
    private void drawRoutes(Graphics2D g2) {

        for (Route route : board.getRoutes()) {

            Point[] endpoints = getVisualEndpoints(route);
            Point p1 = endpoints[0];
            Point p2 = endpoints[1];

            boolean selected = route.getId().equals(selectedRouteId);

            // Draw a glowing yellow aura behind selected routes
            if (selected && !route.isClaimed()) {
                g2.setStroke(new BasicStroke(18, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(new Color(255, 240, 110, 180));
                g2.drawLine(p1.x, p1.y, p2.x, p2.y); // Use p1 and p2!
            }

            // Pass the offset points to the segment drawer
            drawRouteSegments(g2, route, p1, p2, selected);
        }
    }

    /**
     * This method calculates the position and rotation for individual train
     * slots along a route.
     */
    private void drawRouteSegments(Graphics2D g2, Route route, Point a, Point b, boolean selected) {
        double angle = Math.atan2(b.y - a.y, b.x - a.x);

        int ferryIconsToDraw = getFerryIconCount(route);

        for (int i = 1; i <= route.getLength(); i++) {
            double t = i / (double) (route.getLength() + 1);

            int x = (int) (a.x + (b.x - a.x) * t);
            int y = (int) (a.y + (b.y - a.y) * t);

            if (route.isClaimed()) {
                drawClaimedTrainPiece(g2, x, y, angle, getOwnerColor(route));
            } else {
                drawTrainSlot(g2, x, y, angle, getRouteColor(route), selected);

                if (i <= ferryIconsToDraw) {
                    drawFerryIcon(g2, x, y, angle);
                }
            }
        }
    }

    /**
     * This method renders an empty route slot with a recessed 3D effect.
     */
    private void drawTrainSlot(Graphics2D g2, int x, int y, double angle,
                               Color color, boolean selected) {
        AffineTransform old = g2.getTransform();
        g2.translate(x, y);
        g2.rotate(angle);

        // Drop Shadow
        g2.setColor(new Color(0, 0, 0, 65));
        g2.fillRoundRect(-25, -8, 52, 24, 16, 16);

        // Main colored body of the slot
        g2.setColor(selected ? new Color(255, 232, 92) : color);
        g2.fillRoundRect(-24, -12, 48, 22, 15, 15);

        // Top glossy highlight
        g2.setColor(new Color(255, 255, 255, 70));
        g2.fillRoundRect(-20, -9, 40, 7, 12, 12);

        // Inner dark border and center groove
        g2.setColor(new Color(65, 38, 20, 190));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(-24, -12, 48, 22, 15, 15);

        g2.setColor(new Color(40, 25, 15, 90));
        g2.drawLine(-14, -1, 14, -1);

        g2.setTransform(old);
    }

    /**
     * This method renders a populated train piece showing player ownership.
     */
    private void drawClaimedTrainPiece(Graphics2D g2, int x, int y, double angle, Color color) {
        AffineTransform old = g2.getTransform();

        g2.translate(x, y);
        g2.rotate(angle);

        // Heavy drop shadow for raised effect
        g2.setColor(new Color(0, 0, 0, 90));
        g2.fillRoundRect(-27, -10, 56, 26, 16, 16);

        // Main piece body
        g2.setColor(color);
        g2.fillRoundRect(-25, -13, 50, 24, 14, 14);

        // Top edge highlight
        g2.setColor(color.brighter());
        g2.fillRoundRect(-20, -10, 40, 7, 10, 10);

        // Outer rim outline
        g2.setColor(new Color(25, 20, 15, 210));
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawRoundRect(-25, -13, 50, 24, 14, 14);

        // Train wheels
        g2.setColor(new Color(30, 25, 20));
        g2.fillOval(-18, 6, 8, 8);
        g2.fillOval(10, 6, 8, 8);

        // Front cabin roof reflection
        g2.setColor(new Color(255, 255, 255, 90));
        g2.fillRoundRect(8, -8, 10, 8, 4, 4);

        g2.setTransform(old);
    }

    /**
     * This method iterates through and renders all city nodes on the board.
     */
    private void drawCities(Graphics2D g2) {
        for (City city : board.getCities()) {
            int x = city.getX();
            int y = city.getY();

            // Offset drop shadow
            g2.setColor(new Color(0, 0, 0, 65));
            g2.fillOval(x - 17, y - 10, 38, 28);

            // Dark brown base
            g2.setColor(new Color(92, 52, 28));
            g2.fillOval(x - 16, y - 16, 32, 32);

            // Light brown inner circle
            g2.setColor(new Color(255, 230, 160));
            g2.fillOval(x - 11, y - 11, 22, 22);

            // Solid defining outer stroke
            g2.setColor(new Color(120, 65, 30));
            g2.setStroke(new BasicStroke(2.5f));
            g2.drawOval(x - 16, y - 16, 32, 32);

            // Specular reflection (shiny white dot)
            g2.setColor(new Color(255, 255, 255, 90));
            g2.fillOval(x - 7, y - 9, 8, 6);

            drawCityLabel(g2, city, x, y);
        }
    }

    /**
     * This method renders a bordered nameplate floating above a specific city.
     */
    private void drawCityLabel(Graphics2D g2, City city, int x, int y) {

        String name = city.getName();
        g2.setFont(new Font("Serif", Font.BOLD, 13));
        FontMetrics metrics = g2.getFontMetrics();

        // Calculate dynamic dimensions
        int textWidth = metrics.stringWidth(name);
        int boxWidth = textWidth + 14;
        int boxHeight = 20;

        // Center the box horizontally relative to the city center
        int boxX = x - (boxWidth / 2);

        // Position the box slightly below the city circle
        int boxY = y + 18;

        // Label background box
        g2.setColor(new Color(255, 242, 205, 235));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);

        // Label border
        g2.setColor(new Color(120, 65, 30));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 10, 10);

        // City name text
        int textX = boxX + 7;
        int textY = boxY + 14;

        g2.setColor(new Color(60, 35, 18));
        g2.drawString(name, textX, textY);
    }

    /**
     * This method maps a route's enum color to an actual rendering Color.
     *
     * @param route The route to be checked its colour
     * @return The route colour
     */
    private Color getRouteColor(Route route) {
        return ThemeUtils.routeColour(route.getColour());
    }

    /**
     * This method maps a player's enum color to an actual rendering Color.
     *
     * @param route The route to be checked
     * @return The player colour for the route owner
     */
    private Color getOwnerColor(Route route) {
        return ThemeUtils.playerColour(route.getOwner().getColour());
    }

    /**
     * This method processes a raw mouse click by converting it back to the
     * original design coordinate space, then checks if it intersects with any
     * game routes.
     *
     * @param point The raw (x, y) click coordinate from the mouse listener.
     */
    private void handleClick(Point point) {

        // Recalculate current window scaling
        double scaleX = (double) getWidth() / DESIGN_WIDTH;
        double scaleY = (double) getHeight() / DESIGN_HEIGHT;
        double scale = Math.min(scaleX, scaleY);

        // Recalculate centering offsets
        int offsetX = (int) (getWidth() - (DESIGN_WIDTH * scale)) / 2;
        int offsetY = (int) (getHeight() - (DESIGN_HEIGHT * scale)) / 2;

        // "Un-zoom" the click back to the 1100x750 scale
        int designX = (int) ((point.x - offsetX) / scale);
        int designY = (int) ((point.y - offsetY) / scale);
        Point designPoint = new Point(designX, designY);

        // Collision detection using pure design-space coordinates
        for (Route route : board.getRoutes()) {

            if (isNearRoute(designPoint, route)) {
                if (route.isClaimed()) return;

                selectedRouteId = route.getId();
                if (onRouteSelected != null) {
                    onRouteSelected.accept(selectedRouteId);
                }
                return;
            }
        }
    }

    /**
     * This method determines if a given point is close enough to a route
     * line to be considered a click.
     */
    private boolean isNearRoute(Point point, Route route) {

        Point[] endpoints = getVisualEndpoints(route);
        Point p1 = endpoints[0];
        Point p2 = endpoints[1];

        // Calculate perpendicular distance from the point to the SHIFTED line segment
        double distance = java.awt.geom.Line2D.ptSegDist(
                p1.x, p1.y,
                p2.x, p2.y,
                point.x, point.y
        );

        // Return true if the click is within 14 pixels of the physical track
        return distance < 15;
    }

    /**
     * This method calculates the visual start and end points for a route.
     *
     * @param route The route to calculate endpoints for.
     * @return An array of two Points: [startPoint, endPoint].
     */
    private Point[] getVisualEndpoints(Route route) {
        int x1 = route.getCityA().getX();
        int y1 = route.getCityA().getY();
        int x2 = route.getCityB().getX();
        int y2 = route.getCityB().getY();

        // Determine shift direction based on the naming convention
        int offsetAmount = 0;
        if (route.getId().endsWith("(1)")) {
            offsetAmount = -14; // Shift 18 pixels in one perpendicular direction
        } else if (route.getId().endsWith("(2)")) {
            offsetAmount = 14;  // Shift 18 pixels in the opposite perpendicular direction
        }

        // If it's a single route, return the exact city centers
        if (offsetAmount == 0) {
            return new Point[]{new Point(x1, y1), new Point(x2, y2)};
        }

        // Perpendicular Vector Math
        double dx = x2 - x1;
        double dy = y2 - y1;
        double length = Math.sqrt(dx * dx + dy * dy);

        // Calculate the unit normal vector
        double nx = -dy / length;
        double ny = dx / length;

        // Apply the offset
        int offsetX = (int) (nx * offsetAmount);
        int offsetY = (int) (ny * offsetAmount);

        Point p1 = new Point(x1 + offsetX, y1 + offsetY);
        Point p2 = new Point(x2 + offsetX, y2 + offsetY);

        return new Point[]{p1, p2};
    }

    /**
     * This method configures the Graphics context to use high-quality
     * anti-aliasing.
     */
    private void enableQuality(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    private void drawFerryIcon(Graphics2D g2, int x, int y, double angle) {
        AffineTransform old = g2.getTransform();

        g2.translate(x, y);
        g2.rotate(angle);

        g2.setColor(new Color(35, 35, 35));
        g2.fillRoundRect(-12, -5, 24, 10, 3, 3);
        g2.fillRect(-8, -12, 10, 8);

        g2.setColor(Color.BLACK);
        g2.fillOval(-10, 4, 6, 6);
        g2.fillOval(4, 4, 6, 6);

        g2.setTransform(old);
    }

    private int getFerryIconCount(Route route) {
        if (route.getRouteClaimStrategy() instanceof FerryRouteClaimStrategy ferryStrategy) {
            return ferryStrategy.getRequiredWildCards();
        }

        return 0;
    }

    private void drawRiver(Graphics2D g2) {
        Stroke oldStroke = g2.getStroke();
        Paint oldPaint = g2.getPaint();

        for (Route route : board.getRoutes()) {
            if (getFerryIconCount(route) <= 0) {
                continue;
            }

            Point[] endpoints = getVisualEndpoints(route);
            Point p1 = endpoints[0];
            Point p2 = endpoints[1];

            double angle = Math.atan2(p2.y - p1.y, p2.x - p1.x);
            double length = p1.distance(p2);

            int cx = (p1.x + p2.x) / 2;
            int cy = (p1.y + p2.y) / 2;

            AffineTransform oldTransform = g2.getTransform();
            g2.translate(cx, cy);
            g2.rotate(angle);

            int lakeWidth = (int) length + 45;
            int lakeHeight = 62;

            // Shadow
            g2.setColor(new Color(40, 90, 140, 80));
            g2.fillOval(
                    -lakeWidth / 2 + 4,
                    -lakeHeight / 2 + 4,
                    lakeWidth,
                    lakeHeight
            );

            // Main lake
            g2.setColor(new Color(85, 155, 200, 120));
            g2.fillOval(
                    -lakeWidth / 2,
                    -lakeHeight / 2,
                    lakeWidth,
                    lakeHeight
            );

            // Inner highlight
            g2.setColor(new Color(120, 190, 220, 80));
            g2.fillOval(
                    -lakeWidth / 2 + 14,
                    -lakeHeight / 2 + 10,
                    lakeWidth - 28,
                    lakeHeight - 20
            );

            // Outline
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(new Color(45, 100, 150, 105));
            g2.drawOval(
                    -lakeWidth / 2,
                    -lakeHeight / 2,
                    lakeWidth,
                    lakeHeight
            );

            g2.setTransform(oldTransform);
        }

        g2.setStroke(oldStroke);
        g2.setPaint(oldPaint);
    }
}
