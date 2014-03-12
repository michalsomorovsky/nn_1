/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nn_1;

import java.util.Random;

/**
 *
 * @author m.somorovsky
 */
public class Neuron {
    //output activity of neuron
    double activity;
    //potential of neuron
    double netActivity;
    double errorSignal;
    double treshold;
    //pervious change of treshold
    double deltaTreshold;
    double[] weights;
    //pervious changes of weights
    double[] deltaWeights;
    
    public Neuron(int perviousLayerNeuronsCount, Random rand)
    {
        //inicialization of treshold with random number
        treshold = randomNumber(rand);
        deltaTreshold = 0.0;
        weights = new double[perviousLayerNeuronsCount];
        deltaWeights = new double[perviousLayerNeuronsCount];
        //inicialization of weights with random number
        for (int i = 0; i < perviousLayerNeuronsCount; i++) {
            weights[i] = randomNumber(rand);
            deltaWeights[i] = 0.0;
        }
    }
    
    private double randomNumber(Random rand)
    {
        //interval for generating random number
        double min = -0.5;
        double max = 0.5;
        //generating random number
        double randomNum = rand.nextDouble();
        //normalization to chosen interval
        randomNum = min + randomNum * (max - min);
        return randomNum;
    }
    
}
