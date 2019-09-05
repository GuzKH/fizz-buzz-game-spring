package com.lunchee.fizzbuzz.game

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.MediaType.APPLICATION_STREAM_JSON
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

@ExperimentalCoroutinesApi
@WebFluxTest(controllers = [FizzBuzzGameController::class])
@ExtendWith(SpringExtension::class)
open class FizzBuzzGameControllerIT {

    @Autowired
    private lateinit var testClient: WebTestClient

    @MockBean
    private lateinit var game: FizzBuzzGame

    @Test
    fun `should return answers as array of strings`() {
        given(game.play(CountToNumber(5))).willReturn(answers("1", "2", "Fizz", "4", "Buzz"))

        val response: Flux<Answer> = testClient
            .get().uri("/game?countTo=5").accept(APPLICATION_STREAM_JSON).exchange()
            .expectStatus().isOk
            .returnResult(Answer::class.java)
            .responseBody

        StepVerifier.create(response)
            .expectNext(Answer("1"), Answer("2"), Answer("Fizz"), Answer("4"), Answer("Buzz"))
            .expectComplete()
            .verify()
    }

    private fun answers(vararg answers: String): Flow<Answer> {
        return answers.asSequence()
            .map { Answer(it) }
            .asFlow()
    }

    @Test
    fun `should return 422 Unprocessable Entity if Count To is less than 1`() {
        testClient.get().uri("/game?countTo=0").accept(APPLICATION_STREAM_JSON).exchange()
            .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
            .expectBody().jsonPath("$.message").isNotEmpty
    }
}