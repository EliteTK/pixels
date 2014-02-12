/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package start;

import graphics.mandelbrot.MandelbrotColourtableFunction;
import java.awt.Color;
import java.util.Random;

/**
 *
 * @author main
 */
public class Argument implements MandelbrotColourtableFunction {

    Random rand = new Random();

    @Override
    public int getColour(int iter, int maxIter, double real, double imaginary) {
        return Color.HSBtoRGB((float)(Math.tan(real/imaginary) / (2f * Math.PI)), 1.0f, 1.0f);
    }
    
    @Override
    public boolean isAdvanced() {
        return true;
    }
}
