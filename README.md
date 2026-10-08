# News Classifier — Assignment 1

A Java news classification project developed for the first assignment of an Object Oriented Programming course. It introduces fundamental Natural Language Processing (NLP) techniques through text preprocessing, TF-IDF vectorisation, cosine similarity, and topic-based grouping.

The project uses 20 local news articles covering space exploration and cryptocurrency.

## Project Objectives

- Extract useful text from HTML news articles.
- Prepare text for numerical analysis.
- Represent documents using TF-IDF vectors.
- Measure and rank article similarity.
- Group articles by comparing them with two representative documents.
- Organise the implementation into reusable Java classes.

## Processing Pipeline

1. Load local HTML articles and the stop-word list.
2. Extract article titles and body text.
3. Clean and lowercase the text.
4. Apply rule-based word suffix removal.
5. Remove stop words.
6. Build a vocabulary across all processed articles.
7. Calculate a TF-IDF vector for each article.
8. Calculate cosine similarity and rank related articles.
9. Assign articles to topic groups using two representative articles.

## NLP Concepts

### HTML Text Extraction

The parser extracts article titles from HTML title tags and article content from the `articleBody` field.

This parser is designed for the supplied HTML format rather than arbitrary websites.

### Text Cleaning and Tokenisation

Text is converted to lowercase, and characters other than letters, digits, and spaces are removed. The resulting text is split into word tokens.

### Rule-Based Word Normalisation

The method named `textLemmatization` checks the suffixes `ing`, `ed`, `es`, and `s` in that order and removes the first matching suffix.

Examples include:

- `playing` → `play`
- `helped` → `help`
- `apples` → `appl`
- `bananas` → `banana`

Although the assignment calls this lemmatization, the implementation is a simplified suffix-stripping method, closer to stemming. It does not use a dictionary, grammatical context, or part-of-speech information.

### Stop-Word Removal

Common words are removed using a supplied stop-word list, allowing the document representation to focus on more informative terms.

### Vocabulary Construction

A shared vocabulary contains the unique terms found across the processed articles. Each vocabulary term corresponds to one dimension of a document vector.

### TF-IDF Vectorisation

Term Frequency (TF) measures how often a term occurs relative to the total number of tokens in a document.

Inverse Document Frequency (IDF) gives greater weight to terms that appear in fewer documents.

The implementation uses:

- `TF(t, d) = count(t, d) / total_tokens(d)`
- `IDF(t) = ln(N / DF(t)) + 1`
- `TF-IDF(t, d) = TF(t, d) × IDF(t)`

Here, `N` is the number of documents, `DF(t)` is the number of documents containing the term, and `ln` is the natural logarithm.

### Cosine Similarity

Cosine similarity compares the direction of two document vectors:

`similarity(A, B) = dot(A, B) / (norm(A) × norm(B))`

For nonzero TF-IDF vectors, a higher score indicates greater similarity in their weighted vocabulary.

### Topic Grouping

Two representative articles act as references for the topic groups. Each article is compared with both references and assigned to the group with the higher similarity score.

This is a similarity-based approach rather than a classifier trained on labelled data.

## Object Oriented Programming Concepts

- Classes and objects.
- Constructors and instance fields.
- Static utility methods.
- Separation of responsibilities.
- Composition of cooperating objects.
- Arrays and two-dimensional numerical data.
- Reusable methods for vector operations and result formatting.

## Main Classes

| Class | Responsibility |
| --- | --- |
| `HtmlParser` | Extract article titles and content from HTML strings. |
| `NLP` | Clean text, remove suffixes, and filter stop words. |
| `Vector` | Provide vector access, resizing, arithmetic, dot products, and cosine similarity. |
| `NewsClassifier` | Coordinate preprocessing, vocabulary construction, TF-IDF calculation, ranking, and grouping. |
| `Toolkit` | Load local news files and stop words. |

The project was developed from a supplied coursework skeleton, which included supporting code and tests.

## Technologies

- Java 17
- Maven
- JUnit
- Local HTML and CSV resources

The main application uses Java's standard library without an external NLP framework.

## Repository Structure

- `pom.xml` — Maven configuration.
- `src/main/java/uob/oop/` — application source code.
- `src/main/resources/News/` — local news articles.
- `src/main/resources/stopwords.csv` — stop-word list.
- `src/test/java/` — coursework tests.
- `.gitignore` — excludes IDE settings and generated build files.

## Running the Application

### IntelliJ IDEA

1. Open the project folder containing `pom.xml`.
2. Select JDK 17.
3. Load the Maven project and its dependencies.
4. Set the working directory to the project root.
5. Run the `main` method in `uob.oop.NewsClassifier`.

### macOS / Linux Terminal

Run the following commands from the project root:

```bash
mkdir -p target/classes
javac -d target/classes src/main/java/uob/oop/*.java
java -cp target/classes:src/main/resources uob.oop.NewsClassifier
```

On Windows, replace the classpath separator `:` with `;`.

## Application Output

The application prints:

- The top 10 similarity results for a selected article, including the article itself.
- Article indexes, similarity scores, and titles.
- The number of articles assigned to each topic group.
- The titles of articles in each group.

## Testing

The repository includes the following test classes:

- `Tester_HtmlParser`
- `Tester_NLP`
- `Tester_Vector`
- `Tester_NewsClassifier`

These can be run individually from IntelliJ IDEA. Their filenames may require explicit configuration for automatic discovery by a Maven test runner.

No verified test pass count or classification accuracy is reported in this README.

## Limitations

- The dataset contains only 20 articles across two broad topics.
- HTML extraction depends on the supplied page format.
- Word normalisation uses simple suffix rules.
- TF-IDF captures weighted word overlap rather than contextual meaning.
- Grouping depends on the selected representative articles.
- The original implementation leaves articles unassigned when both group similarity scores are equal.

## Assignment Series

This repository covers Assignment 1: the foundational Java NLP pipeline for news similarity and topic grouping.
