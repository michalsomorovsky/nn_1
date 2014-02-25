/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nn_1;

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

    public float getLearningRate() {
        return learningRate;
    }

    public float getMomentum() {
        return momentum;
    }
    
    public void feedForward(){};
    
    public void backPropagation(){};
}
