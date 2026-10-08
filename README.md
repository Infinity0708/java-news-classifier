# Java News Classifier

Two independent implementations from an Object Oriented Programming assignment series. This repository shows a progression from TF-IDF similarity to GloVe-based supervised classification.

| Implementation | Representation | Classification approach |
| --- | --- | --- |
| [Assignment 1](assignment-1/) | TF-IDF document vectors | Cosine similarity to representative articles |
| [Assignment 2](assignment-2/) | GloVe-derived features | Dense neural network using Deeplearning4j |

## Structure

Each assignment has its own `pom.xml`, source code, resources, tests, and README. Open or build each assignment separately. `checks/` contains additional focused regression checks, and `docs/` records refactoring and validation.

## Run

Use JDK 17 and Maven. Open an assignment folder in IntelliJ, import its Maven configuration, and run its main class. Set the working directory to that assignment folder.

- Assignment 1: `uob.oop.NewsClassifier`
- Assignment 2: `uob.oop.AdvancedNewsClassifier`

Run `mvn test` within either assignment folder to execute the original tests. Assignment 2 downloads large CoreNLP model and native ND4J dependencies; native backend compatibility must be checked on the target platform.

## Project scope

These implementations originated from supplied coursework skeletons and tests. This cleaned version contains subsequent refactoring; it is not an unchanged submission archive. Original packages are retained to preserve imports and test compatibility.

GloVe features are static word representations, not contextual transformer embeddings. Assignment 2 preserves the submitted scalar mean per word representation; it does not retain the full 50-dimensional word vectors as neural-network features.

## Validation

Assignment 1 compiles and runs on Java 17. Focused TF-IDF, empty-text, HTML parsing, and grouping checks passed. Assignment 2's dependency-free median and parser checks passed. Full Maven tests, CoreNLP processing, and model training for Assignment 2 have not been run in this environment. No accuracy or performance improvement is claimed.

See [refactoring notes](docs/REFACTORING.md) for behavior changes and remaining limitations.
