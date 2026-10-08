package uob.oop;

import java.util.ArrayList;
import java.util.List;

/** Median supported-token length used for a fixed-size document representation. */
public final class DocumentLengths {
    private DocumentLengths() { }

    public static int median(List<Integer> lengths) {
        if (lengths.isEmpty()) throw new IllegalArgumentException("No articles available");
        List<Integer> sorted = new ArrayList<>(lengths);
        sorted.sort(Integer::compareTo);
        int middle = sorted.size() / 2;
        return sorted.size() % 2 == 0
                ? (int) (((long) sorted.get(middle - 1) + sorted.get(middle)) / 2)
                : sorted.get(middle);
    }
}
