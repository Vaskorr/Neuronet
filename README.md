# Neuronet
Java class for creating simple neural networks.

Neuronet.java Documentation

Usage:

### Creating a new object:
Neuronet neuronet = new Neuronet ();

### Initialization of the neural network:
neuronet.init (int [] lays, dE, dA);
- lays - an array of integer values defining the number of layers and the number of neurons in each of them.
- dE is the learning rate of the neural network. (normal value is 0.5-0.9)
- dA is the moment required to overcome local minima. (normal value is 0.5-0.9)

### Random filling of neural network weights:
neuronet.fillWeights ();

### Automatic training of the neural network:
neuronet.autotrain (int epoch_count, int[][] base);
- epoch_count - the number of epochs of training the neural network
- base - database for training

### Database structure:

The database is a two-dimensional array containing the number of elements corresponding to the number of sets.
Each set contains a set of data and a reference answer. The amount of input must match
the number of neurons on the input layer.
