/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nn_1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author m.somorovsky
 */
public class Process {
    
    Network neuralNetwork;
    NN_window window;
    
    public Process(Network neuralNetwork, NN_window window)
    {
        this.neuralNetwork = neuralNetwork;
        this.window = window;
        this.window.addProblemSelectorActionListener(new ProblemSelectorActionListener());
        this.window.addTrainActionListener(new TrainingActionListener());
        this.window.addRunActionListener(new RuningActionListener());
        this.setDefautlXOR();
    }
    
    public void setDefautlXOR()
    {
        window.setNumberOfInputNeurons(2);
        window.setNumberOfHiddenNeurons(2);
        window.setNumberOfOutputNeurons(1);
        
    }
    public void setDefautlParita()
    {
        window.setNumberOfInputNeurons(8);
        window.setNumberOfHiddenNeurons(8);
        window.setNumberOfOutputNeurons(1);
        window.setMomentum(1);
    }
    public void setDefautlNieco()
    {
        window.setNumberOfInputNeurons(50);
        window.setNumberOfHiddenNeurons(20);
        window.setNumberOfOutputNeurons(1);
        window.setMomentum(100);
    }
    
    class ProblemSelectorActionListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e) {
            switch(window.getSelectedRadioButton())
            {
                case 0:
                    setDefautlXOR();
                    break;
                case 1:
                    setDefautlParita();
                    break;
                case 2:
                    setDefautlNieco();
                    break;
                default:
                    setDefautlXOR();
                    break;
            }
        }
    }
    
    class TrainingActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Geronimo");
        }
    }
    
    class RuningActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Geronimo2");
        }
    }
}
