package graphics.mandelbrot;

/**
 *
 * @author main
 */
public final class MandelbrotColourtable {

    private final int colourTable[];
    private final int maxIter, maxIterColour;
    private final MandelbrotColourtableFunction mcf;

    public MandelbrotColourtable(MandelbrotColourtableFunction mcf, int maxIter, int maxIterColour) {
        this.mcf = mcf;
        if (!mcf.isAdvanced()) {
            this.colourTable = new int[maxIter];
            this.maxIter = maxIter;
            this.maxIterColour = maxIterColour;

            this.recalculate(mcf);
        } else {
            this.colourTable = new int[0];
            this.maxIter = 0;
            this.maxIterColour = 0;
        }

    }

    /**
     * Recalculate the colour lookup table.
     *
     * @param mcf
     */
    public void recalculate(MandelbrotColourtableFunction mcf) {
        for (int i = 0; i < this.maxIter; i++) {
            this.colourTable[i] = mcf.getColour(i, maxIter, 0, 0);
        }
    }

    /**
     * Lookup colour for iteration.
     *
     * @param i
     * @param real
     * @param imaginary
     * @return colour
     */
    public int getColour(int i, double real, double imaginary) {
        if (!this.mcf.isAdvanced()) {
            if (i < this.maxIter) {
                return this.colourTable[i - 1];
            } else {
                return this.maxIterColour;
            }
        } else {
            return this.mcf.getColour(0, 0, real, imaginary);
        }
    }
}
