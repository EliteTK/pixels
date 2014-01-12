/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package window;

import java.awt.image.BufferedImage;

/**
 *
 * @author main
 */
public class DrawingPlane {
    BufferedImage image;
    Window window;
    
    /**
     *
     * @param width
     * @param height
     * @param window
     */
    public DrawingPlane(int width, int height, Window window) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.window = window;
    }
}
