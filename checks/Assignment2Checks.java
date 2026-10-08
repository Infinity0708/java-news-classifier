import uob.oop.DocumentLengths;
import uob.oop.HtmlParser;
import java.util.List;

public class Assignment2Checks {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void main(String[] args) {
        check(DocumentLengths.median(List.of(9, 1, 3)) == 3, "Odd median");
        check(DocumentLengths.median(List.of(8, 2, 6, 4)) == 5, "Even median");
        check(DocumentLengths.median(List.of(7)) == 7, "Single article");
        check(DocumentLengths.median(List.of(2, 8)) == 5, "Two articles");
        try {
            DocumentLengths.median(List.of());
            throw new AssertionError("Empty corpus must be rejected");
        } catch (IllegalArgumentException expected) { }
        check(HtmlParser.getNewsTitle("<title>Plain title</title>").equals("Plain title"), "Title without publisher suffix");
        check(HtmlParser.getNewsTitle("missing opening</title>").equals("Title not found!"), "Missing opening title tag");
        check(HtmlParser.getLabel("missing opening</label>").equals("-1"), "Missing opening label tag");
        System.out.println("Assignment 2 median/parser checks passed; external NLP/model pipeline not tested here.");
    }
}
