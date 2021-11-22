package leveretconey.exp.exp6.samplePairs;

import leveretconey.dependencyDiscover.Predicate.SingleAttributePredicateList;

import java.util.ArrayList;
import java.util.List;

public class MapKey {
    public List<AttributeAndDirection> left;
    public List<AttributeAndDirection> right;


    public MapKey(SingleAttributePredicateList preLeft, SingleAttributePredicateList preRight){
        String[] preLeftPart = preLeft.toString().split(",");
        String[] preRightPart = preRight.toString().split(",");
        this.left = getIntList(preLeftPart);
        this.right = getIntList(preRightPart);
    }

    public static List<AttributeAndDirection> getIntList(String[] strings){
        List<AttributeAndDirection> list = new ArrayList<>();
        for(String s : strings){
            int attr = 0;
            int dir = 0;
            char[] chars = s.toCharArray();
            for(int i = 0; i < chars.length; i++){
                if(chars[i] >= '0' && chars[i] <= '9'){
                    attr = attr*10 + (chars[i] - '0');
                }
                if(chars[i] == '<'){
                    dir = AttributeAndDirection.UP;
                    break;
                }
                if(chars[i] == '>'){
                    dir = AttributeAndDirection.DOWN;
                    break;
                }
            }
            list.add(new AttributeAndDirection(attr,dir));
        }
        return list;
    }


    @Override
    public String toString() {
        return left + "|-->" + right;
    }
}
