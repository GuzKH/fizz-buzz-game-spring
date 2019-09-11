package com.lunchee.fizzbuzz.game

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class GameTest {

    private fun game() = Game(player)

    @MockK
    private lateinit var player: Player

    @Test
    fun `should return one value if counting to 1`() {
        every { player.giveAnswer(1) } returns Answer("One?")

        runBlocking {
            assertThat(game().play(CountToNumber(1)).toList())
                .containsExactly(Answer("One?"))
        }
    }

    @Test
    fun `should return two values if counting to 2`() {
        every {
            player.giveAnswer(number = capture(capturedNumber))
        } answers { Answer(capturedNumber.captured.toString()) }

        runBlocking {
            assertThat(game().play(CountToNumber(2)).toList())
                .containsExactly(Answer("1"), Answer("2"))
        }
    }

    private val capturedNumber = slot<Int>()

    @Test
    fun `should return as many answers as was requested`() {
        every {
            player.giveAnswer(number = capture(capturedNumber))
        } answers { Answer(capturedNumber.captured.toString()) }

        runBlocking {
            assertThat(game().getAnswers(flowOf(1, 2, 3)).toList())
                .containsExactly(Answer("1"), Answer("2"), Answer("3"))
        }
    }

    @Test
    fun `should return an answer for a single number`() {
        every { player.giveAnswer(1) } returns Answer("1")

        assertThat(game().getAnswer(1)).isEqualTo(Answer("1"))
    }
}