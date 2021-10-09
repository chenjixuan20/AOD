package leveretconey.pre;

import java.io.*;
import java.util.*;

public class GetIntegerData {

    public static List<List<String>> readCSV(String path, boolean hasTitle, String segmentation ) throws IOException {
        System.out.println("读文件开始");
        File csv = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(csv));
        if(hasTitle){
            String firstLine = "";
            firstLine = br.readLine();
        }
        String lineDta = "";
        List<List<String>> List = new ArrayList<>();

        while ((lineDta = br.readLine())!= null){
            List<String> stringListlist = null;
            if(segmentation.equals(","))
                stringListlist = Arrays.asList(lineDta.split(","));
            if(segmentation.equals(";"))
                stringListlist = Arrays.asList(lineDta.split(";"));
            List.add(stringListlist);
        }
        System.out.println("读文件结束");
        return List;
    }

    public static void changeToInt(List<List<String>> listList){
        if(listList.size() == 0) return;
        int len = listList.get(0).size();
        for(int i = 0; i < len; i++){
            int nowSort = i;
            listList.sort(new Comparator<List<String>>() {
                @Override
                public int compare(List<String> o1, List<String> o2) {
                    List<String> list1 = new ArrayList<>(o1);
                    List<String> list2 = new ArrayList<>(o2);
                    return list1.get(nowSort).compareTo(list2.get(nowSort));
                }
            });

            String temp = listList.get(0).get(i);
            int index = 0;

            for (int j = 0; j < listList.size(); j++) {
                if(temp.equals(listList.get(j).get(i))){
                    listList.get(j).set(i, String.valueOf(index));
                }else {
                    index++;
                    temp = listList.get(j).get(i);
                    listList.get(j).set(i, String.valueOf(index));
                }
            }
        }
    }

    public static void writeCSV(List<List<String>> listList, String path) throws IOException {
        System.out.println("写文件开始");
        File csv2 = new File(path);//CSV文件
        BufferedWriter bw = new BufferedWriter(new FileWriter(csv2,true));
        int attributeNum = listList.get(0).size();
        List<String> head = new ArrayList<>();
        for(int i = 0; i < attributeNum; i++){
            head.add(String.valueOf(i+1));
        }
        String h = String.join(",", head);
        bw.write(h);
        bw.newLine();

        for (List<String> strings : listList) {
            String s0 = String.join(",", strings);
            bw.write(s0);
            bw.newLine();
        }
        bw.close();

        System.out.println("写文件结束");
    }

    public static void main(String[] args) throws IOException {

        List<List<String>> listList = readCSV("data/exp/flight_1k-15.csv", true, ",");

        changeToInt(listList);

        writeCSV(listList, "data/exp/flight_1k-15-int.csv");
    }
}
