package com.lunchee.fizzbuzz.game

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.asFlux
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/game")
class GameController(private val game: Game) {

    @ExperimentalCoroutinesApi
    @GetMapping(produces = [APPLICATION_STREAM_JSON_VALUE])
    fun play(@RequestParam countTo: Int): Flux<Answer> {
        return game.play(CountToNumber(countTo)).asFlux()
    }

    @ExperimentalCoroutinesApi
    @PostMapping(
        path = ["/answers"],
        consumes = [APPLICATION_STREAM_JSON_VALUE],
        produces = [APPLICATION_STREAM_JSON_VALUE]
    )
    fun getAnswers(@RequestBody numbers: Flux<NumberToAnswer>): Flux<Answer> {
        return game.getAnswers(numbers.map { it.value }.asFlow()).asFlux()
    }

    @GetMapping(path = ["/answer/{number}"], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun getAnswer(@PathVariable number: Int): Answer {
        return game.getAnswer(number)
    }
}

data class NumberToAnswer(val value: Int)