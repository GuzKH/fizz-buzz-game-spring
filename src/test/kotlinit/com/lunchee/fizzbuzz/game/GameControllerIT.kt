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
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParameters
import org.springframework.restdocs.snippet.Attributes.key
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExperimentalCoroutinesApi
@WebFluxTest(controllers = [GameController::class])
@ExtendWith(SpringExtension::class)
@AutoConfigureRestDocs
open class GameControllerIT {

    @Autowired
    private lateinit var testClient: WebTestClient

    @MockBean
    private lateinit var game: Game

    @Test
    fun `should return answers as a stream`() {
        given(game.play(CountToNumber(5))).willReturn(answersFlux("1", "2", "Fizz", "4", "Buzz"))

        testClient
            .get().uri("/game?countTo=5").accept(APPLICATION_STREAM_JSON).exchange()
            .expectStatus().isOk
            .expectBodyList(Answer::class.java)
            .hasSize(5)
            .contains(*answers("1", "2", "Fizz", "4", "Buzz"))
            .consumeWith<WebTestClient.ListBodySpec<Answer>>(
                document(
                    "game-ok",
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

    private fun answersFlux(vararg answers: String): Flow<Answer> {
        return answers.asSequence()
            .map { Answer(it) }
            .asFlow()
    }

    private fun answers(vararg answers: String): Array<Answer> {
        return answers.map { Answer(it) }.toTypedArray()
    }

    @Test
    fun `should return 422 Unprocessable Entity if Count To is less than 1`() {
        testClient.get().uri("/game?countTo=0").accept(APPLICATION_STREAM_JSON).exchange()
            .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
            .expectBody()
            .consumeWith(
                document(
                    "game-count-0",
                    responseFields(
                        fieldWithPath("code").description("HTTP Response Status Value"),
                        fieldWithPath("error").description("HTTP Response Status Name"),
                        fieldWithPath("message").description("Error message")
                    )
                )
            )
    }
}