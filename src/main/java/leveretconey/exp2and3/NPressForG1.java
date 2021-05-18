package leveretconey.exp2and3;

import leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard;
import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Discoverer.ALODDiscoverer;
import leveretconey.dependencyDiscover.SortedPartition.ImprovedTwoSideSortedPartition;

import static leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard.ValidatorType.G1;

public class NPressForG1 {
    public static void main(String[] args) {
        DataFrame data = DataFrame.fromCsv("data/exp1/ncvoter 1000 19.csv");

        //状态压缩相关
        //关状态压缩，默认值是true
        ImprovedTwoSideSortedPartition.g1IncrementalUseStateCompaction=false;
        //默认值是32，改成0应该会让优化效果更明显（我脑补的）
        ImprovedTwoSideSortedPartition.segmentTreeThreshold = 0;
        //注意状态压缩这个优化只是针对g1而言的
        ALODDiscoverer discoverer = new DFSDiscovererWithMultipleStandard(G1,0.01);
        //对于DFSDiscovererWithMultipleStandard这个类，它实际使用的error rate以上面这行为准，下面这个是没用的（接口太烂）
        discoverer.discover(data,0.01);
    }
}
