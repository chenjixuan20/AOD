package leveretconey.fastod;

import java.util.*;

import com.sun.javafx.binding.StringFormatter;
import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Predicate.Operator;
import leveretconey.dependencyDiscover.Predicate.SingleAttributePredicate;
import leveretconey.util.Timer;

public class StrippedPartition {
    public List<Integer> indexes;
    public List<Integer> begins;
    private DataFrame data;
    public static long mergeTime=0;
    public static long validateTime=0;
    public static long cloneTime=0;
    public static final int CACHE_SIZE=10000;
    private static Map<AttributeSet, StrippedPartition> cache=new LinkedHashMap<AttributeSet, StrippedPartition>(){
        @Override
        protected boolean removeEldestEntry(Map.Entry<AttributeSet, StrippedPartition> eldest) {
            return size()>=CACHE_SIZE;
        }
    };

    public StrippedPartition(DataFrame data) {
        this.data = data;

        indexes=new ArrayList<>();
        for (int i = 0; i < data.getTupleCount(); i++) {
            indexes.add(i);
        }
        begins=new ArrayList<>();
        if(data.getTupleCount()!=0){
            begins.add(0);
        }
        begins.add(data.getTupleCount());
    }

    public StrippedPartition(StrippedPartition origin){
        this.indexes=new ArrayList<>(origin.indexes);
        this.begins=new ArrayList<>(origin.begins);
        this.data=origin.data;
    }


    public StrippedPartition product(int attribute){
        Timer timer=new Timer();
        List<Integer> newIndexes=new ArrayList<>();
        List<Integer> newBegins=new ArrayList<>();
        int fillPointer=0;
        for(int beginPointer=0;beginPointer<begins.size()-1;beginPointer++){
            int groupBegin=begins.get(beginPointer);
            int groupEnd=begins.get(beginPointer+1);
            HashMap<Integer,List<Integer>> subGroups=new HashMap<>();

            for(int i=groupBegin;i<groupEnd;i++){
                int index= indexes.get(i);
                int value=data.get(index,attribute);
                if(!subGroups.containsKey(value)){
                    subGroups.put(value,new ArrayList<>());
                }
                subGroups.get(value).add(index);
            }

            for(List<Integer> newGroup:subGroups.values()){
                if(newGroup.size()>=1){
                    newBegins.add(fillPointer);
                    for (int i :newGroup) {
                        newIndexes.add(i);
                        fillPointer++;
                    }
                }
            }
        }
        this.indexes=newIndexes;
        this.begins=newBegins;
        begins.add(indexes.size());
        mergeTime+=timer.getTimeUsed();
        return this;
    }

    public boolean split(int right){
        Timer timer=new Timer();
        for(int beginPointer=0;beginPointer<begins.size()-1;beginPointer++) {
            int groupBegin = begins.get(beginPointer);
            int groupEnd = begins.get(beginPointer + 1);

            int groupValue=data.get(indexes.get(groupBegin),right);
            for(int i=groupBegin+1;i<groupEnd;i++) {
                int index = indexes.get(i);
                int value = data.get(index, right);
                if(value!=groupValue) {
                    validateTime+=timer.getTimeUsed();
                    return true;
                }
            }
        }
        validateTime+=timer.getTimeUsed();
        return false;
    }

    public boolean swap(SingleAttributePredicate left, int right){
        Timer timer=new Timer();
        for(int beginPointer=0;beginPointer<begins.size()-1;beginPointer++) {
            int groupBegin = begins.get(beginPointer);
            int groupEnd = begins.get(beginPointer + 1);

            List<DataAndIndex> values=new ArrayList<>();
            for(int i=groupBegin;i<groupEnd;i++) {
                int index = indexes.get(i);
                values.add(new DataAndIndex(filteredDataFrameGet(data,index,left),data.get(index,right)));
            }
            Collections.sort(values);
            int beforeMax=Integer.MIN_VALUE;
            int groupMax=Integer.MIN_VALUE;
            for (int i = 0; i < values.size(); i++) {
                int index=values.get(i).index;
                if(i==0 || values.get(i-1).data != values.get(i).data){
                    beforeMax=Math.max(groupMax,beforeMax);
                    groupMax=index;
                }else {
                    groupMax=Math.max(groupMax,index);
                }
                if(index<beforeMax) {
                    validateTime+=timer.getTimeUsed();
                    return true;
                }
            }
        }
        validateTime+=timer.getTimeUsed();
        return false;
    }

    @Override
    public String toString() {
        return "StrippedPartition{" +
                "indexes=" + indexes +
                ", begins=" + begins +
                '}';
    }

    public StrippedPartition deepClone(){
        Timer timer=new Timer();
        StrippedPartition result=new StrippedPartition(this.data);
        result.indexes= new ArrayList<>(indexes);
        result.begins= new ArrayList<>(begins);
        cloneTime+=timer.getTimeUsed();
        return result;
    }

    public static StrippedPartition getStrippedPartition(AttributeSet attributeSet, DataFrame data){
        if(cache.containsKey(attributeSet)){
            return cache.get(attributeSet);
        }
        StrippedPartition result=null;
        for(int attribute:attributeSet){
            AttributeSet oneLess=attributeSet.deleteAttribute(attribute);
            if(cache.containsKey(oneLess)){
                result=cache.get(oneLess).deepClone().product(attribute);
            }
        }
        if(result==null){
            result=new StrippedPartition(data);
            for (int attribute : attributeSet) {
                result.product(attribute);
            }
        }
        cache.put(attributeSet,result);
        return result;
    }

    public long splitRemoveCount(int right){
        Timer timer=new Timer();
        long result=0;
        for(int beginPointer = 0; beginPointer<begins.size()-1; beginPointer++) {

            int groupBegin = begins.get(beginPointer);
            int groupEnd = begins.get(beginPointer + 1);
            int groupLength=groupEnd-groupBegin;
            HashMap<Integer,Integer> groupInt2count=new HashMap<>();

            for (int i = groupBegin ; i < groupEnd; i++) {
                int rightValue=data.get(indexes.get(i),right);
                groupInt2count.put(rightValue,groupInt2count.getOrDefault(rightValue,0)+1);
            }
            int max=Integer.MIN_VALUE;
            for (int count : groupInt2count.values()) {
                max=Math.max(max,count);
            }
            result+=groupLength-max;
        }
        validateTime+=timer.getTimeUsed();
        return result;
    }

    public long swapRemoveCount(SingleAttributePredicate left, int right){
        System.out.println("swapRemoveCount");
        System.out.println(this);
        int length=indexes.size();
        int[] vioCount=new int[length];
        boolean[] deleted=new boolean[length];
        int result=0;
        nextClass:
        for(int beginPointer = 0; beginPointer<begins.size()-1; beginPointer++) {
            int groupBegin = begins.get(beginPointer);
            int groupEnd = begins.get(beginPointer + 1);
            for (int i = groupBegin ; i < groupEnd; i++) {
                int lefti=filteredDataFrameGet(data,indexes.get(i),left);
                int righti=data.get(indexes.get(i),right);
                for (int j = i+1 ; j < groupEnd; j++) {
                    int diffLeft=lefti-filteredDataFrameGet(data,indexes.get(j),left);
                    int diffRight=righti-data.get(indexes.get(j),right);
                    if (diffLeft!=0 && diffRight!=0 && (diffLeft>0 != diffRight>0)){
                        vioCount[i]++;
                        vioCount[j]++;
                    }
                }
            }
            while (true){
                int deleteIndex=-1;
                for (int i = groupBegin ; i < groupEnd; i++) {
                    if (!deleted[i] &&(deleteIndex==-1 || vioCount[i]>vioCount[deleteIndex])){
                        deleteIndex=i;
                    }
                }
                if (deleteIndex==-1 || vioCount[deleteIndex]==0){
                    continue nextClass;
                }
                result++;
                deleted[deleteIndex]=true;
                int leftj=filteredDataFrameGet(data,indexes.get(deleteIndex),left);
                int rightj=data.get(indexes.get(deleteIndex),right);
                for (int i = groupBegin ; i < groupEnd; i++) {
                    int diffLeft=leftj-filteredDataFrameGet(data,indexes.get(i),left);
                    int diffRight=rightj-data.get(indexes.get(i),right);
                    if (diffLeft!=0 && diffRight!=0 && (diffLeft>0 != diffRight>0)){
                        vioCount[i]--;
                    }
                }
            }
        }
        return result;
    }

    public long swapRemoveCountEDBT(SingleAttributePredicate left, int right){
        System.out.println("swapRemoveCountEDBT");
        System.out.println(this);
        List<Integer> sortedIndex = new ArrayList<>(indexes);
        System.out.println(sortedIndex);
        List<Integer> sortedBegin = new ArrayList<>(begins);
        for(int beginPointer=0;beginPointer<sortedBegin.size()-1;beginPointer++) {
            int groupBegin = sortedBegin.get(beginPointer);
            int groupEnd = sortedBegin.get(beginPointer + 1);
            List<Integer> part = sortedIndex.subList(groupBegin, groupEnd);
            part.sort((A,B) -> {
                if(data.get(A,left.attribute) == data.get(B,left.attribute))
                    return data.get(A,right) - data.get(B,right);
                else return data.get(A,left.attribute) - data.get(B,left.attribute);
            });
        }
        System.out.println(sortedIndex);
        return data.getTupleCount() - lengthOfLISBS(sortedIndex, right);
    }

    public long lengthOfLIS(List<Integer> index, int right){
        System.out.println("lengthOfLIS");
        int n = index.size();
        int[] dp = new int[n];
        int[] nums = new int[n];
        for(int i = 0; i < n; i++){
            nums[i] = data.get(index.get(i), right);
            System.out.print(nums[i] + " ");
        }
        System.out.println();
        int res = 0;
        Arrays.fill(dp, 1);
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < i; j++) {
                if(nums[j] <= nums[i]) dp[i] = Math.max(dp[i], dp[j] + 1);
            }
            res = Math.max(res, dp[i]);
        }
        System.out.println(res);
        return res;
    }

    public long lengthOfLISBS(List<Integer> index, int right){
        System.out.println("lengthOfLISBS");
        int n = index.size();
        int[] tails = new int[n];
        int[] nums = new int[n];
        for(int i = 0; i < n; i++){
            nums[i] = data.get(index.get(i), right);
            System.out.print(nums[i] + " ");
        }
        int res = 0;
        System.out.println();
        for(int num : nums) {
            int i = 0, j = res;
            while(i < j) {
                int m = (i + j) / 2;
                if(tails[m] <= num) i = m + 1;
                else j = m;
            }
            tails[i] = num;
            if(res == j) res++;
        }
        System.out.println(res);
        return res;
    }

    private int filteredDataFrameGet(DataFrame data,int tuple,SingleAttributePredicate column){
        int result=data.get(tuple,column.attribute);
        if (column.operator== Operator.greaterEqual){
            result=-result;
        }
        return result;
    }

    public static int lengthOfLIS(List<Integer> nums) {
        int[] tails = new int[nums.size()];
        int res = 0;
        for(int num : nums) {
            int i = 0, j = res;
            while(i < j) {
                int m = (i + j) / 2;
                if(tails[m] <= num) i = m + 1;
                else j = m;
            }
            tails[i] = num;
            if(res == j) res++;
        }
        return res;
    }

    public static void main(String[] args) {
        DataFrame data = DataFrame.fromCsv("data/test1.csv");
        StrippedPartition sp = new StrippedPartition(data);
        sp.product(0);
        System.out.println(sp);

    }
}

