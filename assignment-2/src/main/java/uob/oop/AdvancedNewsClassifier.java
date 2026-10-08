package uob.oop;

import org.apache.commons.lang3.time.StopWatch;
import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.WorkspaceMode;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class AdvancedNewsClassifier {
    public Toolkit myTK = null;
    public static List<NewsArticles> listNews = null;
    public static List<Glove> listGlove = null;
    public List<ArticlesEmbedding> listEmbedding = null;
    public MultiLayerNetwork myNeuralNetwork = null;

    public final int BATCHSIZE = 10;

    public int embeddingSize = 0;
    private static StopWatch mySW = new StopWatch();

    public AdvancedNewsClassifier() throws IOException {
        myTK = new Toolkit();
        myTK.loadGlove();
        listNews = myTK.loadNews();
        listGlove = createGloveList();
        listEmbedding = loadData();
    }

    public static void main(String[] args) throws Exception {
        mySW.start();
        AdvancedNewsClassifier myANC = new AdvancedNewsClassifier();

        myANC.embeddingSize = myANC.calculateEmbeddingSize(myANC.listEmbedding);
        myANC.populateEmbedding();
        myANC.myNeuralNetwork = myANC.buildNeuralNetwork(2);
        myANC.predictResult(myANC.listEmbedding);
        myANC.printResults();
        mySW.stop();
        System.out.println("Total elapsed time: " + mySW.getTime());
    }

    public List<Glove> createGloveList() {
        List<Glove> listResult = new ArrayList<>();
        for (int i = 0; i < Toolkit.listVocabulary.size(); i++) {
            Glove glove = new Glove(Toolkit.listVocabulary.get(i), new Vector(Toolkit.listVectors.get(i)));
            boolean flag = true;
            for (String stop : Toolkit.STOPWORDS) {
                if (stop.equals(Toolkit.listVocabulary.get(i))) {
                    flag = false;
                    break;
                }
            }
            if (flag) listResult.add(glove);
        }
        return listResult;
    }

    public static List<ArticlesEmbedding> loadData() {
        List<ArticlesEmbedding> listEmbedding = new ArrayList<>();
        for (NewsArticles news : listNews) {
            ArticlesEmbedding myAE = new ArticlesEmbedding(news.getNewsTitle(), news.getNewsContent(), news.getNewsType(), news.getNewsLabel());
            listEmbedding.add(myAE);
        }
        return listEmbedding;
    }

    public int calculateEmbeddingSize(List<ArticlesEmbedding> articles) {
        if (articles.isEmpty()) throw new IllegalArgumentException("No articles available");
        Set<String> vocabulary = new HashSet<>();
        for (Glove glove : listGlove) vocabulary.add(glove.getVocabulary());
        List<Integer> lengths = new ArrayList<>();
        for (ArticlesEmbedding article : articles) {
            int count = 0;
            for (String word : article.getNewsContent().split("\\s+")) {
                if (vocabulary.contains(word)) count++;
            }
            lengths.add(count);
        }
        int median = DocumentLengths.median(lengths);
        if (median <= 0) throw new IllegalArgumentException("No usable median embedding length");
        return median;
    }

    public void populateEmbedding() {
        listEmbedding.forEach(this::populateEmbedding);
    }

    public void populateEmbedding(ArticlesEmbedding article) {
        article.setEmbeddingSize(embeddingSize);
        article.getNewsContent();
        try {
            article.getEmbedding();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot embed article: " + article.getNewsTitle(), e);
        }
    }

    public DataSetIterator populateRecordReaders(int _numberOfClasses) throws Exception {
        ListDataSetIterator myDataIterator = null;
        List<DataSet> listDS = new ArrayList<>();
        INDArray inputNDArray = null;
        INDArray outputNDArray = null;

        for (ArticlesEmbedding articlesEmbedding : listEmbedding) {
            if (articlesEmbedding.getNewsType() == NewsArticles.DataType.Training) {
                outputNDArray = Nd4j.create(1, _numberOfClasses);
                outputNDArray.put(0, Integer.parseInt(articlesEmbedding.getNewsLabel()) - 1, 1);
                inputNDArray = articlesEmbedding.getEmbedding();
                DataSet myDataSet = new DataSet(inputNDArray, outputNDArray);
                listDS.add(myDataSet);
            }
        }

        return new ListDataSetIterator(listDS, BATCHSIZE);
    }

    public MultiLayerNetwork buildNeuralNetwork(int _numOfClasses) throws Exception {
        DataSetIterator trainIter = populateRecordReaders(_numOfClasses);
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(42)
                .trainingWorkspaceMode(WorkspaceMode.ENABLED)
                .activation(Activation.RELU)
                .weightInit(WeightInit.XAVIER)
                .updater(Adam.builder().learningRate(0.02).beta1(0.9).beta2(0.999).build())
                .l2(1e-4)
                .list()
                .layer(new DenseLayer.Builder().nIn(embeddingSize).nOut(15)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.HINGE)
                        .activation(Activation.SOFTMAX)
                        .nIn(15).nOut(_numOfClasses).build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();

        for (int n = 0; n < 100; n++) {
            model.fit(trainIter);
            trainIter.reset();
        }
        return model;
    }

    public List<Integer> predictResult(List<ArticlesEmbedding> _listEmbedding) throws Exception {
        List<Integer> listResult = new ArrayList<>();
        for (ArticlesEmbedding articlesEmbedding : _listEmbedding) {
            if (articlesEmbedding.getNewsType() == NewsArticles.DataType.Testing) {
                int[] resultArr = myNeuralNetwork.predict(articlesEmbedding.getEmbedding());
                listResult.add(resultArr[0]);
                articlesEmbedding.setNewsLabel(String.valueOf(resultArr[0] + 1));

            }

        }

        return listResult;
    }

    public void printResults() {
        List<StringBuilder> list = new ArrayList<>();
        for (ArticlesEmbedding articlesEmbedding : listEmbedding) {
            if (articlesEmbedding.getNewsType() == NewsArticles.DataType.Testing) {
                while (list.size() - 1 < Integer.parseInt(articlesEmbedding.getNewsLabel())) {
                    list.add(new StringBuilder());
                }
                list.get(Integer.parseInt(articlesEmbedding.getNewsLabel())).append(articlesEmbedding.getNewsTitle()).append("\r\n");
            }
        }
        for (int i = 1; i < list.size(); i++) {
            System.out.println("Group " + i);
            System.out.println(list.get(i).toString().trim());
        }
    }
}
