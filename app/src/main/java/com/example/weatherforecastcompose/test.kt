package com.example.weatherforecastcompose

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Scanner


fun main(args: Array<String>) {
    with(Scanner(System.`in`)) {
        val programCounter = nextLine().toInt()
        val programPercent = nextLine().trim().split(' ').map { it.toInt() }
        println(100 /programPercent.sum())
    }
}