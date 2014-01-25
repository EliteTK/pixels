package graphics.mandelbrot;

import pos.Posxy;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import window.*;

/**
 *
 * @author main
 */
public final class Mandelbrot implements Runnable {

    private final int width, height, totalPixels;
    private int maxIter, nextUncalculatedPixel = 0;
    private double pixelStep, startx, starty, endx, endy, invIter;

    private final Window window;
    private final DrawingPlane dp;
    private final MandelbrotColourtable mc;

    private final Thread t0;
    private final Thread t1;
    private final Thread t2;
    private final Thread t3;

    boolean needsRedrawing, saveImage;

    /**
     * Create a new Mandelbrot generator drawing class.
     *
     * @param window
     * @param mcf
     * @param width
     * @param height
     * @param posx
     * @param posy
     * @param pixelStep
     * @param maxIter
     * @param saveImage
     */
    public Mandelbrot(Window window, MandelbrotColourtableFunction mcf, int width, int height, double posx, double posy, double pixelStep, int maxIter, boolean saveImage) {

        this.width = width;
        this.height = height;
        
        this.updateViewport(posx, posy, pixelStep, maxIter);
        
        this.window = window;
        this.dp = new DrawingPlane(width, height, window);

        this.totalPixels = this.width * this.height;

        this.mc = new MandelbrotColourtable(mcf, maxIter, Color.BLACK.getRGB());

        for (int i = 1; i <= maxIter; i++) {
            System.out.printf("%d: %d%n", i, mc.getColour(i));
        }
        
        this.t0 = new Thread(this);
        this.t1 = new Thread(this);
        this.t2 = new Thread(this);
        this.t3 = new Thread(this);
    }

    public void updateViewport(double posx, double posy, double pixelStep, int maxIter) {
        this.pixelStep = pixelStep;

        this.startx = posx - this.width / 2 * pixelStep;
        this.starty = posy - this.width / 2 * pixelStep;

        this.endx = posx + this.width / 2 * pixelStep;
        this.endy = posy + this.width / 2 * pixelStep;

        this.maxIter = maxIter;

        this.invIter = (double) 1 / (double) maxIter;

        this.needsRedrawing = true;
    }

    public void draw() {
        if (this.needsRedrawing) {
            long startTime = System.currentTimeMillis();

            t0.start();
            t1.start();
            t2.start();
            t3.start();

            try {
                t0.join();
                t1.join();
                t2.join();
                t3.join();

            } catch (InterruptedException e) {
                System.err.println("Oops failed to draw image. Unexpected interrupt.");
            }
            System.out.printf("It took %d miliseconds to draw the image.%n", System.currentTimeMillis() - startTime);
            dp.redraw();
        }
        if (this.saveImage) {
            try {
                System.out.println("Attempting to save as png.");
                BufferedImage bi = dp.getBufferedImage();
                File output = new File("mandelbrot.png");
                ImageIO.write(bi, "png", output);
                System.out.println("Successfuly saved as png");
            } catch (IOException e) {
                System.err.println("Failed to save as image.");
            }
        }
    }

    private int getNextUncalculated() {
        if (nextUncalculatedPixel < totalPixels) {
            return nextUncalculatedPixel++;
        } else {
            return -1;
        }
    }

    private Posxy resolveFromValue(int value) {

        int x = (int) Math.floor(value / this.height);
        int y = value - x * this.height;
        y = this.height - y;
        return new Posxy(startx + x * this.pixelStep, starty + y * this.pixelStep);
    }

    private void drawByValue(int value, int colour) {
        // Resolve x and y coordinates - would be nice to remove the need for division in the future.
        int x = (int) Math.floor(value / this.height);
        int y = value - x * this.height;
        // Set the colour.
        this.dp.setPixel(x, y, colour);
    }

    @Override
    public void run() {
        boolean iterateMore = true;
        while (iterateMore) {
            int pos = getNextUncalculated();
            if (pos != -1) {
                Posxy startPoint = resolveFromValue(pos);
                double xConst = startPoint.getX();
                double yConst = startPoint.getY();
                double xIterator = 0.0;
                double yIterator = 0.0;

                int iter = 0;
                while ((xIterator * xIterator + yIterator * yIterator < 4) && iter < maxIter) {
                    double xtemp = xIterator * xIterator - yIterator * yIterator + xConst;
                    yIterator = 2 * xIterator * yIterator + yConst;
                    xIterator = xtemp;
                    iter++;
                }
                drawByValue(pos, mc.getColour(iter));
            } else {
                iterateMore = false;
            }
        }
    }
    
    public DrawingPlane getDrawingPlane () {
        return this.dp;
    }
}
