package start;

import graphics.mandelbrot.MandelbrotColourtableFunction;
import java.awt.Color;

/**
 *
 * @author main
 */
public class Stalk implements MandelbrotColourtableFunction {

    double real;
    double imaginary;

    @Override
    public int getColour(int iter, int maxIter, double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
        double distance = Math.min(Math.abs(real), Math.abs(imaginary)) / 0.03;
        double xIter = 0;
        double yIter = 0;
        int i = 0;
        while (i < 100 && (xIter * xIter + yIter * yIter) < 1000) {
            
        }
    }

    @Override
    public boolean isAdvanced() {
        return true;
    }
}
