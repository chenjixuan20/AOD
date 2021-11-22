package leveretconey;

import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Dependency.LexicographicalOrderDependency;

import java.util.*;

public class ReturnData {
    public Collection<LexicographicalOrderDependency> result;
    public DataFrame dataFrame;

    public ReturnData(Collection<LexicographicalOrderDependency> result, DataFrame dataFrame){
        this.result = result;
        this.dataFrame = dataFrame;
    }

    public static class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }
  }

}
