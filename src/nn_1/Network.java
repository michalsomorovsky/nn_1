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
import java.util.Random;

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
    ArrayList<double[]> testData;
    int epochCount;
    StringBuffer buffer;
   
    public void setLayers(int inpulLayerSize, int hiddenLayerSize, int outputLayerSize)
    {
        Random rand = new Random(System.currentTimeMillis());
        this.inputLayer = new Neuron[inpulLayerSize];
        this.hiddenLayer = new Neuron[hiddenLayerSize];
        this.outputLayer = new Neuron[outputLayerSize];
        for (int i = 0; i < inputLayer.length; i++) {
            inputLayer[i] = new Neuron(0, rand);
        }
        for (int i = 0; i < hiddenLayer.length; i++) {
            hiddenLayer[i] = new Neuron(inpulLayerSize, rand);
        }
        for (int i = 0; i < outputLayer.length; i++) {
            outputLayer[i] = new Neuron(hiddenLayerSize, rand);
        }        
    }
    
    public void inicializeNetwork(int type) throws FileNotFoundException, IOException
    {
        trainData = new ArrayList();
        FileReader reader = new FileReader(trainFile);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = null;
        char[] chararray;
        double[] nieco;
        switch(type)
        {
            case 1://parity
                //treba upravit!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                while((line = bufferedReader.readLine()) != null)
                {
                    chararray = line.toCharArray();
                    for (char c : chararray) {
                        
                    }
                    trainData.add(new double[] {Double.parseDouble(line.substring(0, 1)), Double.parseDouble(line.substring(1, 2)), Double.parseDouble(line.substring(2))});
                }
                break;
            case 2://other
                break;
            case 0://xor
            default:
                while((line = bufferedReader.readLine()) != null)
                {
                    trainData.add(new double[] {Double.parseDouble(line.substring(0, 1)), Double.parseDouble(line.substring(1, 2)), Double.parseDouble(line.substring(2))});
                }
                break;
        }
        
    }
    
    public void beforeRun() throws FileNotFoundException, IOException
    {
        testData = new ArrayList<>();
        FileReader reader = new FileReader(testFile);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = null;
        while((line = bufferedReader.readLine()) != null)
        {
            testData.add(new double[] {Double.parseDouble(line.substring(0, 1)), Double.parseDouble(line.substring(1))});
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
        return (1 / (1 + Math.exp((-1) * netOutput)));
    }
    
    public double dSigmoid(double netOutput)
    {
        double aktivity = sigmoid(netOutput);
        return aktivity * (1 - aktivity);
    }
    
    public double output()
    {
        return outputLayer[0].activity;
    }
    
    public void feedForward(double[] inputData)
    {
        //setting input values
        for (int i = 0; i < inputLayer.length; i++) {
            inputLayer[i].activity = inputData[i];
            //System.out.println("IL " + i + " act: " + inputLayer[i].activity);
        }
        
        
        //computing activities on neurons of hidden layer
        double sum = 0.0;
        for (int i = 0; i < hiddenLayer.length; i++) {
            for (int j = 0; j < inputLayer.length; j++) {
                sum += inputLayer[j].activity * hiddenLayer[i].weights[j];
                //System.out.println("HL waha " + i + " " + j + " = " + hiddenLayer[i].weights[j]);
            }
            //adding treshold
            sum += hiddenLayer[i].treshold;
            //System.out.println("HL prah " + i + " = " + hiddenLayer[i].treshold);
            hiddenLayer[i].netActivity = sum;
            hiddenLayer[i].activity = sigmoid(sum);
            //System.out.println("HL " + i + " netact: " + hiddenLayer[i].netActivity);
            //System.out.println("HL " + i + " act: " + hiddenLayer[i].activity);
            sum = 0.0;
        }
        
        //computing activities on neurons of output layer
        sum = 0.0;
        for (int i = 0; i < outputLayer.length; i++) {
            for (int j = 0; j < hiddenLayer.length; j++) {
                sum += hiddenLayer[j].activity * outputLayer[i].weights[j];
                //System.out.println("OL waha " + i + " " + j + " = " + outputLayer[i].weights[j]);
            }
            //adding treshold
            sum += outputLayer[i].treshold;
            //System.out.println("OL prah " + i + " = " + outputLayer[i].treshold);
            outputLayer[i].netActivity = sum;
            outputLayer[i].activity = sigmoid(sum);
            //System.out.println("OL " + i + " netact: " + outputLayer[i].netActivity);
            //System.out.println("OL " + i + " act: " + outputLayer[i].activity);
            sum = 0.0;
        }
    }
    
    public void backPropagation(double[] inputData)
    {
        int indexOfLast = inputData.length -1;
        
        //computing error signal on neurons of output layer
        for (int i = 0; i < outputLayer.length; i++) {
            outputLayer[i].errorSignal = dSigmoid(outputLayer[i].netActivity) * (inputData[indexOfLast] - outputLayer[i].activity);
        }
        
        //computing error signal on neurons of hidden layer
        double sum = 0.0;
        /*for (int i = 0; i < hiddenLayer.length; i++) {
            for (int j = 0; j < hiddenLayer[i].weights.length; j++) {
                sum += outputLayer[j].errorSignal * hiddenLayer[i].weights[j];
            }
            hiddenLayer[i].errorSignal = dSigmoid(hiddenLayer[i].netActivity) * sum;
        }*/
        for (int i = 0; i < hiddenLayer.length; i++) {
            for (int j = 0; j < outputLayer.length; j++) {
                sum += outputLayer[j].errorSignal * outputLayer[j].weights[i];
            }
            hiddenLayer[i].errorSignal = dSigmoid(hiddenLayer[i].netActivity) * sum;
        }
        
        //computing changes of weights on neurons of hidden layer
        for (int i = 0; i < outputLayer.length; i++) {
            for (int j = 0; j < hiddenLayer.length; j++) {
                outputLayer[i].deltaWeights[j] = learningRate * outputLayer[i].errorSignal * hiddenLayer[j].activity + momentum * outputLayer[i].deltaWeights[j];
                outputLayer[i].weights[j] += outputLayer[i].deltaWeights[j];
                //System.out.println("DELTAOLw " + i + " " + j + " " + outputLayer[i].deltaWeights[j]);
                //System.out.println("OLw " + i + " " + j + " " + outputLayer[i].weights[j]);
            }
            outputLayer[i].deltaTreshold = learningRate * outputLayer[i].errorSignal + momentum * outputLayer[i].deltaTreshold;
            outputLayer[i].treshold += outputLayer[i].deltaTreshold;
            //ystem.out.println("OLt " + i + " " + outputLayer[i].treshold);
        }
        //computing changes of weights on neurons of input layer
        for (int i = 0; i < hiddenLayer.length; i++) {
            for (int j = 0; j < inputLayer.length; j++) {
                hiddenLayer[i].deltaWeights[j] = learningRate * hiddenLayer[i].errorSignal * inputLayer[j].activity + momentum * hiddenLayer[i].deltaWeights[j];
                hiddenLayer[i].weights[j] += hiddenLayer[i].deltaWeights[j];
                //System.out.println("HLw " + i + " " + j + " " + hiddenLayer[i].weights[j]);
            }
            hiddenLayer[i].deltaTreshold = learningRate * hiddenLayer[i].errorSignal + momentum * hiddenLayer[i].deltaTreshold;
            hiddenLayer[i].treshold += hiddenLayer[i].deltaTreshold;
            //System.out.println("HLt " + i + " " + hiddenLayer[i].treshold);
        }
    }
    
    public double roundOutput(double output)
    {
        if(output >= 0.7 && output <= 1.0) return 1;
        else if(output >= 0.0 && output <= 0.3) return 0;
        else return output;
    }
    
    public double mse(double[] data)
    {
        double partial = 0.0;
        for (int i = 0; i < outputLayer.length; i++) {
            partial += Math.pow((outputLayer[i].activity - data[i]), 2);
        }
        return partial/2;
    }
    
    public void train(int pocetEpoch)
    {
        double uspesnost = 0.0;
        double mse = 0.0;
        for(int i=0; i<epochCount; i++)
        {
            Collections.shuffle(trainData);
            for (int j = 0; j < trainData.size(); j++) {
                feedForward(trainData.get(j));
                backPropagation(trainData.get(j));
                //buffer.append(output());
                if(trainData.get(j)[2] == roundOutput(output())) uspesnost +=1;
                mse += mse(new double[] {trainData.get(j)[2]});
                System.out.println("/////////////////");
                
            }
            System.out.println("Uspesnost: "+uspesnost/4);
            System.out.println("MSE: " + mse/trainData.size());
            uspesnost = 0.0;
            mse = 0.0;
        }
    }
    
    public void test()
    {
        for (int i = 0; i < testData.size(); i++) {
            feedForward(testData.get(i));
            System.out.println(roundOutput(output()));
        }
        
    }
}
