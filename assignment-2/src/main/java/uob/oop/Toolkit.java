package uob.oop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Toolkit {
    public static List<String> listVocabulary = null;
    public static List<double[]> listVectors = null;
    private static final String FILENAME_GLOVE = "glove.6B.50d_Reduced.csv";

    public static final String[] STOPWORDS = {"a", "able", "about", "across", "after", "all", "almost", "also", "am", "among", "an", "and", "any", "are", "as", "at", "be", "because", "been", "but", "by", "can", "cannot", "could", "dear", "did", "do", "does", "either", "else", "ever", "every", "for", "from", "get", "got", "had", "has", "have", "he", "her", "hers", "him", "his", "how", "however", "i", "if", "in", "into", "is", "it", "its", "just", "least", "let", "like", "likely", "may", "me", "might", "most", "must", "my", "neither", "no", "nor", "not", "of", "off", "often", "on", "only", "or", "other", "our", "own", "rather", "said", "say", "says", "she", "should", "since", "so", "some", "than", "that", "the", "their", "them", "then", "there", "these", "they", "this", "tis", "to", "too", "twas", "us", "wants", "was", "we", "were", "what", "when", "where", "which", "while", "who", "whom", "why", "will", "with", "would", "yet", "you", "your"};

    public void loadGlove() throws IOException {
        listVocabulary = new ArrayList<>();
        listVectors = new ArrayList<>();
        try (BufferedReader myReader = new BufferedReader(
                new FileReader(Toolkit.getFileFromResource(FILENAME_GLOVE)))) {
            while (true){
                String line =  myReader.readLine();
                if (line == null)break;
                int index = line.indexOf(',');
                String term = line.substring(0,index);
                listVocabulary.add(term);
                String[] paraStr = line.substring(index+1).split(",");
                double[] paraDouble = new double[paraStr.length];
                for (int i = 0; i < paraStr.length; i++) {
                    paraDouble[i] = Double.parseDouble(paraStr[i]);
                }
                listVectors.add(paraDouble);
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new IOException("Cannot load GloVe embeddings", e);
        }

    }

    private static File getFileFromResource(String fileName) throws URISyntaxException {
        ClassLoader classLoader = Toolkit.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException(fileName);
        } else {
            return new File(resource.toURI());
        }
    }

    public List<NewsArticles> loadNews() {
        List<NewsArticles> listNews = new ArrayList<>();
       try {
            File newsFolder = Toolkit.getFileFromResource("News");
            File[] files = newsFolder.listFiles();
            if (files != null) {
                List<File> fileList = List.of(files);
                fileList = fileList.stream().sorted(Comparator.comparing(File::getName)).toList();
                for (File file : fileList) {
                    if (!file.isDirectory()) {
                        String fileName = file.getName();
                        int lastDotIndex = fileName.lastIndexOf(".");
                        if (lastDotIndex > 0) {
                           String fileExtension = fileName.substring(lastDotIndex + 1);
                           if (fileExtension.equals("htm")){

                               String htmlText = Files.readString(file.toPath());
                               String title = HtmlParser.getNewsTitle(htmlText.toString());
                               String label = HtmlParser.getLabel(htmlText.toString());
                               NewsArticles.DataType type = HtmlParser.getDataType(htmlText.toString());
                               String content = HtmlParser.getNewsContent(htmlText.toString());
                               NewsArticles newsArticles = new NewsArticles(title,content,type,label);
                               listNews.add(newsArticles);

                           }
                        }
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return listNews;
    }

    public static List<String> getListVocabulary() {
        return listVocabulary;
    }

    public static List<double[]> getlistVectors() {
        return listVectors;
    }

    public static int binarySearch(List<String> sortedList, String target) {
        int low = 0;
        int high = sortedList.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int compareResult = target.compareTo(sortedList.get(mid));

            if (compareResult == 0) {
                return mid;
            } else if (compareResult < 0) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return -1;
    }

}
