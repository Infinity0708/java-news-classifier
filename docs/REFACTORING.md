# Refactoring and validation

## Source

Prepared from the two uploaded submission ZIPs. GitHub cloning failed because authentication was unavailable. This snapshot does not include later GitHub edits and has no remote commit history. Original ZIPs remain separate backups. This package contains no Git credentials or `.git` folder.

## Changes

- Split both Maven projects into independent assignment directories.
- Exclude IntelliJ configuration, compiled output, ZIP archives, and assignment PDFs from the code repository snapshot.
- Remove completed TODO markers and commented-out production code.
- Add repository overview, per-assignment run instructions, and validation notes.
- Pin JUnit Jupiter to 5.9.3 instead of RELEASE, and configure Surefire 3.1.2 to discover both original test naming patterns.
- Assignment 1: tokenize each document once, count document frequency once, and preserve insertion-order vocabulary. Exclude empty tokens from vocabulary and term totals. Empty documents produce zero feature rows.
- Assignment 1: return the existing missing-content sentinel for an unterminated article body rather than throwing from substring.
- Assignment 2: correct zero-based odd/even median indexing, use a vocabulary set for supported-token counting, and reject an unusable input length.
- Assignment 2: reuse a lazily initialized CoreNLP pipeline under synchronization. Cache processed text even when it is empty, and reuse embeddings until input size or the GloVe list reference changes.
- Assignment 2: retain scalar mean features per supported word; use direct arithmetic instead of allocating ND4J arrays for every word.
- Assignment 2: replace recursive embedding recovery with explicit preprocessing and clear failure propagation.
- Assignment 2: close the GloVe file reader with try-with-resources and preserve the IOException cause.
- Assignment 2: guard missing opening HTML tags and accept titles without a publisher suffix.

## Behavior changes

Assignment 1 similarity scores may differ because empty tokens from repeated spaces are no longer treated as terms. The supplied 20 articles still form two groups of 10 in the verified run. Assignment 2's corrected median may change neural-network input length and predictions. The submitted model architecture is otherwise preserved.

## Verified here

- Assignment 1: all production Java sources compiled with the Java 17 compiler module; application ran successfully.
- Assignment 1 checks: known TF-IDF values, document frequency, missing terms, empty document/vocabulary handling, malformed HTML, and supplied-corpus group count passed.
- Assignment 2 checks: odd/even, single/two-article median, empty corpus rejection, malformed HTML, and title parsing passed.
- Both Maven files are well-formed XML; resource files and original test files are byte-for-byte preserved from their respective ZIPs.

## Not verified here

Full Maven dependency resolution, original JUnit suites, CoreNLP preprocessing, native ND4J loading, neural-network training, and performance benchmarks. Run both Maven suites on the target machine before merging this snapshot into an existing repository.
