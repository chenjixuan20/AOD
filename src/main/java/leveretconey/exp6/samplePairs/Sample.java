package leveretconey.exp6.samplePairs;

import leveretconey.chino.util.Timer;
import leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard;
import leveretconey.cocoa.sample.SubsetSampleALODDiscoverer;
import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Dependency.LexicographicalOrderDependency;
import leveretconey.dependencyDiscover.Discoverer.ALODDiscoverer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard.ValidatorType.G1;

public class Sample {
    static List<AODCandidate> sampleReslut = new ArrayList<>();
    static int minimalCheckTime = 0;

    public static boolean isEqu(DataFrame data, List<AttributeAndDirection> attrList, int first, int second){
        for(int i = 0; i < attrList.size(); i++){
            int col = attrList.get(i).attr - 1;
            if(data.get(first,col) == data.get(second,col)){
                continue;
            }else {
                return false;
            }
        }
        return true;
    }

    public static boolean isEquInt(DataFrame data, List<Integer> attrList, int first, int second){
        for(int i = 0; i < attrList.size(); i++){
            int col = attrList.get(i) - 1;
            if(data.get(first,col) == data.get(second,col)){
                continue;
            }else {
                return false;
            }
        }
        return true;
    }

    public static boolean isLess(DataFrame data, List<AttributeAndDirection> attrList, int first, int second){
        for(int i = 0; i < attrList.size(); i++){
            int col = attrList.get(i).attr - 1;
            int dir = attrList.get(i).dir;
            if(dir == AttributeAndDirection.UP){
                if(data.get(first,col) < data.get(second,col)){
                    return true;
                }else if(data.get(first,col) > data.get(second,col)){
                    return false;
                }else {
                    continue;
                }
            }else {
                if(data.get(first,col) > data.get(second,col)){
                    return true;
                }else if(data.get(first,col) < data.get(second,col)){
                    return false;
                }else {
                    continue;
                }
            }
        }
        return false;
    }

    public static boolean isSplit(DataFrame data, List<AttributeAndDirection> left, List<AttributeAndDirection> right,
                                  int first, int second){
        if(isEqu(data,left,first,second) && !isEqu(data,right,first,second)){
            return true;
        }
        return false;
    }

    public static boolean isSplitInt(DataFrame data, List<Integer> left, List<Integer> right,
                                  int first, int second){
        if(isEquInt(data,left,first,second) && !isEquInt(data,right,first,second)){
            return true;
        }
        return false;
    }

    public static boolean isSwap(DataFrame data, List<AttributeAndDirection> left, List<AttributeAndDirection> right,
                                 int first, int second){
        if(isLess(data,left,first,second) && isMore(data,right,first,second)){
            return true;
        }
        if(isMore(data,left,first,second) && isLess(data,right,first,second)){
            return true;
        }
        return false;
    }

    public static boolean isOrder(DataFrame data, List<AttributeAndDirection> left, List<AttributeAndDirection> right,
                                 int first, int second){
        if(isLess(data,left,first,second) && isLess(data,right,first,second)){
            return true;
        }
        if(isMore(data,left,first,second) && isMore(data,right,first,second)){
            return true;
        }
        return false;
    }

    public static boolean isMore(DataFrame data, List<AttributeAndDirection> attrList, int first, int second){
        for(int i = 0; i < attrList.size(); i++){
            int col = attrList.get(i).attr - 1;
            int dir = attrList.get(i).dir;
            if(dir == AttributeAndDirection.UP){
                if(data.get(first,col) > data.get(second,col)){
                    return true;
                }else if(data.get(first,col) < data.get(second,col)){
                    return false;
                }else {
                    continue;
                }
            }else {
                if(data.get(first,col) < data.get(second,col)){
                    return true;
                }else if(data.get(first,col) > data.get(second,col)){
                    return false;
                }else {
                    continue;
                }
            }

        }
        return false;
    }

    public static void Experimental(DataFrame data,Map<MapKey, Double> aodMap, Set<Set<Integer>> sets,
                                    Collection<LexicographicalOrderDependency> aods){
        int count = 0;
        Timer timer = new Timer();
        Set<MapKey> set = aodMap.keySet();
        for(MapKey key : set){
            List<AttributeAndDirection> leftList = key.left;
            List<AttributeAndDirection> rightList = key.right;
            int split  = 0;
            int swap = 0;
            for(Set<Integer> s : sets){
                Iterator<Integer> it = s.iterator();
                int first = it.next();
                int second = it.next();
//            前等后不等，split
                if(isSplit(data,leftList,rightList,first,second)){
                    split++;
                }
//            前小后大，前大后小，swap
                if(isSwap(data,leftList,rightList,first,second)){
                    swap++;
                }
            }

            System.out.println("-----------");
            System.out.println(key.toString());
            System.out.println("split: " + split);
            System.out.println("swap: " + swap);
            double g1Sample =(double) (split + swap) / sets.size();
            double g1 = aodMap.get(key);
            System.out.println("g1: " + g1);
            System.out.println("g1Sample: " + g1Sample);
            if(Math.abs(g1-g1Sample) <= 0.001) count++;
            System.out.println("count: "+count);
            System.out.println("---------");
        }

        int aod_size = aods.size();

        double rate = (double)count/aod_size;

        System.out.println("time:"+timer.getTimeUsedAndReset()/1000.0+"s");
        System.out.println("rate:" + rate);
    }

    public static void Compute(SampleTree.SampleTreeNode node){
//        System.out.println(node);
//        System.out.println("setsSize: "+node.sets.size());
        int split = node.split;
        int swap = node.swap;
        int order = node.order;
//        int split = 0;
//        int swap = 0;
//        int order = 0;
        for(Set<Integer> s : node.sets){
//        for(Set<Integer> s : node.sampleSets){
            Iterator<Integer> it = s.iterator();
            int first = it.next();
            int second = it.next();
//          前等后不等，split
            if(isSplit(node.data,node.candidate.left,node.candidate.right,first,second)){
                split++;
            }
//          前小后大，前大后小，swap
            if(isSwap(node.data,node.candidate.left,node.candidate.right,first,second)){
                swap++;
            }
//          前小后小，前大后大，order
            if(isOrder(node.data,node.candidate.left,node.candidate.right,first,second)){
                order++;
            }
        }
        node.split = split;
        node.swap = swap;
        node.order = order;
        node.Vg = (double) (node.split + node.swap)/node.sampleSize;
        node.LBg = (double) node.swap/node.sampleSize;
        node.UBg = (double) 1 - (double)node.order/node.sampleSize;
    }

    public static void ExtendAttr(List<Integer> exAttrs, AODCandidate candidate, DataFrame data, Set<Set<Integer>> sets,  Map<Integer, List<List<Integer>>> map){
        Stack<ExtendInfo> extendInfoStack = new Stack<>();
        List<List<AttributeAndDirection>> minimalRight = new ArrayList<>();
        List<AttributeAndDirection> fdLeft = candidate.right;
//        System.out.println("exAttrs: " + exAttrs);
        ExtendInfo exInfo = new ExtendInfo(exAttrs, fdLeft);
        extendInfoStack.push(exInfo);
        while(!extendInfoStack.isEmpty()){
            ExtendInfo info = extendInfoStack.pop();
            List<Integer> nowExAttrs = info.extendAttrs;
            int exSize = nowExAttrs.size();
            List<AttributeAndDirection> nowLeft = info.left;
            for(int i = 0; i < exSize*2; i++){
                int exAttr;
                if(i < exSize){
                    exAttr = nowExAttrs.get(i);
                }else{
                    exAttr = nowExAttrs.get(i-exSize);
                }
                List<Integer> list = getInt(nowLeft);
                list.add(exAttr);
//                if(isAttributeListMinimalInt(list,data,sets)){
                if(isAttributeListMinimalFDInt(list,map)){
                    List<AttributeAndDirection> newLeft = deepCloneAtt(nowLeft);
                    AttributeAndDirection attr;
                    if(i < exSize){
                        attr = new AttributeAndDirection(exAttr,AttributeAndDirection.UP);
                    }else {
                        attr = new AttributeAndDirection(exAttr,AttributeAndDirection.DOWN);
                    }
                    newLeft.add(attr);
                    List<Integer> newExAttrs = deepClone(nowExAttrs);
                    if(i < exSize){
                        newExAttrs.remove(i);
                    }else {
                        newExAttrs.remove(i-exSize);
                    }
                    ExtendInfo child = new ExtendInfo(newExAttrs, newLeft);
                    extendInfoStack.push(child);
                }
            }
            if(nowExAttrs.size() == 0){
                List<AttributeAndDirection> nowRight = deepCloneAtt(nowLeft);
//                System.out.println("nowRight: "+nowRight);
                minimalRight.add(nowRight);
            }
        }
        for(List<AttributeAndDirection> right : minimalRight){
            AODCandidate aod = new AODCandidate(candidate.left, right);
//            System.out.println("Extend: " + aod);
            sampleReslut.add(aod);
        }
    }

    public static boolean isAttributeListMinimalInt(List<Integer> list, DataFrame data, Set<Set<Integer>> sets){
        Timer timer = new Timer();
        int length = list.size();
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        for(int i = 0; i < length; i++) {
            if (i < length - 1) {
                left.add(list.get(i));
            } else {
                right.add(list.get(i));
            }
        }
        for(Set<Integer> s : sets) {
            Iterator<Integer> it = s.iterator();
            int first = it.next();
            int second = it.next();
            if(isSplitInt(data,left,right,first,second)){
                minimalCheckTime += timer.getTimeUsedAndReset();
                return true;
            }
        }
        minimalCheckTime += timer.getTimeUsedAndReset();
        return false;
    }

    public static boolean isAODMinimal(AODCandidate candidate){
        for(AODCandidate r : sampleReslut){
            List<AttributeAndDirection> r_left = r.left;
            List<AttributeAndDirection> r_right = r.right;
            List<AttributeAndDirection> c_left = candidate.left;
            List<AttributeAndDirection> c_right = candidate.right;
            if(r_right == c_right && c_left.containsAll(r_left)){
                return false;
            }
            if(r_left == c_left && r_right.containsAll(c_right)){
                return false;
            }
            if(r_left == c_left && c_right.containsAll(r_right)){
                sampleReslut.remove(r);
            }
            if(r_right == c_right && r_left.containsAll(c_left)){
                sampleReslut.remove(r);
            }
        }
        return true;
    }

    public static boolean isAttributeListMinimal(List<AttributeAndDirection> list, DataFrame data, Set<Set<Integer>> sets){
        Timer timer = new Timer();
        int length = list.size();
        List<AttributeAndDirection> left = new ArrayList<>();
        List<AttributeAndDirection> right = new ArrayList<>();
        for(int i = 0; i < length; i++) {
            if (i < length - 1) {
                left.add(list.get(i));
            } else {
                right.add(list.get(i));
            }
        }
        for(Set<Integer> s : sets) {
            Iterator<Integer> it = s.iterator();
            int first = it.next();
            int second = it.next();
            if(isSplit(data,left,right,first,second)){
                minimalCheckTime += timer.getTimeUsedAndReset();
                return true;
            }
        }
        minimalCheckTime += timer.getTimeUsedAndReset();
        return false;
    }

    public static List<FdInfo> getFD(String path) throws IOException {
        File csv = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(csv));
        String lineDta = "";
        List<FdInfo> list = new ArrayList<>();

        while ((lineDta = br.readLine())!= null){
            char[] fdchar = lineDta.toCharArray();
            int len = fdchar.length;
            int index = 0;
            for(int i = 0; i < len; i++){
                if(fdchar[i] == '-'){
                    index = i;
                    break;
                }
            }
            char[] leftChar = new char[index];
            for(int i = 0; i < index; i++){
                leftChar[i] = fdchar[i];
            }
            String[] leftS = String.valueOf(leftChar).split(",");
            List<Integer> left = new ArrayList<>();
            for(String s: leftS){
                int att = StringToInt(s);
                left.add(att);
            }

            int right = 0;
            while (index+2 < len){
                right = right*10 + (fdchar[index+2]-'0');
                index++;
            }
            list.add(new FdInfo(left,right));
        }
        return list;
    }

    public static Map<Integer, List<List<Integer>>> getFdMap(List<FdInfo> list){
        Map<Integer, List<List<Integer>>> fdmap = new HashMap<>();
        for(FdInfo fd: list){
            int key = fd.right;
            if(!fdmap.keySet().contains(key)){
                List<List<Integer>> value = new ArrayList<>();
                value.add(fd.left);
                fdmap.put(key,value);
            }else {
                List<List<Integer>> value = fdmap.get(key);
                value.add(fd.left);
                fdmap.put(key,value);
            }
        }
        return fdmap;
    }

    public static boolean isAttributeListMinimalFDInt(List<Integer> list, Map<Integer, List<List<Integer>>> map){
        int length = list.size();
        List<Integer> left_check = new ArrayList<>();
        int right_check = 0;
        for(int i = 0; i < length; i++) {
            if (i < length - 1) {
                left_check.add(list.get(i));
            } else {
                right_check = list.get(i);
            }
        }
        if(!map.keySet().contains(right_check)){
            return true;
        }
        List<List<Integer>> fdlefts = map.get(right_check);
        for(List<Integer> fdleft : fdlefts){
            if(fdleft.size() == 1 && fdleft.get(0) == 0){
                return false;
            }
            if(left_check.containsAll(fdleft)){
                return false;
            }
        }
        return true;
    }

    public static boolean isAttributeListMinimalFD(List<AttributeAndDirection> list, Map<Integer, List<List<Integer>>> map){
        int length = list.size();
        List<Integer> left_check = new ArrayList<>();
        int right_check = 0;
        for(int i = 0; i < length; i++) {
            if (i < length - 1) {
                left_check.add(list.get(i).attr);
            } else {
                right_check = list.get(i).attr;
            }
        }
//        System.out.println("list: "+list);
//        System.out.println("left_check: "+left_check);
//        System.out.println("right_check: "+right_check);
        if(!map.keySet().contains(right_check)){
            return true;
        }
        List<List<Integer>> fdlefts = map.get(right_check);
//        System.out.println("fdlefts: " + fdlefts);
        for(List<Integer> fdleft : fdlefts){
            if(fdleft.size() == 1 && fdleft.get(0) == 0){
                return false;
            }
            if(left_check.containsAll(fdleft)){
                return false;
            }
        }
        return true;
    }

    public static int StringToInt(String s){
        int result = 0;
        char[] chars = s.toCharArray();
        for(int i = 0; i < chars.length; i++){
            result = result * 10 + (chars[i]-'0');
        }
        return result;
    }

    public static void Search(SampleTree.SampleTreeNode node, double e, int attrCount, Map<Integer, List<List<Integer>>> map){
        Compute(node);
        double Vg = node.Vg;
        double LBg = node.LBg;
        double UBg = node.UBg;
//        System.out.println(node);
        if(Vg <= e){
//            System.out.println("Vg<=e");
            if(isAODMinimal(node.candidate)){
//                System.out.println("minimal");
//                System.out.println("candidate: " + node.candidate);
                sampleReslut.add(node.candidate);
//                System.out.println("result: " + sampleReslut);
//                System.out.println("result size: " + sampleReslut.size());
            }else {
//                System.out.println("non-minimal");
            }
            if(UBg > e){
//                System.out.println("UBg>e");
                for(int i = 1; i <= attrCount*2; i++){
                    int index;
                    if(i <= attrCount){
                        index = i;
                    }else{
                        index = i-attrCount;
                    }
                    if(!node.candidate.getExistAttr().contains(index)){
                        List<AttributeAndDirection> rightExtendInt = deepCloneAtt(node.candidate.right);
                        AttributeAndDirection attrAndDir;
                        if(i <= attrCount){
                            attrAndDir = new AttributeAndDirection(i,AttributeAndDirection.UP);
                        }else {
                            attrAndDir = new AttributeAndDirection(i-attrCount,AttributeAndDirection.DOWN);
                        }
                        rightExtendInt.add(attrAndDir);
//                        if(isAttributeListMinimal(rightExtendInt, node.data, node.sampleSets)){
                        if(isAttributeListMinimalFD(rightExtendInt, map)){
                            SampleTree.SampleTreeNode rightExtend = node.getRightExtendNode(attrAndDir);
                            Search(rightExtend,e,attrCount,map);
                        }
                    }
                }
            }else{
//                System.out.println("UBg<=e");
                List<Integer> list = node.candidate.getExistAttr();
                List<Integer> extentAtrributes = new ArrayList<>();
                for(int i = 1; i <= attrCount; i++){
                    if(!list.contains(i)){
                        extentAtrributes.add(i);
                    }
                }
                if(extentAtrributes.size() != 0){
//                    ExtendAttr(extentAtrributes,node.candidate,node.data,node.sampleSets);
                    ExtendAttr(extentAtrributes,node.candidate,node.data,node.sampleSets,map);
                }
            }
        }else {
//            System.out.println("Vg>e");
            if(LBg <= e){
//                System.out.println("LBg<=e");
                for(int i = 1; i <= attrCount*2; i++) {
                    int index;
                    if(i <= attrCount){
                        index = i;
                    }else{
                        index = i-attrCount;
                    }
                    if (!node.candidate.getExistAttr().contains(index)) {
                        List<AttributeAndDirection> leftExtendInt = deepCloneAtt(node.candidate.left);
                        AttributeAndDirection attrAndDir;
                        if(i <= attrCount) {
                            attrAndDir = new AttributeAndDirection(i, AttributeAndDirection.UP);
                        }else{
                            attrAndDir = new AttributeAndDirection(i-attrCount, AttributeAndDirection.DOWN);
                        }
                        leftExtendInt.add(attrAndDir);
//                        if (isAttributeListMinimal(leftExtendInt, node.data, node.sampleSets)) {
                        if (isAttributeListMinimalFD(leftExtendInt, map)) {
                            SampleTree.SampleTreeNode leftExtend = node.getLeftExtendNode(attrAndDir);
                            Search(leftExtend, e, attrCount,map);
                        }
                    }
                }
            }
        }
    }

    public static void DoSearch(int attrCount, DataFrame data, Set<Set<Integer>> sets, double e, Map<Integer, List<List<Integer>>> map){
        for(int i = 1; i <= attrCount*2; i++){
            for(int j = 1; j <= attrCount; j++){
                int index;
                if(i <= attrCount){
                    index = i;
                }else
                    index = i-attrCount;
                if(j != index){
                    List<AttributeAndDirection> leftList = new ArrayList<>();
                    List<AttributeAndDirection> rightList = new ArrayList<>();
                    AttributeAndDirection left;
                    if(i <= attrCount){
                        left = new AttributeAndDirection(i,AttributeAndDirection.UP);
                    }else {
                        left = new AttributeAndDirection(i - attrCount,AttributeAndDirection.DOWN);
                    }
                    AttributeAndDirection right = new AttributeAndDirection(j,AttributeAndDirection.UP);
                    leftList.add(left);
                    rightList.add(right);
                    AODCandidate candidate = new AODCandidate(leftList, rightList);
                    SampleTree.SampleTreeNode node = new SampleTree.SampleTreeNode(data,candidate,sets);
                    Search(node, e, attrCount, map);
                }
            }
        }
    }

    public static void ComputeTest(DataFrame data, AODCandidate candidate, Set<Set<Integer>> sets){
        int split = 0;
        int swap = 0;
        int order = 0;
        for(Set<Integer> s : sets){
            Iterator<Integer> it = s.iterator();
            int first = it.next();
            int second = it.next();
//          前等后不等，split
            if(isSplit(data,candidate.left,candidate.right,first,second)){
                split++;
            }
//          前小后大，前大后小，swap
            if(isSwap(data, candidate.left, candidate.right,first,second)){
                swap++;
            }
//          前小后小，前大后大，order
            if(isOrder(data,candidate.left,candidate.right,first,second)){
                order++;
            }
        }
        System.out.println("split:" + split);
        System.out.println("swap:" +swap);
        System.out.println("order:"+order);
        double Vg = (double) (split + swap)/sets.size();
        double LBg = (double) swap/sets.size();
        double UBg = (double) 1 - (double)order/sets.size();
        System.out.println("Vg:"+Vg);
        System.out.println("LBg:"+LBg);
        System.out.println("UBg:"+UBg);
    }

    public static List<Integer> deepClone(List<Integer> list){
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            result.add(list.get(i));
        }
        return result;
    }

    public static List<AttributeAndDirection> deepCloneAtt(List<AttributeAndDirection> list){
        List<AttributeAndDirection> result = new ArrayList<>();
        for(int i = 0; i < list.size(); i++){
            result.add(list.get(i));
        }
        return result;
    }

    public static List<Integer> getInt(List<AttributeAndDirection> list){
        List<Integer> result = new ArrayList<>();
        for(AttributeAndDirection and : list){
            result.add(and.attr);
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
//        DataFrame data = DataFrame.fromCsv("data/exp1/echocardiogram.csv");
//        DataFrame data = DataFrame.fromCsv("data/exp1/NCV 1000 19.csv");
//        DataFrame data = DataFrame.fromCsv("data/exp1/echocardiogram.csv");
//        DataFrame data = DataFrame.fromCsv("data/exp6/NCV 14.csv");
//        DataFrame data = DataFrame.fromCsv("data/exp6/test.csv");
        DataFrame data = DataFrame.fromCsv("data/exp6/NCV 300K 14.csv");

        List<FdInfo> list = getFD("fd/NCV 300K 14.txt");
        Map<Integer, List<List<Integer>>> map = getFdMap(list);
        Timer timer2= new Timer();
        ALODDiscoverer discoverer =new DFSDiscovererWithMultipleStandard(G1,0.001);
        //对于DFSDiscovererWithMultipleStandard这个类，它实际使用的error rate以上面这行为准，下面这个是没用的（接口太烂）
        Collection<LexicographicalOrderDependency> aods = discoverer.discover(data, 0.001);
        System.out.println("aod: " + (double)timer2.getTimeUsed() / 1000.0 + "s");
        System.out.println(aods);

        SubsetSampleALODDiscoverer discoverer1 = new SubsetSampleALODDiscoverer(0.02, 0.001, 0.1);
        Set<Set<Integer>> sets = discoverer1.newDiscoverPlusSet(data, 0.001);
        System.gc();
        Timer timer = new Timer();
        DoSearch(data.getColumnCount(),data,sets, 0.001,map);
        System.out.println("sample: " + (double)timer.getTimeUsed() / 1000.0 + "s");
        System.out.println("sampleReslut");
        System.out.println(sampleReslut);
        System.out.println(sampleReslut.size());

        List<MapKey> aodMapKey = new ArrayList<>();
        for(LexicographicalOrderDependency aod : aods){
            aodMapKey.add(new MapKey(aod.left, aod.right));
        }
        List<AODCandidate> aodList = new ArrayList<>();
        for(MapKey m: aodMapKey){
            AODCandidate aod = new AODCandidate(m.left, m.right);
            aodList.add(aod);
        }
        System.out.println("aodList");
        System.out.println(aodList);
        System.out.println(aodList.size());

        int countAOD = 0;
        for(AODCandidate sample: sampleReslut){
           if (aodList.contains(sample)){
               countAOD++;
           }
        }
        System.out.println("aods包含多个sampleResult中aod:" + countAOD);

        int countSample = 0;
        for(AODCandidate aod: aodList){
            if(sampleReslut.contains(aod)){
                countSample++;
            }
        }
        System.out.println("sampleResult包含多少个aods中aod:" + countSample);

    }

//    public static void main(String[] args) throws IOException {
//        List<FdInfo> list = getFD("fd/test.txt");
//        System.out.println(list);
//        Map<Integer, List<List<Integer>>> map = getFdMap(list);
//        System.out.println(map);
//
//        List<Integer> list1 = new ArrayList<>();
//        list1.add(2);
//        list1.add(1);
//        list1.add(5);
//
//
//        System.out.println(isAttributeListMinimalFDInt(list1,map));
//
//    }
}
