package com.lunchee.fizzbuzz.game

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.MediaType.*
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.restdocs.snippet.Attributes.key
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux

@ExperimentalCoroutinesApi
@WebFluxTest(controllers = [GameController::class])
@AutoConfigureRestDocs
open class GameControllerIT {

    @MockkBean
    private lateinit var game: Game

    @Autowired
    private lateinit var testClient: WebTestClient

    @Test
    fun `when playing game, should return answers as a stream`() {
        every { game.play(CountToNumber(5)) } returns answersFlow("1", "2", "Fizz", "4", "Buzz")

        testClient
            .get().uri("/game?countTo=5").accept(APPLICATION_STREAM_JSON).exchange()
            .expectStatus().isOk
            .expectHeader().contentType("application/stream+json")
            .expectBodyList(Answer::class.java)
            .hasSize(5)
            .contains(*answers("1", "2", "Fizz", "4", "Buzz"))
            .consumeWith<WebTestClient.ListBodySpec<Answer>>(
                document(
                    "game-start",
                    requestParameters(
                        parameterWithName("countTo")
                            .description("Number to play up to, inclusive.")
                            .attributes(key("constraints").value("Must be greater than zero"))
                    ),
                    responseFields(
                        fieldWithPath("value").description("Answer of a Player")
                    )
                )
            )
    }

    private fun answersFlow(vararg answers: String): Flow<Answer> {
        return answers.asSequence()
            .map { Answer(it) }
            .asFlow()
    }

    private fun answers(vararg answers: String): Array<Answer> {
        return answers.map { Answer(it) }.toTypedArray()
    }

    @Test
    fun `when playing game, should return 422 Unprocessable Entity if Count To is less than 1`() {
        testClient.get().uri("/game?countTo=0").accept(APPLICATION_STREAM_JSON).exchange()
            .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
            .expectBody()
            .consumeWith(
                document(
                    "game-start-illegal-count",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("timestamp").description("Timestamp of an Error"),
                        fieldWithPath("path").description("Requested Path"),
                        fieldWithPath("status").description("HTTP Response Status Value"),
                        fieldWithPath("error").description("HTTP Response Status Name"),
                        fieldWithPath("message").description("Error Message"),
                        fieldWithPath("requestId").description("Request ID")
                    )
                )
            )
    }

    @Test
    fun `when requesting an answer for a single number, should return a single answer`() {
        every { game.getAnswer(42) } returns Answer("42")

        testClient
            .get().uri("/game/answer/{number}", mapOf("number" to 42)).accept(APPLICATION_JSON).exchange()
            .expectStatus().isOk
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody().jsonPath("$.value").isEqualTo("42")
            .consumeWith(
                document(
                    "get-single-answer",
                    pathParameters(parameterWithName("number").description("A number to get an answer for")),
                    responseFields(
                        fieldWithPath("value").description("Answer for a number")
                    )
                )
            )
    }

    @Test
    fun `when requesting answers for multiple numbers, should return a stream of as much answers as was requested`() {
        every {
            game.getAnswers(numbers = capture(capturedNumbers))
        } coAnswers {
            capturedNumbers.captured.toList()
            answersFlow("1", "Buzz", "Fizz", "10")
        }

        testClient
            .post().uri("/game/answers")
            .contentType(APPLICATION_STREAM_JSON)
            .body(fluxOfNumbersToAnswer(1, 5, 3, 10), NumberToAnswer::class.java)
            .accept(APPLICATION_STREAM_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Answer::class.java)
            .hasSize(4)
            .contains(*answers("1", "Buzz", "Fizz", "10"))
            .consumeWith<WebTestClient.ListBodySpec<Answer>>(
                document(
                    "get-multiple-answers",
                    requestFields(
                        fieldWithPath("value").description("Value of a number to answer")
                    )
                )
            )
    }

    private fun fluxOfNumbersToAnswer(vararg numbers: Int): Flux<NumberToAnswer> {
        return Flux.fromIterable(numbers.map { NumberToAnswer(it) })
    }

    private val capturedNumbers = slot<Flow<Int>>()
}