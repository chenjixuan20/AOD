//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package leveretconey.exp6.samplePairs;

import java.util.*;

import leveretconey.dependencyDiscover.Data.DataFrame;

public class SampleTree {
    public SampleTree() {
    }

    public static boolean isEqu(DataFrame data, List<AttributeAndDirection> attrList, int first, int second) {
        for(int i = 0; i < attrList.size(); ++i) {
            int col = ((AttributeAndDirection)attrList.get(i)).attr - 1;
            if (data.get(first, col) != data.get(second, col)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isLess(DataFrame data, List<AttributeAndDirection> attrList, int first, int second) {
        for(int i = 0; i < attrList.size(); ++i) {
            int col = ((AttributeAndDirection)attrList.get(i)).attr - 1;
            int dir = ((AttributeAndDirection)attrList.get(i)).dir;
            if (dir == 1) {
                if (data.get(first, col) < data.get(second, col)) {
                    return true;
                }

                if (data.get(first, col) > data.get(second, col)) {
                    return false;
                }
            } else {
                if (data.get(first, col) > data.get(second, col)) {
                    return true;
                }

                if (data.get(first, col) < data.get(second, col)) {
                    return false;
                }
            }
        }

        return false;
    }

    public static boolean isSplit(DataFrame data, List<AttributeAndDirection> left, List<AttributeAndDirection> right, int first, int second) {
        return isEqu(data, left, first, second) && !isEqu(data, right, first, second);
    }

    public static boolean isSwap(DataFrame data, List<AttributeAndDirection> left, List<AttributeAndDirection> right, int first, int second) {
        if (isLess(data, left, first, second) && isMore(data, right, first, second)) {
            return true;
        } else {
            return isMore(data, left, first, second) && isLess(data, right, first, second);
        }
    }

    public static boolean isOrder(DataFrame data, List<AttributeAndDirection> left, List<AttributeAndDirection> right, int first, int second) {
        if (isLess(data, left, first, second) && isLess(data, right, first, second)) {
            return true;
        } else {
            return isMore(data, left, first, second) && isMore(data, right, first, second);
        }
    }

    public static boolean isMore(DataFrame data, List<AttributeAndDirection> attrList, int first, int second) {
        for(int i = 0; i < attrList.size(); ++i) {
            int col = ((AttributeAndDirection)attrList.get(i)).attr - 1;
            int dir = ((AttributeAndDirection)attrList.get(i)).dir;
            if (dir == 1) {
                if (data.get(first, col) > data.get(second, col)) {
                    return true;
                }

                if (data.get(first, col) < data.get(second, col)) {
                    return false;
                }
            } else {
                if (data.get(first, col) < data.get(second, col)) {
                    return true;
                }

                if (data.get(first, col) > data.get(second, col)) {
                    return false;
                }
            }
        }

        return false;
    }

    public static class SampleTreeNode {
        public DataFrame data;
        public AODCandidate candidate;
        public int split;
        public int swap;
        public int order;
        public double Vg;
        public double LBg;
        public double UBg;
        public Set<Set<Integer>> sets;
        public Set<Set<Integer>> sampleSets;
        public int sampleSize;

        public SampleTreeNode() {
        }

        public SampleTreeNode(DataFrame data, AODCandidate candidate,  Set<Set<Integer>> sets) {
            this.data = data;
            this.candidate = candidate;
            this.sets = sets;
            this.sampleSets = sets;
            this.sampleSize = sets.size();
        }

        public Set<Set<Integer>> getLeftExtentSets(Set<Set<Integer>> sets, DataFrame data) {
            Set<Set<Integer>> leftExtenPairs = new HashSet();
            for(Set<Integer> s : sets ){
                Iterator<Integer> it = s.iterator();
                int first = it.next();
                int second = it.next();
                if (SampleTree.isSplit(data, this.candidate.left, this.candidate.right, first, second)) {
                    leftExtenPairs.add(s);
                }
            }
            return leftExtenPairs;
        }

        public SampleTree.SampleTreeNode getLeftExtendNode(AttributeAndDirection attr) {
            SampleTree.SampleTreeNode result = new SampleTree.SampleTreeNode();
            result.data = this.data;
            result.split = 0;
            result.swap = this.swap;
            result.order = this.order;
            result.sets = this.getLeftExtentSets(this.sampleSets, this.data);
            result.sampleSize = this.sampleSize;
            result.sampleSets = this.sampleSets;
            List<AttributeAndDirection> newLeft = deepClone(this.candidate.left);
            List<AttributeAndDirection> newRight = deepClone(this.candidate.right);
            newLeft.add(attr);
            AODCandidate newCandidate = new AODCandidate(newLeft, newRight);
            result.candidate = newCandidate;
            return result;
        }

        public Set<Set<Integer>> getRightExtendSets(Set<Set<Integer>> sets, DataFrame data) {
            Set<Set<Integer>> rightExtenPairs = new HashSet();
            for(Set<Integer>s : sets ){
                Iterator<Integer> it = s.iterator();
                int first = it.next();
                int second = it.next();
                if (isEqu(data, this.candidate.right, first, second)) {
                    rightExtenPairs.add(s);
                }
            }
            return rightExtenPairs;
        }

        public SampleTree.SampleTreeNode getRightExtendNode(AttributeAndDirection attr) {
            SampleTree.SampleTreeNode result = new SampleTree.SampleTreeNode();
            result.data = this.data;
            result.split = this.split;
            result.swap = this.swap;
            result.order = this.order;
            result.sets = this.getRightExtendSets(this.sampleSets, this.data);
            result.sampleSize = this.sampleSize;
            result.sampleSets = this.sampleSets;
            List<AttributeAndDirection> newLeft = deepClone(this.candidate.left);
            List<AttributeAndDirection> newRight = deepClone(this.candidate.right);
            newRight.add(attr);
            AODCandidate newCandidate = new AODCandidate(newLeft, newRight);
            result.candidate = newCandidate;
            return result;
        }

        public static List<AttributeAndDirection> deepClone(List<AttributeAndDirection> list){
            List<AttributeAndDirection> reslut = new ArrayList<>();
            for(AttributeAndDirection ad : list){
                reslut.add(ad);
            }
            return reslut;
        }

        @Override
        public String toString() {
            return "split: " + split +
                    " swap: " + swap +
                    " order: " + order  +
                    " Vg: " + Vg +
                    " LBg: " + LBg +
                    " UBg: " + UBg  +
                    " candidate:" + candidate.toString() +
                    " sampleSize:" + sampleSize;
        }
    }
}
