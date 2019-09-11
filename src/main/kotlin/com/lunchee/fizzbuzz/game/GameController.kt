package com.lunchee.fizzbuzz.game

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactor.asFlux
import org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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
    @GetMapping(path = ["/answers"], produces = [APPLICATION_STREAM_JSON_VALUE])
    fun getAnswers(@RequestParam numbers: List<Int>): Flux<Answer> {
        return Flux.empty()
    }
}