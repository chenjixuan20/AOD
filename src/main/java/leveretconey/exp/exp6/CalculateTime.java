package leveretconey.exp.exp6;

import leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard;
import leveretconey.cocoa.sample.SubsetSampleALODDiscoverer;
import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Dependency.LexicographicalOrderDependency;
import leveretconey.dependencyDiscover.Discoverer.ALODDiscoverer;

import java.util.Collection;

import static leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard.ValidatorType.G1;

public class CalculateTime {
    public static void main(String[] args) {
        DataFrame data= DataFrame.fromCsv("data/echocardiogram.csv");

        System.gc();
        System.out.println("原始算法： ");
        ALODDiscoverer discoverer =new DFSDiscovererWithMultipleStandard(G1,0.001);
        Collection<LexicographicalOrderDependency> aods = discoverer.discover(data,0.01);

        System.gc();
        System.out.println("抽样： ");
        discoverer = new SubsetSampleALODDiscoverer(0.02,0.01,0.1);
        Collection<LexicographicalOrderDependency> aods2 = discoverer.discover(data,0.01);

//        System.out.println("aods大小: " + aods.size());
//        System.out.println("aods2大小: " + aods2.size());
//        int aod_count = 0;
//        int aod2_count = 0;
//        for(LexicographicalOrderDependency aod : aods){
//            if(aods2.contains(aod)){
//                aod_count++;
//            }
//        }
//        System.out.println("aods中有多少被aods2包含: " + aod_count);
//
//        for(LexicographicalOrderDependency aod2 : aods2){
//            if(aods.contains(aod2)){
//                aod2_count++;
//            }
//        }
//        System.out.println("aod2s中有多少被aods包含: " + aod2_count);


    }
}
