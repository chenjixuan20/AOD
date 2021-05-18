package leveretconey;

import java.util.Collection;
import java.util.List;

import leveretconey.chino.dataStructures.DataFrame;
import leveretconey.chino.dataStructures.ODCandidate;
import leveretconey.chino.dataStructures.ODTree;
import leveretconey.chino.discoverer.ChinoPlus;
import leveretconey.chino.util.Util;
import leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard;
import leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard.ValidatorType;
import leveretconey.dependencyDiscover.Dependency.LexicographicalOrderDependency;

public class ThesisMain {
    public static void main(String[] args) {
        String algName = args[0];
        String filePath = args[1];
        if (algName.equals("findod")){
            DataFrame data =DataFrame.fromCsv(filePath);
            ChinoPlus chinoPlus = new ChinoPlus(true);
            Collection<ODCandidate> result =
                    chinoPlus.discover(data).getAllOdsOrderByBFS();

            for (ODCandidate odCandidate : result) {
                Util.out(odCandidate);
            }
        }else if(algName.equals("findaod")){
            leveretconey.dependencyDiscover.Data.DataFrame data =
                    leveretconey.dependencyDiscover.Data.DataFrame.fromCsv(filePath);
            String standard = args[2];
            ValidatorType type = standard.equals("g1") ? ValidatorType.G1 : ValidatorType.G3;
            double errorRateThreshold = Double.parseDouble(args[3]);
            DFSDiscovererWithMultipleStandard discoverer
                    = new DFSDiscovererWithMultipleStandard(type, errorRateThreshold);
            Collection<LexicographicalOrderDependency> result
                    = discoverer.discover(data, errorRateThreshold);
            for (LexicographicalOrderDependency dependency : result) {
                String s = dependency.toString();
                s = s.replace("<=","↑");
                s = s.replace(">=","↓");
                Util.out(s);
            }
        }else {
            Util.out("Invalid algorithm name. Should be findod or findaod");
        }
    }
}
