package com.lunchee.fizzbuzz.game

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class)
class FizzBuzzGameTest {

    private fun game() = FizzBuzzGame(player)

    @Mock
    private lateinit var player: FizzBuzzPlayer

    @Test
    fun `should return one value if counting to 1`() {
        given(player.giveAnswer(1)).willReturn(Answer("One?"))

        runBlocking {
            assertThat(game().play(CountToNumber(1)).toList())
                .containsExactly(Answer("One?"))
        }
    }

    @Test
    fun `should return two values if counting to 2`() {
        given(player.giveAnswer(anyInt())).willAnswer { Answer(it.arguments[0].toString()) }

        runBlocking {
            assertThat(game().play(CountToNumber(2)).toList())
                .containsExactly(Answer("1"), Answer("2"))
        }
    }
}