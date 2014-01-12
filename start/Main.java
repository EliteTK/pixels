/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package start;

import window.Window;

/**
 *
 * @author main
 */
public class Main {
    public static void main(String args[]) {
        Window window = new Window(640, 420, "Testing");
        window.frameBasicInit();
        window.frame().setVisible(true);
        
        
    }
}
