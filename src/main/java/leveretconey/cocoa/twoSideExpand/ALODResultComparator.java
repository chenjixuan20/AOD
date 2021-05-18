package leveretconey.cocoa.twoSideExpand;

import java.util.Collection;

import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Dependency.LexicographicalOrderDependency;
import leveretconey.dependencyDiscover.SortedPartition.TwoSideSortedPartition;
import leveretconey.util.Util;

public class ALODResultComparator {
    /**
     *
     * @return [equal count, general count, special count, new count]
     */
    public int[] getEqualGenerakSpecial(
            Collection<LexicographicalOrderDependency> originalResult,
            Collection<LexicographicalOrderDependency> sampleResult){
        int equalCount=0,speciaCount=0,generalCount=0,newCount=0;
        for (LexicographicalOrderDependency originalOd : originalResult) {
            boolean equal=false,special=false,general=false;
            for (LexicographicalOrderDependency  sampleOd: sampleResult) {
                if(sampleOd.equals(originalOd)){
                    equal=true;
                    break;
                }
                if(sampleOd.left.isPrefixOf(originalOd.left)
                  && originalOd.right.isPrefixOf(sampleOd.right)){
                    general=true;
                }
                if(originalOd.left.isPrefixOf(sampleOd.left)
                        && sampleOd.right.isPrefixOf(originalOd.right)){
                    special=true;
                }
            }
            if (equal){
                equalCount++;
            }else if(special){
                speciaCount++;
            }else if (general){
                generalCount++;
            }else {
                newCount++;
            }
        }
        return new int[]{equalCount,speciaCount,generalCount,newCount};
    }

    /**
     *
     * @return [recall, precision]
     */
    public double[] getRecallPrecision(
            DataFrame originalData,Collection<LexicographicalOrderDependency> originalOds,
            DataFrame sampleData,Collection<LexicographicalOrderDependency> sampleOds,
            double errorRateThreshold, boolean isG1){
        int recallCount=0;
        for (LexicographicalOrderDependency od : originalOds) {
            TwoSideSortedPartition sp = new TwoSideSortedPartition(sampleData, od);
            double er;
            if (isG1){
                er = sp.validateForALODWithG1().errorRate;
            }else {
                er = sp.validateForALODWithG3().errorRate;
            }
            Util.out(String.format("%s %.3f",od,er*100));
            if(er < errorRateThreshold){
                recallCount++;
            }
        }
        int precisionCount=0;
        for (LexicographicalOrderDependency od:sampleOds){
            TwoSideSortedPartition sp = new TwoSideSortedPartition(originalData, od);
            double er;
            if (isG1){
                er = sp.validateForALODWithG1().errorRate;
            }else {
                er = sp.validateForALODWithG3().errorRate;
            }
            Util.out(String.format("%s %.3f%%",od,er*100));
            if(er < errorRateThreshold){
                precisionCount++;
            }
        }
        return new double[]{
                (double) recallCount/ originalOds.size(),
                (double) precisionCount/ sampleOds.size(),
        };
    }

}
