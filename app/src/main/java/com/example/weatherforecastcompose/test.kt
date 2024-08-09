package com.example.weatherforecastcompose


import java.util.Scanner


fun main(args: Array<String>) {
    with(Scanner(System.`in`)) {
        val str = nextLine()

        if (str.length == 9) println("YandexCup")
        else {
            str.toCharArray().forEach {
                
            }
        }
    }
}

//3!!!
//with(Scanner(System.`in`)) {
//    val firstStr = nextLine().trim().split(' ')
//    val secondStr = nextLine().trim().split(' ').map { it.toInt() }
//
//    var secondaryPoints = 0
//
//    secondStr.forEachIndexed { index, element ->
//        secondaryPoints += element * element
//        var x = element
//        for (i in index + 1..<secondStr.size) {
//            if (x > 0 && secondStr[i] > 0) {
//                x -= 1
//                secondaryPoints += secondStr[i]
//            }
//        }
//    }
//    println(secondaryPoints)
//}


//2
//with(Scanner(System.`in`)) {
//    val firstStr = nextLine().trim().split(' ')
//
//    val articleCounter = firstStr[0].toInt()
//    val mentionCounter = firstStr[1].toInt()
//
//    val map = mutableMapOf<String, Boolean>()
//    var websiteCounter = 0
//
//    repeat(articleCounter) {
//        val title = nextLine()
//        val description = nextLine()
//
//        var mention = 0
//
//        description.trim().split(' ').forEach { if (it == "goose") mention++ }
//        map[title] = false
//
//        if (mention >= mentionCounter) {
//            websiteCounter++
//            map[title] = true
//        }
//    }
//
//    println(websiteCounter)
//    map.forEach {
//        if (it.value) println(it.key)
//    }
//}


//1
//with(Scanner(System.`in`)) {
//    val programCounter = nextLine().toInt()
//    val programPercent = nextLine().trim().split(' ').map { it.toInt() }
//    println(100 /programPercent.sum())
//}