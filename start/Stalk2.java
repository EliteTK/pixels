package start;

import graphics.mandelbrot.MandelbrotColourtableFunction;
import java.awt.Color;

/**
 *
 * @author main
 */
public class Stalk2 implements MandelbrotColourtableFunction {

    @Override
    public int getColour(int iter, int maxIter, double real, double imaginary) {
        double distance = Math.max(Math.abs(1 / real), Math.abs(1 / imaginary));
        if (distance == 0) {
            return 0;
        } else {
            return (new Color(0.5f, 0.5f, (float)distance)).getRGB();
        }
    }

    @Override
    public boolean isAdvanced() {
        return true;
    }
}
