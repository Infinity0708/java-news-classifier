package uob.oop;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

public class ArticlesEmbedding extends NewsArticles {
    private boolean textProcessed = false;
    private INDArray cachedEmbedding;
    private List<Glove> cachedGloveSource;
    private int intSize = -1;

    private static class PipelineHolder {
        private static final StanfordCoreNLP PIPELINE = createPipeline();
        private static StanfordCoreNLP createPipeline() {
            Properties properties = new Properties();
            properties.setProperty("annotators", "tokenize,pos,lemma");
            return new StanfordCoreNLP(properties);
        }
    }
    private String processedText = "";

    private INDArray newsEmbedding = Nd4j.create(0);

    public ArticlesEmbedding(String _title, String _content, NewsArticles.DataType _type, String _label) {
        super(_title,_content,_type,_label);
    }

    public void setEmbeddingSize(int _size) {
        if (_size <= 0) throw new IllegalArgumentException("Embedding size must be positive");
        if (intSize != _size) cachedEmbedding = null;
        this.intSize = _size;

    }

    public int getEmbeddingSize(){
        return intSize;
    }

    @Override
    public String getNewsContent() {
        if (!textProcessed) {
            String text = textCleaning(super.getNewsContent());
            CoreDocument document;
            synchronized (PipelineHolder.PIPELINE) {
                document = PipelineHolder.PIPELINE.processToCoreDocument(text);
            }
            StringBuilder sb = new StringBuilder();
            for (CoreLabel tok : document.tokens()) {
                boolean flag = true;
                for (String stop : Toolkit.STOPWORDS) {
                    if (stop.equals(tok.lemma().toLowerCase(Locale.ROOT))) {
                        flag = false;
                        break;
                    }
                }
                if (flag) sb.append(tok.lemma().toLowerCase(Locale.ROOT)).append(" ");
            }
            processedText = sb.toString().trim();
            textProcessed = true;
        }
        return processedText.trim();
    }

    public INDArray getEmbedding() throws Exception {
        if (intSize <= 0) throw new InvalidSizeException("Invalid size");
        if (processedText.isEmpty()) throw new InvalidTextException("Invalid text");
        List<Glove> source = AdvancedNewsClassifier.listGlove;
        if (source == null) throw new IllegalStateException("Load GloVe before embedding articles");
        if (cachedEmbedding != null && cachedGloveSource == source) return cachedEmbedding;

        Map<String, double[]> vectors = new HashMap<>();
        for (Glove glove : source) vectors.put(glove.getVocabulary(), glove.getVector().getAllElements());
        double[][] features = new double[1][intSize];
        int position = 0;
        for (String word : processedText.split("\\s+")) {
            if (position == intSize) break;
            double[] vector = vectors.get(word);
            if (vector == null || vector.length == 0) continue;
            double sum = 0;
            for (double value : vector) sum += value;
            features[0][position++] = sum / vector.length;
        }
        cachedEmbedding = Nd4j.create(features);
        newsEmbedding = cachedEmbedding;
        cachedGloveSource = source;
        return cachedEmbedding;
    }

    /***
     * Clean the given (_content) text by removing all the characters that are not 'a'-'z', '0'-'9' and white space.
     * @param _content Text that need to be cleaned.
     * @return The cleaned text.
     */
    private static String textCleaning(String _content) {
        StringBuilder sbContent = new StringBuilder();

        for (char c : _content.toLowerCase().toCharArray()) {
            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || Character.isWhitespace(c)) {
                sbContent.append(c);
            }
        }

        return sbContent.toString().trim();
    }
}
