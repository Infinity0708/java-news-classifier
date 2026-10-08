# Assignment 2 — Advanced News Classifier

A supervised Java news classifier using Stanford CoreNLP, pretrained GloVe vectors, ND4J, and Deeplearning4j. The supplied dataset contains 32 HTML articles with training/testing metadata.

## Pipeline

1. Load news articles and the reduced GloVe CSV.
2. Clean text and use CoreNLP for lemmatization.
3. Remove stop words and count tokens supported by GloVe.
4. Select a fixed input length from the median supported-token count.
5. Convert each supported word vector to its scalar mean, truncate long documents, and zero-pad shorter documents.
6. Construct one-hot training labels and train the supplied dense neural network.
7. Predict testing labels and print article groups.

The scalar mean representation is retained from the submitted implementation and supplied test expectations. It loses much of the information in the original 50-dimensional vectors. The network is not a transformer or a sequence model.

## Object oriented design

`ArticlesEmbedding` extends `NewsArticles` and overrides content access. Supporting classes model words and vectors, parse metadata, load data, and define custom exceptions.

## Run

Open this folder in IntelliJ, use JDK 17, import Maven dependencies, and run `uob.oop.AdvancedNewsClassifier`. Use this folder as the working directory. CoreNLP models and the ND4J native backend must be installed through Maven, with platform compatibility checked locally.

Run the original tests with `mvn test`. Full training and original tests have not been verified for this refactoring. Updated median indexing may change model input size and prediction outputs relative to the original submission.

Dependency-free checks can be run from this folder:

```bash
mkdir -p target/checks
javac -d target/checks src/main/java/uob/oop/DocumentLengths.java src/main/java/uob/oop/NewsArticles.java src/main/java/uob/oop/HtmlParser.java ../checks/Assignment2Checks.java
java -cp target/checks Assignment2Checks
```

## Remaining limitations

The median is calculated across all supplied articles, including testing documents, to preserve the assignment workflow. A rigorous evaluation should derive preprocessing configuration from training data only. The original network settings and small supplied dataset are retained; no evaluation accuracy is claimed. Embedding caches assume the loaded GloVe list and vectors are not mutated in place.
