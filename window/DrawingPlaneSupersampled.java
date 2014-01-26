/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author main
 */
public class DrawingPlaneSupersampled extends DrawingPlane {

    BufferedImage imageDownsampled;
    final int sampleRate;

    /**
     *
     * @param width
     * @param height
     * @param window
     * @param sampleRate - The square root of the super-sampling rate.
     */
    public DrawingPlaneSupersampled(int width, int height, Window window, int sampleRate) {
        super(width * sampleRate, height * sampleRate, window);
        this.sampleRate = sampleRate;
        imageDownsampled = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        System.out.printf("Debug: The image should have size %d by %d%n", width, height);
    }

    public void downsample() {
        for (int x = 0; x < super.width / sampleRate; x++) {
            for (int y = 0; y < super.height / sampleRate; y++) {
                int red = 0;
                int green = 0;
                int blue = 0;
                for (int sx = 0; sx < sampleRate; sx++) {
                    for (int sy = 0; sy < sampleRate; sy++) {
                        red += (super.image.getRGB(x * sampleRate + sx, y * sampleRate + sy) >> 16) & 0x0000FF;
                        green += (super.image.getRGB(x * sampleRate + sx, y * sampleRate + sy) >> 8) & 0x0000FF;
                        blue += super.image.getRGB(x * sampleRate + sx, y * sampleRate + sy) & 0x0000FF;
                    }
                }
                red /= sampleRate * sampleRate;
                green /= sampleRate * sampleRate;
                blue /= sampleRate * sampleRate;
                this.imageDownsampled.setRGB(x, y, (red << 16) + (green << 8) + (blue));
            }
        }
    }

    @Override
    public void redraw() {
        downsample();
        Graphics g = window.getGraphics();
        g.drawImage(imageDownsampled, 0, 0, super.width / sampleRate, super.height / sampleRate, window);
        g.dispose();
    }
}
