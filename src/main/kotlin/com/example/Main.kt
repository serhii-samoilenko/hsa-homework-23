package com.example

import kotlin.system.measureTimeMillis

/**
 * Profiling demo
 *
 * This demo will run the benchmarking code, allowing the external profiler to measure the performance.
 */
fun main(args: Array<String>) {
    println("Ready to run benchmark. Please start the profiler and press enter.")
    readlnOrNull()

    insert_512K()
    insert_1024K()
    insert_2048K()
    insert_4096K()
    insert_8192K()
}

fun insert_512K() {
    val dataset = randomDataset(512_000).toList()
    pause()
    runTreePerformance(dataset) { tree, value -> tree.add(value) }
}

fun insert_1024K() {
    val dataset = randomDataset(1024_000).toList()
    pause()
    runTreePerformance(dataset) { tree, value -> tree.add(value) }
}

fun insert_2048K() {
    val dataset = randomDataset(2048_000).toList()
    pause()
    runTreePerformance(dataset) { tree, value -> tree.add(value) }
}

fun insert_4096K() {
    val dataset = randomDataset(4096_000).toList()
    pause()
    runTreePerformance(dataset) { tree, value -> tree.add(value) }
}

fun insert_8192K() {
    val dataset = randomDataset(8192_000).toList()
    pause()
    runTreePerformance(dataset) { tree, value -> tree.add(value) }
}

fun runTreePerformance(dataset: List<Int>, operation: (tree: AaTree<Int>, value: Int) -> Unit) {
    val duration = measureTimeMillis {
        val tree = AaTree<Int>()
        dataset.forEach { operation(tree, it) }
    }.toDouble()

    val kSize = dataset.size / 1000
    val oneKDuration = duration / kSize
    println("Count: $kSize K, Duration: $duration ms, 1K duration: $oneKDuration ms")
}

fun pause() = Thread.sleep(5000)

/**
 * Generates a lazily evaluated sequence of random integers within a specified range.
 * The resulting dataset is guaranteed to contain the minimum and maximum values.
 *
 * @param size the number of integers to generate.
 * @param min the minimum value of the integers to generate (inclusive).
 * @param max the maximum value of the integers to generate (inclusive).
 * @return a Sequence<Int> object that generates size random integers within the specified range.
 */
fun randomDataset(size: Int, min: Int = 1, max: Int = Int.MAX_VALUE): Sequence<Int> = sequence {
    yield(min)
    for (i in 1..size - 2) yield((min..max).random())
    yield(max)
}
