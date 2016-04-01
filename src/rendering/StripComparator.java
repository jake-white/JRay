package rendering;

import java.util.Comparator;

public class StripComparator implements Comparator<Strip> {
    @Override
    public int compare(Strip o1, Strip o2) {
        return o1.getCast().compareTo(o2.getCast());
    }
}
