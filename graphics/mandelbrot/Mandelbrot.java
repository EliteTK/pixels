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

    final int width, height, totalPixels;
    int maxIter, nextUncalculatedPixel = 0;
    double pixelStep, startx, starty, endx, endy, invIter;

    Window window;
    DrawingPlane dp;
    MandelbrotColourtable mc;

    Thread t0 = new Thread(this);
    Thread t1 = new Thread(this);
    Thread t2 = new Thread(this);
    Thread t3 = new Thread(this);
//    Thread t4 = new Thread(this);
//    Thread t5 = new Thread(this);
//    Thread t6 = new Thread(this);
//    Thread t7 = new Thread(this);

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
//        t4.start();
//        t5.start();
//        t6.start();
//        t7.start();

            try {
                t0.join();
                t1.join();
                t2.join();
                t3.join();
//            t4.join();
//            t5.join();
//            t6.join();
//            t7.join();

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
        boolean cont = true;
        while (cont) {
            int pos = getNextUncalculated();
            if (pos != -1) {
                Posxy startPoint = resolveFromValue(pos);
                double x0 = startPoint.getX();
                double y0 = startPoint.getY();
                double x = 0.0;
                double y = 0.0;

                int iter = 0;
                while ((x * x + y * y < 4) && iter < maxIter) {
                    double xtemp = x * x - y * y + x0;
                    y = 2 * x * y + y0;
                    x = xtemp;
                    iter++;
                }
                int colour = mc.getColour(iter);
                drawByValue(pos, colour);
            } else {
                cont = false;
            }
        }
    }
}
