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
        window.setEpochCount(10000);
        window.setStopCondition(0.009);
        
    }
    public void setDefautlParita()
    {
        window.setNumberOfInputNeurons(8);
        window.setNumberOfHiddenNeurons(8);
        window.setNumberOfOutputNeurons(1);
        window.setMomentum(80);
        window.setLearningRate(10);
        window.clearFileTextFields();
        window.setRunButtonDisabledabled();
        window.setEpochCount(100000);
        window.setStopCondition(0.0001);
    }
    public void setDefautlNieco()
    {
        window.setNumberOfInputNeurons(4);
        window.setNumberOfHiddenNeurons(4);
        window.setNumberOfOutputNeurons(3);
        window.setMomentum(70);
        window.setLearningRate(10);
        window.clearFileTextFields();
        window.setRunButtonDisabledabled();
        window.setEpochCount(10000);
        window.setStopCondition(0.0001);
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
            InicializationAndTraining iat = new InicializationAndTraining();
            OutputPrinting op = new OutputPrinting();
            if(window.isReadyToTrain())
            {
               
                    //pp.start();
                    neuralNetwork.setLayers(window.getNumberOfInputNeurons(), window.getNumberOfHiddenNeurons(), window.getNumberOfOutputNeurons());
                    neuralNetwork.setMomentum((double)window.getMomentum()/100);
                    neuralNetwork.setLearningRate((double)window.getLearningRate()/100);
                    neuralNetwork.setEpochCount(window.getEpochCount());
                    iat.start();
                    op.start();
                    /*switch(window.getSelectedRadioButton())
                    {
                        case 1:
                            pp.start();
                            pp2.start();
                            neuralNetwork.inicializeNetwork(1);
                            neuralNetwork.train(window.getStopCondition());
                            //while(neuralNetwork.buffer.length() > 0) window.printText("lama\n");
                            
                            break;
                        case 2:
                            neuralNetwork.inicializeNetwork(2);
                            neuralNetwork.trainIsis(window.getStopCondition());
                            break;
                        case 0:
                        default:
                            neuralNetwork.inicializeNetwork(0);
                            neuralNetwork.train(window.getStopCondition());
                            break;
                    }*/
                   
                    
               
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
            Running run = new Running();
            if(window.isReadyToTest())
            {
                run.start();
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
                neuralNetwork.setTestFile(window.getSelectedTestFile());
            } 
        }
    }
    
    class InicializationAndTraining extends Thread
    {
        @Override
        public void run() {
            switch (window.getSelectedRadioButton()) {
                case 1:
                    try {
                        neuralNetwork.inicializeNetwork(1);
                    } catch (IOException ex) {
                        Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    neuralNetwork.train(window.getStopCondition()); 
                    break;
                case 2:
                    try {
                        neuralNetwork.inicializeNetwork(2);
                    } catch (IOException ex) {
                        Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    neuralNetwork.trainIsis(window.getStopCondition());
                    break;
                case 0:
                default:
                    try {
                        neuralNetwork.inicializeNetwork(0);
                    } catch (IOException ex) {
                        Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    neuralNetwork.train(window.getStopCondition());
                    break;
            }
            window.setRunButtonEnabled();
        }
    }
    
    class OutputPrinting extends Thread
    {
        @Override
        public void run()
        {
            while(true) {
                window.printText(neuralNetwork.fifo.poll());
            }
            
        }
    }
    
    class Running extends Thread {

        @Override
        public void run() {
            try {
                switch (window.getSelectedRadioButton()) {
                    case 1:
                        neuralNetwork.beforeRun(1);
                        neuralNetwork.test(1);
                        break;
                    case 2:
                        neuralNetwork.beforeRun(2);
                        neuralNetwork.test(2);
                        break;
                    case 0:
                    default:
                        neuralNetwork.beforeRun(0);
                        neuralNetwork.test(0);
                        break;
                }

            } catch (IOException ex) {
                Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
}
