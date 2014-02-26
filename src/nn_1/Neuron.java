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
    double activity;
    double netActivity;
    double errorSignal;
    double treshold;
    double[] weights;
    double[] deltaWeights;
    
    public Neuron(int nextLayerNeuronsCount)
    {
        treshold = randomNumber();
        weights = new double[nextLayerNeuronsCount];
        deltaWeights = new double[nextLayerNeuronsCount];
        for (int i = 0; i < nextLayerNeuronsCount; i++) {
            weights[i] = randomNumber();
            deltaWeights[i] = 0.0;
        }
    }
    
    public double randomNumber()
    {
        Random rand = new Random();
        double randomNum = rand.nextDouble();
        randomNum = -2.5 + randomNum * ((2.5+2.5));
        return randomNum;
    }
    
}
