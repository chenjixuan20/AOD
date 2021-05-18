package leveretconey.exp6.samplePairs;


import java.util.ArrayList;
import java.util.List;

public class AttributeAndDirection {
    public static final int UP=1;
    public static final int DOWN=-1;

    public final int attr;
    public final int dir;

    public AttributeAndDirection(int attr, int dir){
        this.attr = attr;
        this.dir = dir;
    }

    @Override
    public String toString() {
        if(dir == UP) return attr + "↑";
        return attr + "↓";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof AttributeAndDirection) {
            AttributeAndDirection atad = (AttributeAndDirection) obj;
            return attr == atad.attr && dir == atad.dir;
        }
        return false;
    }

    public static void main(String[] args) {
        AttributeAndDirection A = new AttributeAndDirection(1,UP);
        AttributeAndDirection B = new AttributeAndDirection(1,UP);
        System.out.println(A==B);
        System.out.println(A.equals(B));
        List<AttributeAndDirection> a = new ArrayList<>();
        List<AttributeAndDirection> b = new ArrayList<>();
        a.add(A);
        a.add(A);
        b.add(B);
        b.add(B);
        System.out.println(a==b);
        System.out.println(a.equals(b));

    }
}
