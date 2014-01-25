package start;

import graphics.mandelbrot.Mandelbrot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import window.Window;

/**
 *
 * @author main
 */
public class Main implements KeyListener {

    static double posx = 0.0, posy = 0.0, pixelStep = 0.007;
    static int maxIter = 50;
    static boolean event = true;

    public static void main(String args[]) {
        Window window = new Window(600, 600, "Mandelbrot");
        window.frameBasicInit();
        window.frame().setVisible(true);

        window.addKeyListener(new Main());

        Mandelbrot mand = new Mandelbrot(window, new ColourFunc(), 600, 600, posx, posy, pixelStep, maxIter, false);

        while (true) {
            mand.draw();
            if (event) {
                mand.updateViewport(posx, posy, pixelStep, maxIter);
                event = false;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'd':
                posx += pixelStep * 10;
                event = true;
                break;
            case 'w':
                posy += pixelStep * 10;
                event = true;
                break;
            case 'a':
                posx -= pixelStep * 10;
                event = true;
                break;
            case 's':
                posy -= pixelStep * 10;
                event = true;
                break;
            case '+':
                pixelStep /= 1.1;
                event = true;
                break;
            case '-':
                pixelStep *= 1.1;
                event = true;
                break;
            case ',':
                maxIter -= 10;
                event = true;
                break;
            case '.':
                maxIter += 10;
                event = true;
                break;
        }
    }
}
