package leveretconey.exp6.samplePairs;

import java.util.List;

public class FdInfo {
    List<Integer> left;
    int right;

    public FdInfo(List<Integer> left, int right){
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return left+"->"+right;
    }
}
