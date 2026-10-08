# Assignment 1 — TF-IDF News Classifier

A Java pipeline for 20 local news articles about space exploration and cryptocurrency.

## Features

- HTML title and body extraction.
- Lowercase text cleaning, suffix stripping, and stop-word filtering.
- Shared vocabulary and TF-IDF vectorisation: TF is token frequency divided by document token count; IDF is `ln(N / DF) + 1`.
- Vector operations, cosine similarity ranking, and two-reference topic grouping.

The method named `textLemmatization` is simplified suffix stripping, not linguistic lemmatization.

## Run

Open this folder in IntelliJ with JDK 17 and run `uob.oop.NewsClassifier`, using this folder as the working directory.

```bash
mkdir -p target/classes
javac -d target/classes src/main/java/uob/oop/*.java
java -cp target/classes:src/main/resources uob.oop.NewsClassifier
```

Run original tests using `mvn test`. Run additional regression checks from this folder:

```bash
javac -d target/classes src/main/java/uob/oop/*.java ../checks/Assignment1Checks.java
java -cp target/classes:src/main/resources Assignment1Checks
```

Use `;` instead of `:` for classpaths on Windows.

## Limitations

HTML parsing is specific to the supplied format. TF-IDF ignores word order. Tied reference similarity scores remain unassigned in the original grouping logic. Zero-norm cosine vectors remain a limitation of the retained Vector implementation.
