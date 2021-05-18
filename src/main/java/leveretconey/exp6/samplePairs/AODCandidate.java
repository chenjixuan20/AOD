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

    public static void main(String[] args) {
        AttributeAndDirection A = new AttributeAndDirection(1,AttributeAndDirection.UP);
        AttributeAndDirection B = new AttributeAndDirection(1,AttributeAndDirection.UP);
        AttributeAndDirection C = new AttributeAndDirection(2,AttributeAndDirection.UP);
        AttributeAndDirection D = new AttributeAndDirection(2,AttributeAndDirection.UP);
        AttributeAndDirection E = new AttributeAndDirection(3,AttributeAndDirection.UP);
        System.out.println(A==B);
        System.out.println(A.equals(B));
        List<AttributeAndDirection> left = new ArrayList<>();
        List<AttributeAndDirection> right = new ArrayList<>();
        List<AttributeAndDirection> left1 = new ArrayList<>();
        List<AttributeAndDirection> left2 = new ArrayList<>();
        List<AttributeAndDirection> right1 = new ArrayList<>();
        List<AttributeAndDirection> right2 = new ArrayList<>();
        System.out.println(left.containsAll(left1));
        left.add(A);
        left1.add(B);
        right.add(C);
        right1.add(D);
        left2.add(D);
        right2.add(E);
        AODCandidate test = new AODCandidate(left, right);
        AODCandidate test1 = new AODCandidate(left1, right1);
        AODCandidate test2 = new AODCandidate(left2, right2);
        Set<AODCandidate> aod = new HashSet<>();
        Set<AODCandidate> aod1 = new HashSet<>();
        aod.add(test);
        aod.add(test2);
        aod1.add(test1);
        System.out.println(aod.containsAll(aod1));
    }

}