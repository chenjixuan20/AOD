# AOD

code for Discovery of Approximate Lexicographical Order Dependencies

## Program

you can find our code in

```
src/main/java/leveretconey/cocoa
```

As you can see clearly that we have encapsulated the different components

## Experiment

You can use the code in the corresponding folder to reproduce our experiment, such as

```
leveretconey/exp1
```

We have also integrated the dataset used in the experiment into the corresponding folder, such as

```
data/exp1/FLI
```

Note that the data set we used is a processed dataset. You can use the tools in "leveretconey/pre" to process the dataset to obtain a dataset that meets the requirements of the program.

You can change the parameters in the function to set different error rate, it's easy, we won't introduce it too much

```
 //AOD_1
ALODDiscoverer discoverer =new DFSDiscovererWithMultipleStandard(type:G1,errorRate:0.001);

//AOD_3
discoverer =new DFSDiscovererWithMultipleStandard(type:G3,errorRate:0.001);

//sample
discoverer = new SubsetSampleALODDiscoverer(alpha:0.02,e:0.01,sigma:0.1);
```