/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nn_1;

import java.util.Calendar;
import java.util.Date;
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
    double deltaTreshold;
    double[] weights;
    double[] deltaWeights;
    
    public Neuron(int perviousLayerNeuronsCount, Random rand)
    {
        treshold = randomNumber(rand);
        deltaTreshold = 0.0;
        weights = new double[perviousLayerNeuronsCount];
        deltaWeights = new double[perviousLayerNeuronsCount];
        for (int i = 0; i < perviousLayerNeuronsCount; i++) {
            weights[i] = randomNumber(rand);
            deltaWeights[i] = 0.0;
        }
    }
    
    public double randomNumber(Random rand)
    {
        double min = -0.5;
        double max = 0.5;
        //Random rand = new Random(System.currentTimeMillis());
        double randomNum = rand.nextDouble();
        randomNum = min + randomNum * (max - min);
        return randomNum;
    }
    
}
