package window;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author main
 */
public class DrawingPlane {
    BufferedImage image;
    Window window;
    
    int width;
    int height;
    
    /**
     *
     * @param width
     * @param height
     * @param window
     */
    public DrawingPlane(int width, int height, Window window) {
        this.width = width;
        this.height = height;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.window = window;
    }
    
    public void setPixel(int x, int y, int colour) {
        this.image.setRGB(x, y, colour);
    }
    
    public void redraw() {
        Graphics g = this.window.getGraphics();
        g.drawImage(this.image, 0, 0, this.width, this.height, this.window);
        g.dispose();
    }
    
    public BufferedImage getBufferedImage() {
        return this.image;
    }
}
