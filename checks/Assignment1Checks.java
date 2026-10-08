import uob.oop.HtmlParser;
import uob.oop.NewsClassifier;

public class Assignment1Checks {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void main(String[] args) {
        NewsClassifier classifier = new NewsClassifier();
        double[][] values = classifier.calculateTFIDF(new String[]{"cat cat dog", "dog"});
        double catIdf = Math.log(2) + 1;
        check(Math.abs(values[0][0] - 2.0 / 3 * catIdf) < 1e-12, "TF-IDF term weighting");
        check(Math.abs(values[0][1] - 1.0 / 3) < 1e-12, "Shared term IDF");
        check(values[1][0] == 0 && values[1][1] == 1, "Absent term and single-token document");
        check(classifier.buildVocabulary(new String[]{"", "cat  dog"}).length == 2, "No empty vocabulary terms");
        check(classifier.calculateTFIDF(new String[]{"", "cat"})[0][0] == 0, "Empty document remains zero");
        check(HtmlParser.getNewsContent("\"articleBody\": \"unfinished").equals("Content not found!"), "Malformed HTML body");
        classifier.newsCleanedContent = classifier.preProcessing();
        classifier.newsTFIDF = classifier.calculateTFIDF(classifier.newsCleanedContent);
        String result = classifier.groupingResults(classifier.newsTitles[10], classifier.newsTitles[13]);
        check(result.contains("There are 10 news in Group 1, and 10 in Group 2."), "All supplied articles grouped");
        System.out.println("Assignment 1 regression checks passed.");
    }
}
