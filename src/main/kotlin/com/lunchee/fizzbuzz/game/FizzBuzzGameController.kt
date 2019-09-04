package com.lunchee.fizzbuzz.game

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactor.asFlux
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/game")
class FizzBuzzGameController(private val game: FizzBuzzGame) {

    @ExperimentalCoroutinesApi
    @GetMapping
    fun play(@RequestParam countTo: Int): Flux<String> {
        return game.play(CountToNumber(countTo)).asFlux()
    }
}