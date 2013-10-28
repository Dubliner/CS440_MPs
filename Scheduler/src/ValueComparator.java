import java.util.Comparator;
import java.util.Map;

/*
 * A comparator class for sorting the hash table
 * */
public class ValueComparator implements Comparator<Integer> {

    Map<Integer, Integer> base;
    public ValueComparator(Map<Integer, Integer> base) {
        this.base = base;
    }
   
    public int compare(Integer a, Integer b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } 
    }
}