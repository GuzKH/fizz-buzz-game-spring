package com.lunchee.fizzbuzz.game

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CountToNumberTest {

    @Test
    fun `can be 1`() {
        assertThat(CountToNumber(1).value).isEqualTo(1)
    }

    @Test
    fun `can be greater than 1`() {
        assertThat(CountToNumber(123).value).isEqualTo(123)
    }

    @Test
    fun `cannot be 0`() {
        assertThrows<IllegalArgumentException> {
            CountToNumber(0)
        }
    }

    @Test
    fun `cannot be less than 1`() {
        assertThrows<IllegalArgumentException> {
            CountToNumber(-1)
        }
    }
}