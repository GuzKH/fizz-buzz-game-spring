package com.lunchee.fizzbuzz.game

import org.springframework.stereotype.Service

@Service
class Player {
    fun giveAnswer(number: Int): Answer {
        val answers = arrayListOf<String>()

        if (number % 3 == 0) answers.add("Fizz")
        if (number % 5 == 0) answers.add("Buzz")
        if (answers.isEmpty()) answers.add(number.toString())

        return Answer(answers.joinToString(separator = " "))
    }
}