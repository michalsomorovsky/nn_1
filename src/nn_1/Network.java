/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nn_1;

import java.io.File;

/**
 *
 * @author m.somorovsky
 */


public class Network {
    float learningRate;
    float momentum;
    Neuron[] inputLayer;
    Neuron[] hiddenLayer;
    Neuron[] outputLayer;
    File trainFile;
    File testFile;

    public float getLearningRate() {
        return learningRate;
    }

    public float getMomentum() {
        return momentum;
    }
    
    public void setTestFile(File testFile)
    {
        this.testFile = testFile;
    }
    
    public void setTrainFile(File trainFile)
    {
        this.trainFile = trainFile;
    }
    
    public void setMomentum(float momentum)
    {
        this.momentum = momentum;
    }
    
    public void setLearningRate(float learningRate)
    {
        this.learningRate = learningRate;
    }
    
    public void feedForward(){};
    
    public void backPropagation(){};
    
    public void train(int pocetEpoch)
    {
        for(int i=0; i<pocetEpoch; i++)
        {
            feedForward();
            backPropagation();
        }
    }
    
    public void test()
    {
        feedForward();
    }
}
