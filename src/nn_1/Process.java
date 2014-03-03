/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nn_1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

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
        this.window.addFileChooserActionListener(new FileChooserActionListener());
        this.window.addFileChooserActionListener2(new FileChooserActionListener2());
        this.setDefautlXOR();
        
    }
    
    public void setDefautlXOR()
    {
        window.setNumberOfInputNeurons(2);
        window.setNumberOfHiddenNeurons(2);
        window.setNumberOfOutputNeurons(1);
        window.setMomentum(50);
        window.setLearningRate(50);
        window.clearFileTextFields();
        window.setRunButtonDisabledabled();
        window.setEpochCount(1);
        window.setStopCondition(0.009);
        
    }
    public void setDefautlParita()
    {
        window.setNumberOfInputNeurons(8);
        window.setNumberOfHiddenNeurons(8);
        window.setNumberOfOutputNeurons(1);
        window.setMomentum(50);
        window.setLearningRate(50);
        window.clearFileTextFields();
        window.setRunButtonDisabledabled();
        window.setEpochCount(1);
    }
    public void setDefautlNieco()
    {
        window.setNumberOfInputNeurons(50);
        window.setNumberOfHiddenNeurons(20);
        window.setNumberOfOutputNeurons(1);
        window.setMomentum(50);
        window.setLearningRate(50);
        window.clearFileTextFields();
        window.setRunButtonDisabledabled();
        window.setEpochCount(1);
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
            ProgressPrinting pp = new ProgressPrinting();
            
            if(window.isReadyToTrain())
            {
                try {
                    neuralNetwork.setLayers(window.getNumberOfInputNeurons(), window.getNumberOfHiddenNeurons(), window.getNumberOfOutputNeurons());
                    neuralNetwork.setMomentum((double)window.getMomentum()/100);
                    neuralNetwork.setLearningRate((double)window.getLearningRate()/100);
                    neuralNetwork.setEpochCount(window.getEpochCount());
                    switch(window.getSelectedRadioButton())
                    {
                        case 1:
                            neuralNetwork.inicializeNetwork(1);
                            break;
                        case 2:
                            neuralNetwork.inicializeNetwork(2);
                            break;
                        case 0:
                        default:
                            neuralNetwork.inicializeNetwork(0);
                            break;
                    }
                    //pp.start();
                    neuralNetwork.train(window.getStopCondition());
                    window.setRunButtonEnabled();
                } catch (IOException ex) {
                    Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                window.showErrorMessage();
            }
            /*try {
                //pp.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
    }
    
    class RuningActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(window.isReadyToTest())
            {
                try {
                    switch(window.getSelectedRadioButton())
                    {
                        case 1:
                            neuralNetwork.beforeRun(1);
                            break;
                        case 2:
                            neuralNetwork.beforeRun(2);
                            break;
                        case 0:
                        default:
                            neuralNetwork.beforeRun(0);
                            break;
                    }
                    
                } catch (IOException ex) {
                    Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                }
                neuralNetwork.test();
                System.out.println("Geronimo2");
            }
            else
            {
                window.showErrorMessage();
            }
        }
    }
    
    class FileChooserActionListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) 
            {
                System.out.println(window.getSelectedTrainFile().getName());
                neuralNetwork.setTrainFile(window.getSelectedTrainFile());
            } 
        }
    }
    
    class FileChooserActionListener2 implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) 
            {
                System.out.println(window.getSelectedTestFile().getName());
                neuralNetwork.setTestFile(window.getSelectedTestFile());
            } 
        }
    }
    
    class ProgressPrinting extends Thread
    {
        @Override
        public void run()
        {
            while(true)
            {
                System.out.println("alive");
                try {
                    neuralNetwork.buffer.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
