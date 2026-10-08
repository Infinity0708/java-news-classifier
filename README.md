# Java News Classifier

A University of Birmingham Object Oriented Programming coursework project (2023), implementing a news similarity and topic grouping pipeline in Java.

## Features

- Extract article titles and content from local HTML files.
- Clean and lowercase text, apply rule-based suffix removal, and filter stop words.
- Build a vocabulary and calculate TF-IDF document vectors.
- Implement vector operations and cosine similarity.
- Rank related news and assign articles to two groups using representative articles.

## Technology

Java 17, Maven, JUnit. Production source code does not require an external NLP library.

## Dataset and method

The supplied coursework dataset contains 20 Sky News HTML articles about space exploration and cryptocurrency. TF is the term count divided by the document word count; IDF is ln(N / document_frequency) + 1. Grouping compares each article with two representative articles. This is a small similarity-based coursework classifier; no trained neural model or financial risk classification is claimed.

The method named `textLemmatization` implements suffix stripping rather than linguistic lemmatization. Ties in group similarity are not assigned to either group by the original implementation.

## Run

Open this folder (the folder containing `pom.xml`) in IntelliJ IDEA, select JDK 17, load the Maven project, and run `uob.oop.NewsClassifier`. Set the working directory to this project root so local news files can be found.

Alternatively, with JDK 17 available, run from the project root:

```bash
mkdir -p target/classes
javac -d target/classes src/main/java/uob/oop/*.java
java -cp target/classes:src/main/resources uob.oop.NewsClassifier
```

The classpath separator above is for macOS/Linux; use `;` instead of `:` on Windows.

## Tests

The original coursework tests are retained under `src/test/java`. Run the `Tester_*` classes from IntelliJ. No passing test count or accuracy score is claimed in this archive. Some Maven test runners may not automatically discover these filenames.

## Repository contents

- `src/main/java/uob/oop/`: Java source files.
- `src/main/resources/`: original coursework news files and stop words.
- `src/test/java/`: supplied coursework tests.
- `docs/assignment-instructions-v2.1.pdf`: the available assignment brief. The course page screenshot mentions a newer V2.5, which is not included here.
- `pom.xml`: original Maven configuration.

## Origin and sharing

This archive preserves the submitted source files, tests, resources, and Maven configuration without changing their code. The coursework was based on a university-provided skeleton; this repository does not claim that every supplied file was authored by the student. The original skeleton is not available here for an exact authorship comparison.

Keep this repository private while checking the course rules on publishing solutions. The supplied brief prohibits sharing code. University materials and third-party news content are not relicensed by this repository.
