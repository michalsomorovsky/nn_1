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
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author m.somorovsky
 */


public class Network {
    double learningRate;
    double momentum;
    //layer of input "neurons"
    Neuron[] inputLayer;
    //layer of hidden neurons
    Neuron[] hiddenLayer;
    //layer of output neurons
    Neuron[] outputLayer;
    //file with train data
    File trainFile;
    //file with test data
    File testFile;
    ArrayList<double[]> trainData;
    ArrayList<double[]> testData;
    int epochCount;
    //queue for output statistics
    Queue<String> fifo;
   
    public Network()
    {
        fifo = new ConcurrentLinkedQueue<>();
    }
    
    //setting up network
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
    //preparing train data from selected file
    public void inicializeNetwork(int type) throws FileNotFoundException, IOException
    {
        trainData = new ArrayList();
        FileReader reader = new FileReader(trainFile);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = null;
        char[] chararray;
        String[] stringarray;
        switch(type)
        {
            case 1://parity
                while((line = bufferedReader.readLine()) != null)
                {
                    double[] lineArray = new double[9];
                    chararray = line.toCharArray();
                    int i=0;
                    for (char c : chararray) {
                        lineArray[i] = (double)Character.getNumericValue(c);
                        i++;
                    }
                    trainData.add(lineArray);
                }
                break;
            case 2://Iris
                while((line = bufferedReader.readLine()) != null)
                {
                    double[] lineArray = new double[7];
                    stringarray = line.split(",");
                    int i=0;
                    for (String c : stringarray) {
                        if(i>=4)
                        {
                            //coding output
                            //Iris-setosa = 100
                            //Iris-versicolor = 010
                            //Iris-virginica = 001
                            switch(c)
                            {
                                case "Iris-setosa":
                                    lineArray[4] = 1.0;
                                    break;
                                case "Iris-versicolor":
                                    lineArray[5] = 1.0;
                                    break;
                                case "Iris-virginica":
                                    lineArray[6] = 1.0;
                                    break;
                            }
                        }
                        else lineArray[i] = Double.parseDouble(c);
                        i++;
                    }
                    trainData.add(lineArray);
                }
                //spliting train data and choosing data for testing
                Collections.shuffle(trainData);
                testData = new ArrayList<>();
                for (int j = 0; j < 15; j++) {
                    testData.add(trainData.get(j));                        
                }
                for (int j = 0; j < 15; j++) {
                    trainData.remove(j);
                }
                break;
            case 0://xor
                while((line = bufferedReader.readLine()) != null)
                {
                    trainData.add(new double[] {Double.parseDouble(line.substring(0, 1)), Double.parseDouble(line.substring(1, 2)), Double.parseDouble(line.substring(2))});
                }
                break;
        }
    }
    
    //preparing test data from selected file
    public void beforeRun(int type) throws FileNotFoundException, IOException
    {
        FileReader reader = new FileReader(testFile);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = null;
        char[] chararray;
        switch(type)
        {
            case 1://parity
                testData = new ArrayList<>();
                while((line = bufferedReader.readLine()) != null)
                {
                    double[] lineArray = new double[9];
                    chararray = line.toCharArray();
                    int i=0;
                    for (char c : chararray) {
                        lineArray[i] = (double)Character.getNumericValue(c);
                        i++;
                    }
                    testData.add(lineArray);
                }
                break;
            case 2://other
                break;
            case 0://xor
            default:
                testData = new ArrayList<>();
                while((line = bufferedReader.readLine()) != null)
                {
                    testData.add(new double[] {Double.parseDouble(line.substring(0, 1)), Double.parseDouble(line.substring(1, 2)), Double.parseDouble(line.substring(2))});
                }
                break;
        }
        
    }
    // <editor-fold defaultstate="collapsed" desc="getters / setters"> 
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
    //</editor-fold>
    
    //sigmoid activation function
    private double sigmoid(double netOutput)
    {
        return (1 / (1 + Math.exp((-1) * netOutput)));
    }
    
    //derivation of sigmoid activation function
    private double dSigmoid(double netOutput)
    {
        double aktivity = sigmoid(netOutput);
        return aktivity * (1 - aktivity);
    }
    
    //output of network from selected output neuron
    private double output(int index)
    {
        return outputLayer[index].activity;
    }
    
    
    public Boolean isFifoEmpty()
    {
        return fifo.isEmpty();
    }
    
    public String fifoPoll()
    {
        return fifo.poll();
    }
    
    public Queue getFifo()
    {
        return fifo;
    }
    
    public void feedForward(double[] inputData)
    {
        //setting input values
        for (int i = 0; i < inputLayer.length; i++) {
            inputLayer[i].activity = inputData[i];
        }
        
        //computing activities on neurons of hidden layer
        double sum = 0.0;
        for (int i = 0; i < hiddenLayer.length; i++) {
            for (int j = 0; j < inputLayer.length; j++) {
                sum += inputLayer[j].activity * hiddenLayer[i].weights[j];
            }
            //adding treshold
            sum += hiddenLayer[i].treshold;
            hiddenLayer[i].netActivity = sum;
            hiddenLayer[i].activity = sigmoid(sum);
            sum = 0.0;
        }
        
        //computing activities on neurons of output layer
        sum = 0.0;
        for (int i = 0; i < outputLayer.length; i++) {
            for (int j = 0; j < hiddenLayer.length; j++) {
                sum += hiddenLayer[j].activity * outputLayer[i].weights[j];
            }
            //adding treshold
            sum += outputLayer[i].treshold;
            outputLayer[i].netActivity = sum;
            outputLayer[i].activity = sigmoid(sum);
            sum = 0.0;
        }
    }
    
    public void backPropagation(double[] inputData)
    {
        int indexOfLast = inputData.length -1;
        
        //computing error signal on neurons of output layer
        for (int i = 0; i < outputLayer.length; i++) {
            outputLayer[i].errorSignal = dSigmoid(outputLayer[i].netActivity) * (inputData[i] - outputLayer[i].activity);
        }
        
        //computing error signal on neurons of hidden layer
        double sum = 0.0;
        for (int i = 0; i < hiddenLayer.length; i++) {
            for (int j = 0; j < outputLayer.length; j++) {
                sum += outputLayer[j].errorSignal * outputLayer[j].weights[i];
            }
            hiddenLayer[i].errorSignal = dSigmoid(hiddenLayer[i].netActivity) * sum;
            sum = 0.0;
        }
        
        //computing changes of weights on neurons of hidden layer
        for (int i = 0; i < outputLayer.length; i++) {
            for (int j = 0; j < hiddenLayer.length; j++) {
                outputLayer[i].deltaWeights[j] = learningRate * outputLayer[i].errorSignal * hiddenLayer[j].activity + momentum * outputLayer[i].deltaWeights[j];
                outputLayer[i].weights[j] += outputLayer[i].deltaWeights[j];
            }
            outputLayer[i].deltaTreshold = learningRate * outputLayer[i].errorSignal + momentum * outputLayer[i].deltaTreshold;
            outputLayer[i].treshold += outputLayer[i].deltaTreshold;
        }
        //computing changes of weights on neurons of input layer
        for (int i = 0; i < hiddenLayer.length; i++) {
            for (int j = 0; j < inputLayer.length; j++) {
                hiddenLayer[i].deltaWeights[j] = learningRate * hiddenLayer[i].errorSignal * inputLayer[j].activity + momentum * hiddenLayer[i].deltaWeights[j];
                hiddenLayer[i].weights[j] += hiddenLayer[i].deltaWeights[j];
            }
            hiddenLayer[i].deltaTreshold = learningRate * hiddenLayer[i].errorSignal + momentum * hiddenLayer[i].deltaTreshold;
            hiddenLayer[i].treshold += hiddenLayer[i].deltaTreshold;
        }
    }
    
    private double roundOutput(double output)
    {
        if(output >= 0.7 && output <= 1.0) return 1;
        else if(output >= 0.0 && output <= 0.3) return 0;
        else return output;
    }
    
    //computing of mean square error
    private double mse(double[] data)
    {
        double partial = 0.0;
        for (int i = 0; i < outputLayer.length; i++) {
            partial += Math.pow((outputLayer[i].activity - data[i]), 2);
        }
        return partial/2;
    }
    
    //training of network
    public void train(double stopCondition)
    {
        double successRate = 0.0;
        double mse = 0.0;
        Random rand = new Random(System.currentTimeMillis());
        int trainDataLength = trainData.get(0).length - 1;
        for(int i=0; i<epochCount; i++)
        {
            //shuffle train data
            Collections.shuffle(trainData, rand);
            for (int j = 0; j < trainData.size(); j++) {
                feedForward(trainData.get(j));
                backPropagation(new double[] {trainData.get(j)[trainDataLength]});
            }
            //computing of success rate and mse
            for (int j = 0; j < trainData.size(); j++) {
                feedForward(trainData.get(j));
                if(trainData.get(j)[trainDataLength] == roundOutput(output(0))) successRate++;
                mse += mse(new double[] {trainData.get(j)[trainDataLength]});
            }
            //printing statistics
            fifo.offer("Uspesnost: "+(successRate/trainData.size())*100 + "%\n" + "MSE: " + mse/trainData.size() +"\n"+"Epocha: " + i+"\n");
            
            if((mse/trainData.size()) <= stopCondition) break;
            successRate = 0.0;
            mse = 0.0;
        }
    }
    
    //training of network with iris data
    public void trainIsis(double stopCondition)
    {
        double successRate = 0.0;
        double mse = 0.0;
        Random rand = new Random(System.currentTimeMillis());        
        for(int i=0; i<epochCount; i++)
        {
            Collections.shuffle(trainData, rand);
            for (int j = 0; j < trainData.size(); j++) {
                feedForward(trainData.get(j));
                backPropagation(new double[] {trainData.get(j)[4], trainData.get(j)[5], trainData.get(j)[6]});              
            }
            
            for (int j = 0; j < trainData.size(); j++) {
                feedForward(trainData.get(j));
                if(trainData.get(j)[4] == roundOutput(output(0)) && trainData.get(j)[5] == roundOutput(output(1)) && trainData.get(j)[6] == roundOutput(output(2))) successRate +=1;
                mse += mse(new double[] {trainData.get(j)[4], trainData.get(j)[5], trainData.get(j)[6]});
            }
            fifo.add("Uspesnost: "+(successRate/trainData.size())*100 + "%\n" + "MSE: " + mse/trainData.size() +"\n"+"Epocha: " + i+"\n");
            if((mse/trainData.size()) <= stopCondition) break;
            successRate = 0.0;
            mse = 0.0;
        }
    }
    
    //testing of network
    public void test(int type) {
        String output = new String();
        int testDataLength = testData.get(0).length - 1;
        output += "Vypocitany vystup \t zelany vystup\n";
        double successRate =0.0;
        for (int i = 0; i < testData.size(); i++) {
            feedForward(testData.get(i));

            switch (type) {
                case 0:
                case 1:
                    output += roundOutput(output(0)) + "\t\t" + testData.get(i)[testDataLength] + "\n";
                    if(roundOutput(output(0)) == testData.get(i)[testDataLength]) successRate++;
                    break;
                case 2:
                    String computedOutput = new String();
                    String desiredOutput = new String();
                    for (int j = 0; j < this.outputLayer.length; j++) {
                        computedOutput += roundOutput(output(j));
                    }
                    switch (computedOutput) {
                        case "1.00.00.0":
                            output += "Iris-setosa";
                            break;
                        case "0.01.00.0":
                            output += "Iris-versicolor";
                            break;
                        case "0.00.01.0":
                            output += "Iris-virginica";
                            break;
                        default:
                            output += "nezname";
                    }
                    output += "\t\t";
                    for (int j = 0; j < this.outputLayer.length; j++) {
                        desiredOutput += testData.get(i)[j + 4];
                    }
                    switch (desiredOutput) {
                        case "1.00.00.0":
                            output += "Iris-setosa";
                            break;
                        case "0.01.00.0":
                            output += "Iris-versicolor";
                            break;
                        case "0.00.01.0":
                            output += "Iris-virginica";
                            break;
                        default:
                            output += "nezname";
                    }
                    if(computedOutput.equals(desiredOutput)) successRate++;
                    output += "\n";
                    break;
            }
        }
        output += "Uspesnost testu: " + (successRate/testData.size())*100 + "%\n";
        fifo.offer(output);
    }
}
