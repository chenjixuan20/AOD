package leveretconey.exp2and3;

import leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard;
import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Discoverer.ALODDiscoverer;

import java.util.ArrayList;
import java.util.List;

import static leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard.ValidatorType.G1;

public class AOD {
    public static void main(String[] args) {
        DataFrame data = DataFrame.fromCsv("data/test1.csv");

        // 原始的发现算法调用
        DFSDiscovererWithMultipleStandard discoverer =new DFSDiscovererWithMultipleStandard(G1,0.01);
        //对于DFSDiscovererWithMultipleStandard这个类，它实际使用的error rate以上面这行为准，下面这个是没用的（接口太烂）
        discoverer.discover(data, 0.01);

        List<Integer> n = new ArrayList<>();
        System.out.println(DFSDiscovererWithMultipleStandard.count);

    }
}
