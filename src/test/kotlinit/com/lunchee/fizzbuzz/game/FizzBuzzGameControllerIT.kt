package com.lunchee.fizzbuzz.game

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
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
        given(game.play(CountToNumber(5))).willReturn(flowOf("1", "2", "Fizz", "4", "Buzz"))

        val response: Flux<String> = testClient
            .get().uri("/game?countTo=5").accept(APPLICATION_STREAM_JSON).exchange()
            .expectStatus().isOk
            .returnResult(String::class.java).responseBody

        StepVerifier.create(response)
            .expectNext("1", "2", "Fizz", "4", "Buzz")
            .expectComplete()
    }

    @Test
    fun `should return 422 Unprocessable Entity if Count To is less than 1`() {
        testClient.get().uri("/game?countTo=0").accept(APPLICATION_STREAM_JSON).exchange()
            .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
            .expectBody().jsonPath("$.message").isNotEmpty
    }
}