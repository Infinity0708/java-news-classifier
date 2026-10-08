package uob.oop;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class NewsClassifier {
    public String[] myHTMLs;
    public String[] myStopWords = new String[127];
    public String[] newsTitles;
    public String[] newsContents;
    public String[] newsCleanedContent;
    public double[][] newsTFIDF;

    private final String TITLE_GROUP1 = "Osiris-Rex's sample from asteroid Bennu will reveal secrets of our solar system";
    private final String TITLE_GROUP2 = "Bitcoin slides to five-month low amid wider sell-off";

    public Toolkit myTK;

    public NewsClassifier() {
        myTK = new Toolkit();
        myHTMLs = myTK.loadHTML();
        myStopWords = myTK.loadStopWords();

        loadData();
    }

    public static void main(String[] args) {
        NewsClassifier myNewsClassifier = new NewsClassifier();

        myNewsClassifier.newsCleanedContent = myNewsClassifier.preProcessing();

        myNewsClassifier.newsTFIDF = myNewsClassifier.calculateTFIDF(myNewsClassifier.newsCleanedContent);

        double[][] doubSimilarity = myNewsClassifier.newsSimilarity(0);

        System.out.println(myNewsClassifier.resultString(doubSimilarity, 10));

        String strGroupingResults = myNewsClassifier.groupingResults(myNewsClassifier.TITLE_GROUP1, myNewsClassifier.TITLE_GROUP2);
        System.out.println(strGroupingResults);
    }

    public void loadData() {
        newsTitles = new String[myHTMLs.length];
        newsContents = new String[myHTMLs.length];
        for (int i = 0; i < myHTMLs.length; i++) {
            newsTitles[i] = HtmlParser.getNewsTitle(myHTMLs[i]);
            newsContents[i] = HtmlParser.getNewsContent(myHTMLs[i]);
        }
    }

    public String[] preProcessing() {
        String[] myCleanedContent = new String[newsContents.length];
        for (int i = 0; i < newsContents.length; i++) {
            myCleanedContent[i] = NLP.removeStopWords(NLP.textLemmatization(NLP.textCleaning(newsContents[i])), myStopWords);
        }
        return myCleanedContent;
    }

    public double[][] calculateTFIDF(String[] cleanedContents) {
        String[] vocabulary = buildVocabulary(cleanedContents);
        Map<String, Integer> indexes = new LinkedHashMap<>();
        for (int j = 0; j < vocabulary.length; j++) indexes.put(vocabulary[j], j);
        int[][] counts = new int[cleanedContents.length][vocabulary.length];
        int[] totals = new int[cleanedContents.length];
        int[] documentFrequency = new int[vocabulary.length];
        for (int i = 0; i < cleanedContents.length; i++) {
            String text = cleanedContents[i].trim();
            if (text.isEmpty()) continue;
            String[] words = text.split("\\s+");
            totals[i] = words.length;
            for (String word : words) counts[i][indexes.get(word)]++;
            for (int j = 0; j < vocabulary.length; j++) {
                if (counts[i][j] > 0) documentFrequency[j]++;
            }
        }
        double[][] result = new double[cleanedContents.length][vocabulary.length];
        for (int j = 0; j < vocabulary.length; j++) {
            double idf = Math.log((double) cleanedContents.length / documentFrequency[j]) + 1;
            for (int i = 0; i < cleanedContents.length; i++) {
                if (totals[i] > 0) result[i][j] = (double) counts[i][j] / totals[i] * idf;
            }
        }
        return result;
    }

    public String[] buildVocabulary(String[] cleanedContents) {
        Map<String, Boolean> vocabulary = new LinkedHashMap<>();
        for (String content : cleanedContents) {
            if (content.trim().isEmpty()) continue;
            for (String word : content.trim().split("\\s+")) vocabulary.put(word, true);
        }
        return vocabulary.keySet().toArray(new String[0]);
    }

    public double[][] newsSimilarity(int _newsIndex) {
        double[][] mySimilarity = new double[newsTFIDF.length][2];

        for (int i = 0; i < newsTFIDF.length; i++) {
            mySimilarity[i][0] = i;
            mySimilarity[i][1] = new Vector(newsTFIDF[_newsIndex]).cosineSimilarity(new Vector(newsTFIDF[i]));
        }

        SortDes(mySimilarity);
        return mySimilarity;
    }

    public String groupingResults(String _firstTitle, String _secondTitle) {

        int firstIndex = 0, secondIndex = 0;
        for (int i = 0; i < newsTitles.length; i++) {
            if (_firstTitle.equals(newsTitles[i])) {
                firstIndex = i;
            }
            if (_secondTitle.equals(newsTitles[i])) {
                secondIndex = i;
            }
        }

        double[][] firstSimilarity = newsSimilarity(firstIndex);
        double[][] secondSimilarity = newsSimilarity(secondIndex);
        SortAsc(firstSimilarity);
        SortAsc(secondSimilarity);
        int[] tempGroup1 = new int[firstSimilarity.length];
        int index1 = 0;

        int[] tempGroup2 = new int[firstSimilarity.length];
        int index2 = 0;
        for (int i = 0; i < firstSimilarity.length; i++) {
            if (firstSimilarity[i][1] > secondSimilarity[i][1]) {
                tempGroup1[index1] = i;
                index1++;
            } else if (firstSimilarity[i][1] < secondSimilarity[i][1]) {
                tempGroup2[index2] = i;
                index2++;
            }
        }
        int[] arrayGroup1 = new int[index1];
        int[] arrayGroup2 = new int[index2];

        System.arraycopy(tempGroup1,0,arrayGroup1,0,index1);
        System.arraycopy(tempGroup2,0,arrayGroup2,0,index2);

        return resultString(arrayGroup1, arrayGroup2);
    }

    public String resultString(double[][] _similarityArray, int _groupNumber) {
        StringBuilder mySB = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
        for (int j = 0; j < _groupNumber; j++) {
            for (int k = 0; k < _similarityArray[j].length; k++) {
                if (k == 0) {
                    mySB.append((int) _similarityArray[j][k]).append(" ");
                } else {
                    String formattedCS = decimalFormat.format(_similarityArray[j][k]);
                    mySB.append(formattedCS).append(" ");
                }
            }
            mySB.append(newsTitles[(int) _similarityArray[j][0]]).append("\r\n");
        }
        mySB.delete(mySB.length() - 2, mySB.length());
        return mySB.toString();
    }

    public String resultString(int[] _firstGroup, int[] _secondGroup) {
        StringBuilder mySB = new StringBuilder();
        mySB.append("There are ").append(_firstGroup.length).append(" news in Group 1, and ").append(_secondGroup.length).append(" in Group 2.\r\n").append("=====Group 1=====\r\n");

        for (int i : _firstGroup) {
            mySB.append("[").append(i + 1).append("] - ").append(newsTitles[i]).append("\r\n");
        }
        mySB.append("=====Group 2=====\r\n");
        for (int i : _secondGroup) {
            mySB.append("[").append(i + 1).append("] - ").append(newsTitles[i]).append("\r\n");
        }

        mySB.delete(mySB.length() - 2, mySB.length());
        return mySB.toString();
    }

    public static void SortDes(double[][] arr) {
        for(int i = 1; i < arr.length; i++) {
            double temp = arr[i][1];
            for(int j = i; j > 0 ; j--) {
                if (temp > arr[j-1][1]){
                    double temp0 = arr[j][0];
                    arr[j][0] = arr[j-1][0];
                    arr[j-1][0] = temp0;
                    double temp1 = arr[j][1];
                    arr[j][1] = arr[j-1][1];
                    arr[j-1][1] = temp1;
                }
            }
        }
    }

    public static void SortAsc(double[][] arr) {
        for(int i = 1; i < arr.length; i++) {
            double temp = arr[i][0];
            for(int j = i; j > 0 ; j--) {
                if (temp < arr[j-1][0]){
                    double temp0 = arr[j][0];
                    arr[j][0] = arr[j-1][0];
                    arr[j-1][0] = temp0;
                    double temp1 = arr[j][1];
                    arr[j][1] = arr[j-1][1];
                    arr[j-1][1] = temp1;
                }
            }
        }

    }

}
