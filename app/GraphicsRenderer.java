package app;

import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;

import fundamentals.Constants;
import fundamentals.component.ComponentRegistry;

/**
 * GraphicsRenderer is responsible for painting all Components onto the application's window with the use of the Graphics class, and
 * the Graphics instance passed into the overridden method, paintComponent(Graphics graphics), 
 * from GraphicsRenderer's superclass, JPanel. Moreover, Graphics is used to paint each Component from the ComponentScheduler's
 * list of registered Component instances onto the screen. 
 * 
 * @see Given that registered Components remain registered unless they are manually unregistered, all registered Components will
 * remain continuously being painted on-screen, allowing any changes they may undergo at any moment be reflected on-screen.  
 */
public class GraphicsRenderer extends JPanel 
{
    private static GraphicsRenderer app_graphics = new GraphicsRenderer();

    /**
     * GraphicsRenderer is responsible for painting all Components onto the application's window with the use of the Graphics class, and
     * the Graphics instance passed into the overridden method, paintComponent(Graphics graphics), 
     * from GraphicsRenderer's superclass, JPanel. Moreover, Graphics is used to paint each Component from the ComponentScheduler's
     * list of registered Component instances onto the screen. 
     * 
     * @see Given that registered Components remain registered unless they are manually unregistered, all registered Components will
     * remain continuously being painted on-screen, allowing any changes they may undergo at any moment be reflected on-screen.  
     */
    public GraphicsRenderer() {}

    @Override
    protected void paintComponent(Graphics graphics) {
        // Get graphics:
        Graphics2D graphics_2d = (Graphics2D)graphics;
        // Render cleared backround before rendering:
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, Constants.WINDOW_CHARACTERISTICS.WINDOW_WIDTH, Constants.WINDOW_CHARACTERISTICS.WINDOW_HEIGHT);
        // Iterate through component registry and render components:
        for(var comp : ComponentRegistry.getComponents()) {
            try {
                if(comp.getActivity()) {
                    int x = comp.getCoordinates().getX() - (comp.getWidth() / 2);
                    int y = comp.getCoordinates().getY() - (comp.getHeight() / 2);    

                    AffineTransform original_transformation = graphics_2d.getTransform();
                    double radians = Math.toRadians(comp.getCoordinates().getDegrees());
                
                    graphics_2d.setClip(0, 0, Constants.WINDOW_CHARACTERISTICS.WINDOW_WIDTH, Constants.WINDOW_CHARACTERISTICS.WINDOW_HEIGHT);
                    graphics_2d.translate(comp.getCoordinates().getX(), comp.getCoordinates().getY());
                    graphics_2d.rotate(radians);
                    graphics_2d.translate(-comp.getCoordinates().getX(), -comp.getCoordinates().getY());
                    graphics_2d.setComposite(AlphaComposite.SrcOver.derive((float)comp.getOpacity()));
                    graphics_2d.drawImage(comp.getAnimation(), x, y, null);
                    graphics_2d.setComposite(AlphaComposite.SrcOver.derive(1));
                    graphics_2d.setTransform(original_transformation);
                }
            }
            catch(ArrayIndexOutOfBoundsException e) {}
        }

        repaint();
    }

    public static GraphicsRenderer getInstance() {
        return app_graphics;
    }
}