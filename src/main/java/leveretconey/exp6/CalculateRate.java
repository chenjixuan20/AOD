package leveretconey.exp6;


import leveretconey.ReturnData;
import leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard;
import leveretconey.cocoa.sample.SubsetSampleALODDiscoverer;
import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Dependency.LexicographicalOrderDependency;
import leveretconey.dependencyDiscover.Discoverer.ALODDiscoverer;
import leveretconey.dependencyDiscover.SortedPartition.ImprovedTwoSideSortedPartition;
import leveretconey.dependencyDiscover.Validator.Result.ValidationResultWithAccurateBound;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static java.lang.StrictMath.max;
import static leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard.ValidatorType.*;

public class CalculateRate {
    public static void main(String[] args) {
        DataFrame data= DataFrame.fromCsv("data/ncvoter 730000 14.csv");

        Map<String, Double> map = new HashMap<>();
        int count = 0;

        BigDecimal sum = new BigDecimal("0.0");
        BigDecimal bigdemical = new BigDecimal("0.001");


        // 原始的发现算法调用
        System.out.println("原始算法： ");
        ALODDiscoverer discoverer =new DFSDiscovererWithMultipleStandard(G1,0.01);
        //对于DFSDiscovererWithMultipleStandard这个类，它实际使用的error rate以上面这行为准，下面这个是没用的（接口太烂）
        Collection<LexicographicalOrderDependency> aods = discoverer.newDiscover(data,0.01).result;
        for (LexicographicalOrderDependency oad : aods) {
            LexicographicalOrderDependency od = LexicographicalOrderDependency.fromString(oad.toString());
            ImprovedTwoSideSortedPartition isp = new ImprovedTwoSideSortedPartition(data,od);
            ValidationResultWithAccurateBound er = isp.validateForALODWithG1();
//            Util.out("oad: " + oad);
//            Util.out("error rate:" + er.errorRate);
            map.put(oad.toString(), er.errorRate);
        }

//        for (Map.Entry<String, Double> entry : map.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//        }

        System.out.println();

        System.out.println("采样结果： ");
        //采样
        discoverer = new SubsetSampleALODDiscoverer(0.02,0.01,0.1);
        //SubsetSampleALODDiscoverer，它实际使用的error rate以下面的为准
        ReturnData re = discoverer.newDiscover(data,0.01);
        Collection<LexicographicalOrderDependency> aods2 = re.result;
        DataFrame subData = re.dataFrame;

        for (LexicographicalOrderDependency oad2 : aods2) {
            LexicographicalOrderDependency od = LexicographicalOrderDependency.fromString(oad2.toString());
            ImprovedTwoSideSortedPartition isp = new ImprovedTwoSideSortedPartition(subData,od);
            ValidationResultWithAccurateBound er = isp.validateForALODWithG1();
//            System.out.println("aod: " + oad2.toString());
//            System.out.println("error rate: " + er.errorRate);
            if(map.get(oad2.toString()) != null){
                double error = map.get(oad2.toString());
//                System.out.println("原始版本error： " + error);
//                System.out.println("抽样error： " + er.errorRate);
                BigDecimal error_ori = new BigDecimal(error);
                BigDecimal error_new = new BigDecimal(er.errorRate);
                if (!(error_ori.compareTo(BigDecimal.ZERO) ==0)){
                    BigDecimal rate = error_ori.subtract(error_new).abs().divide(error_ori, 6, RoundingMode.HALF_UP);
                    System.out.println("error差的绝对值的比值: " + rate);
                    int a = error_ori.subtract(error_new).abs().compareTo(bigdemical);
                    if(a < 0){
                        count++;
                    }
                    sum = sum.add(rate);
                    System.out.println("errorerror差的绝对值的比值的和: " + sum);
                }

            }
        }

        System.out.println("比0.001小的count: " + count);
        System.out.println("size: " + aods.size());
        BigDecimal size = new BigDecimal(aods.size());
        System.out.println();
        System.out.println("差的比值的平均值: " + sum.divide(size, 6, RoundingMode.HALF_UP));


    }
}
