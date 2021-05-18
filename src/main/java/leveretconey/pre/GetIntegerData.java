package leveretconey.pre;

import java.io.*;
import java.util.*;

public class GetIntegerData {

    public static List<List<String>> readCSV(String path) throws IOException {
        System.out.println("读文件开始");
        File csv = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(csv));
//        String firstLine = "";
//        firstLine = br.readLine();
        String lineDta = "";
        List<List<String>> List = new ArrayList<>();

        while ((lineDta = br.readLine())!= null){
//            List<String> stringListlist = Arrays.asList(lineDta.split(","));
            List<String> stringListlist = Arrays.asList(lineDta.split(";"));
            List.add(stringListlist);
        }
        System.out.println("读文件结束");
        return List;
    }

    public static void changeToInt(List<List<String>> listList){
        int len = listList.get(0).size();
        for(int i = 0; i < len; i++){
            int index = 0;
            int finalI = i;
            Collections.sort(listList,new Comparator<List<String>>() {
                @Override
                public int compare(List<String> o1, List<String> o2) {
                    List<String> list1 = new ArrayList<>(o1);
                    List<String> list2 = new ArrayList<>(o2);
                    return list1.get(finalI).compareTo(list2.get(finalI));
                }
            });

            String pre = null;
            String now;

            for (int j = 0; j < listList.size(); j++) {
                now =listList.get(j).get(i);
                if (j == 0){
//                    listList.get(j).set(i, Integer.toString(j));
                    listList.get(j).set(i, Integer.toString(index));
                }else if(now.compareTo(pre) == 0) {
                    listList.get(j).set(i, Integer.toString(index));
                }else{
                    index++;
//                    listList.get(j).set(i, Integer.toString(j));
                    listList.get(j).set(i, Integer.toString(index++));
                }
                pre = now;
            }
        }
    }

    public static void writeCSV(List<List<String>> listList, String path) throws IOException {
        System.out.println("写文件开始");
        File csv2 = new File(path);//CSV文件
        BufferedWriter bw = new BufferedWriter(new FileWriter(csv2,true));


        for(int i = 0; i < listList.size(); i++){
            String s0 = String.join(",",listList.get(i));
            bw.write(s0);
            bw.newLine();
        }
        bw.close();

        System.out.println("写文件结束");
    }

    public static void main(String[] args) throws IOException {

        List<List<String>> listList = readCSV("data/stringData/Horse 300 29.csv");

        changeToInt(listList);

        writeCSV(listList, "data/intData/Horse 300 29.csv");
    }
}
