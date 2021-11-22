package leveretconey.exp2and3;

import leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard;
import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Discoverer.ALODDiscoverer;
import leveretconey.dependencyDiscover.SortedPartition.ImprovedTwoSideSortedPartition;

import static leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard.ValidatorType.G1;

public class NPressForG1 {
    public static void main(String[] args) {
        DataFrame data = DataFrame.fromCsv("data/exp1/NCV/NCV 1000 19.csv");

        ImprovedTwoSideSortedPartition.g1IncrementalUseStateCompaction=false;
        ImprovedTwoSideSortedPartition.segmentTreeThreshold = 0;
        ALODDiscoverer discoverer = new DFSDiscovererWithMultipleStandard(G1,0.01);
        discoverer.discover(data,0.01);
    }
}
