package graphics;

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
public class Mandelbrot implements Runnable {

    int maxIter;
    double stepx, stepy, startx, starty, endx, endy, invIter;

    Window window;
    DrawingPlane dp;
    int nextUncalculated = 0, maxCalculated;

    int width = 900;
    int height = 600;

    public Mandelbrot(Window window, int startx, int endx, int starty, int endy, int maxIter) {
        this.startx = startx;
        this.starty = starty;
        this.endx = endx;
        this.endy = endy;

        this.stepx = (double)(endx - startx) / (double)this.width;
        this.stepy = (double)(endy - starty) / (double)this.height;

        this.maxIter = maxIter;
        this.window = window;
        this.dp = new DrawingPlane(this.width, this.height, window);
        this.maxCalculated = this.width * this.height;
        this.invIter = (double)1/ (double)maxIter;
    }

    public void draw() {
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
        try {
        BufferedImage bi = dp.getBufferedImage();
        File output = new File("mandelbrot.png");
        ImageIO.write(bi, "png", output);
        } catch (IOException e) {
            System.err.println("Failed to save as image.");
        }
        while (true) {
            dp.redraw();
        }
    }

    private synchronized int getNextUncalculated() {
        if (nextUncalculated < maxCalculated) {
            return nextUncalculated++;
        } else {
            return -1;
        }
    }

    private Posxy resolveFromValue(int value) {
        int x = (int) Math.floor(value / this.height);
        int y = value - x * this.height;
        y = this.height - y;
        return new Posxy(startx + x * this.stepx, starty + y * this.stepy);
    }

    private void drawByValue(int value, int color) {
        int x = (int) Math.floor(value / this.height);
        int y = value - x * this.height;
        this.dp.setPixel(x, y, color);
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
                while ((x*x + y*y < 4) && iter < maxIter) {
                    double xtemp = x*x - y*y + x0;
                    y = 2*x*y + y0;
                    x = xtemp;
                    iter++;
                }
                int shade = (int)((double)iter * invIter * (double)255);
                
                int color = (iter < maxIter ? (new Color(shade, shade/2, shade/2)).getRGB() : Color.BLACK.getRGB());
                drawByValue(pos, color);
            } else {
                cont = false;
            }
        }
    }
}
