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
        //creating window (view)
        NN_window window = new NN_window();
        //creating neural network (model)
        Network net = new Network();
        //(controler)
        Process process = new Process(net, window);
        //starting thread which will write statistics of training
        process.start();
        window.setVisible(true);
    }
}
