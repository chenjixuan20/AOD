package leveretconey.cocoa.sample;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import leveretconey.util.ReturnData;
import leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard;
import leveretconey.dependencyDiscover.Data.DataFrame;
import leveretconey.dependencyDiscover.Dependency.LexicographicalOrderDependency;
import leveretconey.dependencyDiscover.Discoverer.ALODDiscoverer;
import leveretconey.util.Util;

import static leveretconey.cocoa.multipleStandard.DFSDiscovererWithMultipleStandard.ValidatorType.G1;

public class SubsetSampleALODDiscoverer extends ALODDiscoverer {


    private int sampleSize;

    public SubsetSampleALODDiscoverer(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    public SubsetSampleALODDiscoverer(double alpha, double e, double sigma) {
        double z = getNormDistributionQuantile(1 - alpha/2);
        this.sampleSize = (int)Math.floor( z * z /(sigma * sigma * e) );
//        this.sampleSize = 8646;
    }

    public Set<Set<Integer>> newDiscoverPlusSet(DataFrame data, double errorRateThreshold){
        Util.out("采样元组对数量:"+sampleSize);
        int size = this.sampleSize;
        //采样的元组数
        Set<Set<Integer>> tuplesPair = new HashSet<>();
        Random random=new Random();
        //get(i)中i的范围是0～getTupleCount-1
        int max = data.getTupleCount()-1;
        int min = 0;
        while(tuplesPair.size() < size) {
            Set<Integer> tuple = new HashSet<>();
            while (tuple.size() < 2){
                int num = random.nextInt(max - min + 1) + min;
                tuple.add(num);
            }
            tuplesPair.add(tuple);
        }
        System.out.println(tuplesPair.size());
        return  tuplesPair;
    }

    @Override
    public ReturnData newDiscover(DataFrame data, double errorRateThreshold) {
        Util.out("采样元组对数量"+sampleSize);
//        int size = 2 * Math.min(data.getTupleCount(), this.sampleSize);
        int size = this.sampleSize;
        Set<Integer> columns = new HashSet<>();
        for (int i = 0; i < data.getColumnCount(); i++) {
            columns.add(i);
        }
        Set<Integer> tuples = new HashSet<>();
        Random random=new Random();
        for (int i = 0; i < size; i++) {
            tuples.add(random.nextInt(data.getTupleCount()));
        }
        DataFrame subset=data.getSubDataFrame(tuples,columns);
        Util.out("采样元组数量"+subset.getTupleCount());
        return new DFSDiscovererWithMultipleStandard(G1,errorRateThreshold)
                .newDiscover(subset,errorRateThreshold);
    }

    @Override
    public Collection<LexicographicalOrderDependency> discover(DataFrame data, double errorRateThreshold) {
        Util.out("采样元组对数量"+sampleSize);
//        int size = 2 * Math.min(data.getTupleCount(), this.sampleSize);
        int size = this.sampleSize;
        Set<Integer> columns = new HashSet<>();
        for (int i = 0; i < data.getColumnCount(); i++) {
            columns.add(i);
        }
        Set<Integer> tuples = new HashSet<>();
        Random random=new Random();
        for (int i = 0; i < size; i++) {
            tuples.add(random.nextInt(data.getTupleCount()));
        }
        DataFrame subset=data.getSubDataFrame(tuples,columns);
        Util.out("采样元组数量"+subset.getTupleCount());
        return new DFSDiscovererWithMultipleStandard(G1,errorRateThreshold)
                .discover(subset,errorRateThreshold);
    }

    private double getNormDistributionQuantile(double p)
    {
        if (p == 0.5)
            return 0;
        double[] b ={0.1570796288E1,   0.3706987906E-1,
                -0.8364353589E-3, -0.2250947176E-3,
                0.6841218299E-5,  0.5824238515E-5,
                -0.1045274970E-5,  0.8360937017E-7,
                -0.3231081277E-8,  0.3657763036E-10,
                0.6936233982E-12};
        double alpha = 0;
        if ((0 < p) && (p < 0.5))
            alpha = p;
        else if ((0.5 < p) && (p < 1))
            alpha = 1 - p;
        double y = -Math.log(4 * alpha * (1 - alpha));
        double u = 0;
        //Toda近似公式，最大误差1.2e-8
        for (int i = 0; i < b.length; i++)
        {
            u += b[i] * Math.pow(y, i);
        }
        u = Math.sqrt(y * u);
        double up = 0;
        if ((0 < p) && (p < 0.5))
            up = -u;
        else if ((0.5 < p) && (p < 1))
            up = u;
        return up;
    }

    public static void main(String[] args) {
        SubsetSampleALODDiscoverer discoverer=new SubsetSampleALODDiscoverer(1000);
        Util.out(discoverer.getNormDistributionQuantile(0.98));
    }
}
