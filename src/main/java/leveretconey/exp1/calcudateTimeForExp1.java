package leveretconey.exp1;

import leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard;
import leveretconey.cocoa.sample.SubsetSampleALODDiscoverer;
import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Dependency.LexicographicalOrderDependency;
import leveretconey.dependencyDiscover.Discoverer.ALODDiscoverer;


import static leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard.ValidatorType.*;


public class calcudateTimeForExp1 {
    public static void main(String[] args) {
        DataFrame data = DataFrame.fromCsv("data/intData/Horse 300 29.csv");

        //G1
        ALODDiscoverer discoverer =new DFSDiscovererWithMultipleStandard(G1,0.001);
        //对于DFSDiscovererWithMultipleStandard这个类，它实际使用的error rate以上面这行为准，下面这个是没用的（接口太烂）
        discoverer.discover(data, 0.001);


        //G3
        discoverer =new DFSDiscovererWithMultipleStandard(G3,0.01);
        discoverer.discover(data, 0.01);
    }


}
