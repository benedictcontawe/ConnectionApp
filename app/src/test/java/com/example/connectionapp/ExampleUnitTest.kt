package com.example.connectionapp

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun checkList_isFirstRepeatedValues() {
        var letters : List<String> = listOf<String>("A","B","C","A","B","D") //println("Distinct ${letters.distinct().firstOrNull()}")
        println("First ${letters.groupingBy { it }.eachCount().filter { it.value > 1 }.firstNotNullOfOrNull { it.key }}")

        letters = listOf<String>("X","Y","Z","A","B","Y")
        println("Second ${letters.groupingBy { it }.eachCount().filter { it.value > 1 }.firstNotNullOfOrNull { it.key }}")

        letters = listOf<String>("A","B","C","D","E","F")
        println("Third ${letters.groupingBy { it }.eachCount().filter { it.value > 1 }.firstNotNullOfOrNull { it.key }}")

        assert(true)
    }
}