package leveretconey.exp.exp6.samplePairs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import leveretconey.util.Timer;
import leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard;
import leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard.ValidatorType;
import leveretconey.cocoa.sample.SubsetSampleALODDiscoverer;
import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Dependency.LexicographicalOrderDependency;
import leveretconey.dependencyDiscover.Discoverer.ALODDiscoverer;
import leveretconey.dependencyDiscover.SortedPartition.ImprovedTwoSideSortedPartition;
import leveretconey.dependencyDiscover.Validator.Result.ValidationResultWithAccurateBound;

public class CheckThesis {

    public static void main(String[] args) {
        DataFrame data = DataFrame.fromCsv("data/exp6/FLI 100K.csv");
//        DataFrame data = DataFrame.fromCsv("data/exp6/test.csv");
        System.out.println("data.size: " + data.getTupleCount());
        Map<MapKey, Double> aodMap = new HashMap();
        System.out.println("原始算法： ");
        ALODDiscoverer discoverer = new DFSDiscovererWithMultipleStandard(ValidatorType.G1, 0.01);
        Collection<LexicographicalOrderDependency> aods = discoverer.newDiscover(data, 0.01).result;
        System.out.println(aods.toString());

        for (LexicographicalOrderDependency aod : aods){
            LexicographicalOrderDependency od = LexicographicalOrderDependency.fromString(aod.toString());
            ImprovedTwoSideSortedPartition isp = new ImprovedTwoSideSortedPartition(data,od);
            ValidationResultWithAccurateBound er = isp.validateForALODWithG1();
            aodMap.put(new MapKey(aod.left, aod.right), er.errorRate);
        }

        Timer timer = new Timer();
        SubsetSampleALODDiscoverer discoverer1 = new SubsetSampleALODDiscoverer(0.02, 0.01, 0.1);
        Set<Set<Integer>> sets = discoverer1.newDiscoverPlusSet(data, 0.01);
        System.out.println(sets);
        System.out.println("sample time set: " + (double)timer.getTimeUsedAndReset() / 1000.0 + "s");
        Sample.Experimental(data, aodMap, sets, aods);
    }
}
