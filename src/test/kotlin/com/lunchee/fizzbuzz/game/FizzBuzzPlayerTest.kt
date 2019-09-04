package com.lunchee.fizzbuzz.game

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FizzBuzzPlayerTest {

    private fun player() = FizzBuzzPlayer()

    @Test
    fun `should answer "1" for 1`() {
        assertThat(player().giveAnswer(1)).isEqualTo("1")
    }

    @Test
    fun `should answer "2" for 2`() {
        assertThat(player().giveAnswer(2)).isEqualTo("2")
    }

    @Test
    fun `should answer "Fizz" for 3`() {
        assertThat(player().giveAnswer(3)).isEqualTo("Fizz")
    }

    @Test
    fun `should answer "Buzz" for 5`() {
        assertThat(player().giveAnswer(5)).isEqualTo("Buzz")
    }

    @Test
    fun `should answer "Fizz Buzz" for 15`() {
        assertThat(player().giveAnswer(15)).isEqualTo("Fizz Buzz")
    }
}