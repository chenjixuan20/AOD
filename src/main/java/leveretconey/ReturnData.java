package leveretconey;

import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Dependency.LexicographicalOrderDependency;

import java.util.Collection;

public class ReturnData {
    public Collection<LexicographicalOrderDependency> result;
    public DataFrame dataFrame;

    public ReturnData(Collection<LexicographicalOrderDependency> result, DataFrame dataFrame){
        this.result = result;
        this.dataFrame = dataFrame;
    }

}
