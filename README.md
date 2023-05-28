# Highload Software Architecture 8 Lesson 19 Homework

Data Structures and Algorithms
---

## Test project setup

The demo is written in Kotlin and contains custom implementation of the Tree and Sorting algorithms.

The `com.example.DemoKt.runDemo` function is used to run benchmarks for the algorithms.

The summary of the results is located in the [REPORT.md](reports/REPORT.md) file.

### Tree implementation

The tree implementation is located in the [AaTree.kt](src/main/kotlin/com/example/AaTree.kt) file.

This is a simplified version of the Black-Red tree that uses levels instead of colors and only two balancing operations.

Additional information on the Wikipedia page: [AA Tree](https://en.wikipedia.org/wiki/AA_tree)

### Sorting algorithm

The sorting algorithm is located in the [CountingSort.kt](src/main/kotlin/com/example/CountingSort.kt) file.

### Datasets

The test datasets generator functions are located in the [DataSets.kt](src/main/kotlin/com/example/Datasets.kt) file.

Various datasets are used to test the Tree implementation, and to test the Sorting algorithm only the Random dataset is used.

## How to build and run

Build and run demo application (Requires Java 11+)

```shell script
./gradlew build && \
java -jar build/libs/hsa19-1.0-SNAPSHOT.jar
```

## Results and Conclusions

The [REPORT.md](reports/REPORT.md) contains tables with the results of the benchmarks. Charts can be found in the [Spreadsheet](https://docs.google.com/spreadsheets/d/13pbKhG1CDDi8sM9jNHGe-5fmxqrJU_c935q5E8210K0/edit?usp=sharing).

### AA Tree

The AA Tree shows logarithmic complexity for all datasets, with worst case performance for the Random dataset.

### Counting Sort

The Counting Sort appears to be very fast for datasets with small spread (range of values), but its performance degrades quickly as the range of values increases.
