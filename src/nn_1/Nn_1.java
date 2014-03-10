/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nn_1;

/**
 *
 * @author m.somorovsky
 */
public class Nn_1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NN_window window = new NN_window();
        Network net = new Network();
        Process process = new Process(net, window);
        process.start();
        window.setVisible(true);
    }
    
}
