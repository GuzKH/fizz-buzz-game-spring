package com.lunchee.fizzbuzz.game

import org.springframework.stereotype.Service

@Service
class FizzBuzzPlayer {
    fun giveAnswer(number: Int): String {
        val answers = arrayListOf<String>()

        if (number % 3 == 0) answers.add("Fizz")
        if (number % 5 == 0) answers.add("Buzz")
        if (answers.isEmpty()) answers.add(number.toString())

        return answers.joinToString(separator = " ")
    }
}