/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nn_1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author m.somorovsky
 */


public class Network {
    double learningRate;
    double momentum;
    Neuron[] inputLayer;
    Neuron[] hiddenLayer;
    Neuron[] outputLayer;
    File trainFile;
    File testFile;
    ArrayList<double[]> trainData;
    int epochCount;
   
    public void setLayers(int inpulLayerSize, int hiddenLayerSize, int outputLayerSize)
    {
        this.inputLayer = new Neuron[inpulLayerSize];
        this.hiddenLayer = new Neuron[hiddenLayerSize];
        this.outputLayer = new Neuron[outputLayerSize];
        for (int i = 0; i < inputLayer.length; i++) {
            inputLayer[i] = new Neuron(hiddenLayerSize);
        }
        for (int i = 0; i < hiddenLayer.length; i++) {
            hiddenLayer[i] = new Neuron(outputLayerSize);
        }
        for (int i = 0; i < outputLayer.length; i++) {
            outputLayer[i] = new Neuron(0);
        }        
    }
    
    public void inicializeNetwork() throws FileNotFoundException, IOException
    {
        trainData = new ArrayList();
        FileReader reader = new FileReader(trainFile);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = null;
        while((line = bufferedReader.readLine()) != null)
        {
            trainData.add(new double[] {Double.parseDouble(line.substring(0, 1)), Double.parseDouble(line.substring(1, 2)), Double.parseDouble(line.substring(2))});
        }
    }
    
    public double getLearningRate() 
    {
        return learningRate;
    }

    public double getMomentum() 
    {
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
    
    public void setMomentum(double momentum)
    {
        this.momentum = momentum;
    }
    
    public void setLearningRate(double learningRate)
    {
        this.learningRate = learningRate;
    }
    
    public void setEpochCount(int count)
    {
        this.epochCount = count;
    }
    
    public double sigmoid(double netOutput)
    {
        return (1 / (1 + Math.exp(netOutput)));
    }
    
    public double dSigmoid(double netOutput)
    {
        double aktivity = sigmoid(netOutput);
        return aktivity * (1 - aktivity);
    }
    
    public void feedForward(double[] inputData)
    {
        for (int i = 0; i < inputLayer.length; i++) {
            inputLayer[i].activity = inputData[i];
        }
        double sum = 0.0;
        for (int i = 0; i < hiddenLayer.length; i++) {
            for (int j = 0; j < inputLayer.length; j++) {
                sum += inputLayer[j].activity * inputLayer[j].weights[i];
            }
            hiddenLayer[i].netActivity = sum;
            hiddenLayer[i].activity = sigmoid(sum);
        }
        sum = 0.0;
        for (int i = 0; i < outputLayer.length; i++) {
            for (int j = 0; j < hiddenLayer.length; j++) {
                sum += hiddenLayer[j].activity * hiddenLayer[j].weights[i];
            }
            outputLayer[i].netActivity = sum;
            outputLayer[i].activity = sigmoid(sum);
        }
    }
    
    public void backPropagation(double[] inputData)
    {
        int indexOfLast = inputData.length -1;
        for (int i = 0; i < outputLayer.length; i++) {
            outputLayer[i].errorSignal = dSigmoid(outputLayer[i].netActivity) * (inputData[indexOfLast] - outputLayer[i].activity);
        }
        double sum = 0.0;
        for (int i = 0; i < hiddenLayer.length; i++) {
            for (int j = 0; j < hiddenLayer[i].weights.length; j++) {
                sum += outputLayer[j].errorSignal * hiddenLayer[i].weights[j];
            }
            hiddenLayer[i].errorSignal = dSigmoid(hiddenLayer[i].netActivity) * sum;
        }
        
        for (int i = 0; i < hiddenLayer.length; i++) {
            for (int j = 0; j < outputLayer.length; j++) {
                hiddenLayer[i].deltaWeights[j] = learningRate * outputLayer[j].errorSignal * outputLayer[j].activity + momentum * hiddenLayer[i].deltaWeights[j];
                hiddenLayer[i].weights[j] += hiddenLayer[i].deltaWeights[j];
            }
        }
        for (int i = 0; i < inputLayer.length; i++) {
            for (int j = 0; j < hiddenLayer.length; j++) {
                inputLayer[i].deltaWeights[j] = learningRate * hiddenLayer[j].errorSignal * hiddenLayer[j].activity + momentum * inputLayer[i].deltaWeights[j];
                inputLayer[i].weights[j] += inputLayer[i].deltaWeights[j];
            }
        }
    }
    
    public void train(int pocetEpoch)
    {
        Collections.shuffle(trainData);
        for(int i=0; i<pocetEpoch; i++)
        {
            for (int j = 0; j < trainData.size(); j++) {
                feedForward(trainData.get(j));
                backPropagation(trainData.get(j));
            }
        }
    }
    
    public void test()
    {
        //feedForward();
    }
}
