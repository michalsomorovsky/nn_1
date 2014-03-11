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
    Neuron[] inputLayer;
    Neuron[] hiddenLayer;
    Neuron[] outputLayer;
    File trainFile;
    File testFile;
    ArrayList<double[]> trainData;
    ArrayList<double[]> testData;
    int epochCount;
    StringBuffer buffer;
    Queue<String> fifo;
   
    public Network()
    {
        fifo = new ConcurrentLinkedQueue<>();
    }
    
    public void setLayers(int inpulLayerSize, int hiddenLayerSize, int outputLayerSize)
    {
        buffer = new StringBuffer();
        
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
    
    public void beforeRun(int type) throws FileNotFoundException, IOException
    {
        
        FileReader reader = new FileReader(testFile);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = null;
        char[] chararray;
        String[] stringarray;
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
                /*while((line = bufferedReader.readLine()) != null)
                {
                    double[] lineArray = new double[7];
                    stringarray = line.split(",");
                    int i=0;
                    for (String c : stringarray) {
                        if(i>=4)
                        {
                            switch(c)
                            {
                                case "Iris-setosa":
                                    if(i==4) lineArray[i] = 1.0;
                                    else lineArray[i] = 0.0;
                                    break;
                                case "Iris-versicolor":
                                    if(i==5) lineArray[i] = 1.0;
                                    else lineArray[i] = 0.0;
                                    break;
                                case "Iris-virginica":
                                    if(i==6) lineArray[i] = 1.0;
                                    else lineArray[i] = 0.0;
                                    break;
                            }
                        }
                        else lineArray[i] = Double.parseDouble(c);
                        i++;
                    }
                    testData.add(lineArray);
                }*/
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
    
    private double sigmoid(double netOutput)
    {
        return (1 / (1 + Math.exp((-1) * netOutput)));
    }
    
    private double dSigmoid(double netOutput)
    {
        double aktivity = sigmoid(netOutput);
        return aktivity * (1 - aktivity);
    }
    
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
    
    private double roundOutput(double output)
    {
        if(output >= 0.7 && output <= 1.0) return 1;
        else if(output >= 0.0 && output <= 0.3) return 0;
        else return output;
    }
    
    private double mse(double[] data)
    {
        double partial = 0.0;
        for (int i = 0; i < outputLayer.length; i++) {
            partial += Math.pow((outputLayer[i].activity - data[i]), 2);
        }
        return partial/2;
    }
    
    public void train(double stopCondition)
    {
        double uspesnost = 0.0;
        double mse = 0.0;
        Random rand = new Random(System.currentTimeMillis());
        int trainDataLength = trainData.get(0).length - 1;
        for(int i=0; i<epochCount; i++)
        {
            Collections.shuffle(trainData, rand);
            for (int j = 0; j < trainData.size(); j++) {
                feedForward(trainData.get(j));
                backPropagation(new double[] {trainData.get(j)[trainDataLength]});
                //buffer.append(output());
                /*if(trainData.get(j)[trainDataLength] == roundOutput(output(0))) uspesnost++;
                mse += mse(new double[] {trainData.get(j)[trainDataLength]});*/
            }
            for (int j = 0; j < trainData.size(); j++) {
                feedForward(trainData.get(j));
                if(trainData.get(j)[trainDataLength] == roundOutput(output(0))) uspesnost++;
                mse += mse(new double[] {trainData.get(j)[trainDataLength]});
            }
            fifo.offer("Uspesnost: "+uspesnost/trainData.size() + "\n" + "MSE: " + mse/trainData.size() +"\n"+"Epocha: " + i+"\n");
            /*synchronized (fifo)
            {
                fifo.notifyAll();
            }*/
            
            /*System.out.println("Uspesnost: "+uspesnost/trainData.size());
            System.out.println("MSE: " + mse/trainData.size());
            System.out.println("Epocha: " + i);*/
            if((mse/trainData.size()) <= stopCondition) break;
            uspesnost = 0.0;
            mse = 0.0;
        }
    }
    
    public void trainIsis(double stopCondition)
    {
        double uspesnost = 0.0;
        double mse = 0.0;
        Random rand = new Random(System.currentTimeMillis());        
        for(int i=0; i<epochCount; i++)
        {
            Collections.shuffle(trainData, rand);
            
            //trainDataSubset.add(trainData.get(i))
            for (int j = 0; j < trainData.size(); j++) {
                feedForward(trainData.get(j));
                backPropagation(new double[] {trainData.get(j)[4], trainData.get(j)[5], trainData.get(j)[6]});
                //buffer.append(output());
                if(trainData.get(j)[4] == roundOutput(output(0)) && trainData.get(j)[5] == roundOutput(output(1)) && trainData.get(j)[6] == roundOutput(output(2))) uspesnost +=1;
                mse += mse(new double[] {trainData.get(j)[4], trainData.get(j)[5], trainData.get(j)[6]});
                //System.out.println("/////////////////");
                
            }
            fifo.add("Uspesnost: "+uspesnost/trainData.size() + "\n" + "MSE: " + mse/trainData.size() +"\n"+"Epocha: " + i+"\n");
            /*System.out.println("Uspesnost: "+uspesnost/trainData.size());
            System.out.println("MSE: " + mse/trainData.size());
            System.out.println("Epocha: " + i);*/
            if((mse/trainData.size()) <= stopCondition) break;
            uspesnost = 0.0;
            mse = 0.0;
        }
    }
    
    public void test(int type) {
        String output = new String();
        int testDataLength = testData.get(0).length - 1;
        output += "Vypocitany vystup \t zelany vystup\n";
        double uspesnost =0.0;
        for (int i = 0; i < testData.size(); i++) {
            feedForward(testData.get(i));

            switch (type) {
                case 0:
                case 1:
                    output += roundOutput(output(0)) + "\t" + testData.get(i)[testDataLength] + "\n";
                    if(roundOutput(output(0)) == testData.get(i)[testDataLength]) uspesnost++;
                    break;
                case 2:
                    String computedOutput = new String();
                    String desiredOutput = new String();
                    for (int j = 0; j < this.outputLayer.length; j++) {
                        computedOutput += roundOutput(output(j));
                        //output += roundOutput(output(j)) + " ";
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
                    output += "\n";
                    break;
            }

            //System.out.println("");
        }
        output += "Uspesnost testu: " + uspesnost/testData.size() + "\n";
        fifo.offer(output);
    }
}
