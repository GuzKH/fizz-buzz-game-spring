package com.lunchee.fizzbuzz.game

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/game")
class GameController(private val game: Game) {

    @ExperimentalCoroutinesApi
    @GetMapping(produces = [APPLICATION_STREAM_JSON_VALUE])
    fun play(@RequestParam countTo: Int): Flow<Answer> {
        return game.play(CountToNumber(countTo))
    }

    @ExperimentalCoroutinesApi
    @PostMapping(
        path = ["/answers"],
        consumes = [APPLICATION_STREAM_JSON_VALUE],
        produces = [APPLICATION_STREAM_JSON_VALUE]
    )
    fun getAnswers(@RequestBody numbers: Flow<NumberToAnswer>): Flow<Answer> {
        return game.getAnswers(numbers.map { it.value })
    }

    @GetMapping(path = ["/answer/{number}"], produces = [APPLICATION_JSON_VALUE])
    fun getAnswer(@PathVariable number: Int): Answer {
        return game.getAnswer(number)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleException(exception: IllegalArgumentException) {
        throw ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, exception.localizedMessage)
    }
}

data class NumberToAnswer(val value: Int)