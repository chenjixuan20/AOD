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

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
//        8, 12, 2, null, null, 6, 4, null, null, null, null
        sb.append(8+ " ");
        sb.append(12+ " ");
        sb.append(2+ " ");
        sb.append("null ");
        sb.append("null ");
        sb.append(6 + " ");
        sb.append(4 + " ");
        sb.append("null ");
        sb.append("null ");
        sb.append("null ");
        sb.append("null ");
        System.out.println(sb.toString());

        String[] temp = sb.toString().split(" ");

        for(String s: temp){
            System.out.println(s);
        }
        TreeNode root = null;
        if(!temp[0].equals("null")){
             root = new TreeNode(Integer.parseInt(temp[0]));
        }

        int u = 1;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            TreeNode now = queue.poll();
            if(now != null){
                if(!temp[u].equals("null")){
                    now.left = new TreeNode(Integer.parseInt(temp[u++]));
                    queue.add(now.left);
                }
                else{
                    now.left = null;
                    u++;
                }
                if(!temp[u].equals("null")){
                    now.right = new TreeNode(Integer.parseInt(temp[u++]));
                    queue.add(now.right);
                }
                else{
                    now.right = null;
                    u++;
                }
            }
        }
        StringBuilder sb2 = new StringBuilder();
        Queue<TreeNode> p = new LinkedList<>();
        p.add(root);
        while(!p.isEmpty()){
            TreeNode now = p.poll();
            if(now==null) sb2.append("null ");
            else sb2.append(now.val + " ");
            if(now != null){
                p.add(now.left);
                p.add(now.right);
            }
        }
        System.out.println(sb2.toString());
    }

}
