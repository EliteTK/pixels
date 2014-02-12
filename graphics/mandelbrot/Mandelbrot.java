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

    private final int baseWidth, baseHeight;
    private int width, height, maxIter, nextUncalculatedPixel = 0, sampleRate, totalPixels;
    private double basePixelStep, pixelStep, startx, starty, endx, endy, invIter;

    private final Window window;
    private DrawingPlane dp;
    private MandelbrotColourtable mc;
    private final MandelbrotColourtableFunction mcf;

    boolean needsRedrawing, saveImage;

    /**
     * Create a new Mandelbrot generator drawing class.
     *
     * @param window - The window to use for storing the drawing plane element.
     * @param mcf - MandelbrotColourtableFunction used for creating
     * colour-tables.
     * @param width - The width of the output.
     * @param height - The height of the output.
     * @param posx - The x centre position.
     * @param posy - The x centre position.
     * @param pixelStep - The actual numerical step that each pixel represents.
     * @param maxIter - The maximum number of iterations to test each pixel
     * with.
     * @param sampleRate - The sample rate, the square of this is the actual
     * rate.
     * @param saveImage - Whether to save the output of the Mandelbrot set as an
     * image.
     */
    public Mandelbrot(Window window, MandelbrotColourtableFunction mcf, int width, int height, double posx, double posy, double pixelStep, int maxIter, int sampleRate, boolean saveImage) {

        this.baseWidth = width;
        this.baseHeight = height;

        this.mcf = mcf;

        this.window = window;

        this.updateViewport(posx, posy, pixelStep, maxIter, sampleRate, saveImage);
    }

    public void updateViewport(double posx, double posy, double pixelStep, int maxIter, int sampleRate, boolean saveImage) {
        this.width = this.baseWidth * sampleRate;
        this.height = this.baseHeight * sampleRate;

        if (this.sampleRate != sampleRate) {

            if (sampleRate == 1) {
                this.dp = new DrawingPlane(this.baseWidth, this.baseHeight, this.window);
            } else {
                this.dp = new DrawingPlaneSupersampled(this.baseWidth, this.baseHeight, this.window, sampleRate);
            }
        }
        this.sampleRate = sampleRate;

        this.basePixelStep = pixelStep;
        this.pixelStep = this.basePixelStep;

        this.startx = posx - this.width / 2 * pixelStep;
        this.starty = posy - this.height / 2 * pixelStep;

        this.endx = posx + this.width / 2 * pixelStep;
        this.endy = posy + this.height / 2 * pixelStep;

        this.maxIter = maxIter;

        this.invIter = (double) 1 / (double) maxIter;

        this.mc = new MandelbrotColourtable(mcf, maxIter, Color.BLACK.getRGB());

        this.totalPixels = this.width * this.height;

        this.saveImage = saveImage;

        this.needsRedrawing = true;
    }

    public void draw() {
        if (this.needsRedrawing) {
            long startTime = System.currentTimeMillis();

            this.nextUncalculatedPixel = 0;

            Thread t0 = new Thread(this);
            Thread t1 = new Thread(this);
            Thread t2 = new Thread(this);
            Thread t3 = new Thread(this);

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

            window.frame().setTitle("Mandelbrot " + String.valueOf(System.currentTimeMillis() - startTime) + " miliseconds " + String.valueOf(maxIter) + " iterations.");
//            System.out.printf("It took %d miliseconds to draw the image.%n", System.currentTimeMillis() - startTime);
            this.needsRedrawing = false;
        }
        dp.redraw();
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
                drawByValue(pos, mc.getColour(iter, xIterator, yIterator));
            } else {
                iterateMore = false;
            }
        }
    }

    public DrawingPlane getDrawingPlane() {
        return this.dp;
    }
}
