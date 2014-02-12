package graphics.mandelbrot;

/**
 *
 * @author main
 */
public interface MandelbrotColourtableFunction {
    public int getColour(int iteration, int maxIterations, double real, double imaginary);
    public boolean isAdvanced();
}
