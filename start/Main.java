package start;

import graphics.mandelbrot.Mandelbrot;
import window.Window;

/**
 *
 * @author main
 */
public class Main {

    public static void main(String args[]) {
        Window window = new Window(600, 600, "Mandelbrot");
        window.frameBasicInit();
        window.frame().setVisible(true);
        
        Mandelbrot mand = new Mandelbrot(window, new ColourFunc(), 600, 600, -0.5, 0.0, 0.007, 50, false);
        mand.draw();
    }
}
