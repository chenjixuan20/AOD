package leveretconey.exp1;

import leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard;
import leveretconey.cocoa.ranking.LODRFClassSquareSumOverAttriCount;
import leveretconey.cocoa.ranking.LODRankingFunction;
import leveretconey.cocoa.sample.SubsetSampleALODDiscoverer;
import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Dependency.LexicographicalOrderDependency;
import leveretconey.dependencyDiscover.Discoverer.ALODDiscoverer;
import leveretconey.dependencyDiscover.SortedPartition.ImprovedTwoSideSortedPartition;
import leveretconey.dependencyDiscover.Validator.Result.ValidationResultWithAccurateBound;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard.ValidatorType.*;


public class calcudateTimeForExp1 {
    public static void main(String[] args) {
        DataFrame data = DataFrame.fromCsv("data/exp1/fd 15.csv");

        //G1
        ALODDiscoverer discoverer =new DFSDiscovererWithMultipleStandard(G1,0.0001);
        //对于DFSDiscovererWithMultipleStandard这个类，它实际使用的error rate以上面这行为准，下面这个是没用的（接口太烂）
        Collection<LexicographicalOrderDependency> aods = discoverer.discover(data, 0.001);
        LODRankingFunction rankingFunction=new LODRFClassSquareSumOverAttriCount();
        System.out.println(aods);
        for (LexicographicalOrderDependency oad : aods) {
            LexicographicalOrderDependency od = LexicographicalOrderDependency.fromString(oad.toString());
            ImprovedTwoSideSortedPartition isp = new ImprovedTwoSideSortedPartition(data,od);
            ValidationResultWithAccurateBound er = isp.validateForALODWithG1();
            oad.rank_measure =   new BigDecimal(String.valueOf(rankingFunction.getRanking(od, isp)));
            oad.error_rate =   new BigDecimal(String.valueOf(er.errorRate));
        }

        ArrayList<LexicographicalOrderDependency> aodlist = new ArrayList<>();
        for(LexicographicalOrderDependency aod: aods){
            aodlist.add(aod);
        }
//        aodlist.sort((r1, r2) -> r2.rank_measure.compareTo(r1.rank_measure));
//        aodlist.sort((r1, r2) -> r1.error_rate.compareTo(r2.error_rate));
        aodlist.sort((r1, r2) -> r1.getALLSize()-r2.getALLSize());
        System.out.println("------------");
        int index = 0;
        for(LexicographicalOrderDependency aod: aodlist){
            index++;
            if(index <= 10){
//                System.out.println(aod);
                System.out.println(aod + "  " + aod.error_rate);

            }
//            System.out.println(aod + "  " + aod.rank_measure);
        }

    }


}
