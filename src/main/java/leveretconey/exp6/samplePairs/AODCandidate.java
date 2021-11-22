package leveretconey.exp6.samplePairs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AODCandidate {
    public List<AttributeAndDirection> left;
    public List<AttributeAndDirection> right;

    public AODCandidate(List<AttributeAndDirection> left, List<AttributeAndDirection> right) {
        this.left = left;
        this.right = right;
    }

    public List<Integer> getExistAttr(){
        List<Integer> result = new ArrayList<>();
        for(AttributeAndDirection ad : left){
            result.add(ad.attr);
        }
        for(AttributeAndDirection ad : right){
            result.add(ad.attr);
        }
        return result;
    }

    public List<Integer> getIntRight(){
        List<Integer> result = new ArrayList<>();
        for(AttributeAndDirection ad : this.right){
            result.add(ad.attr);
        }
        return result;
    }

    public List<Integer> getIntLeft(){
        List<Integer> result = new ArrayList<>();
        for(AttributeAndDirection ad : this.left){
            result.add(ad.attr);
        }
        return result;
    }


    @Override
    public String toString() {
        return left.toString() + "|->" + right.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof AODCandidate) {
            AODCandidate aod = (AODCandidate) obj;
            return left.equals(aod.left) && right.equals(aod.right);
        }
        return false;
    }


}