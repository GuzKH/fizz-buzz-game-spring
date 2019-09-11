package com.lunchee.fizzbuzz.game

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class Game(private val player: Player) {

    private val log = LoggerFactory.getLogger(Game::class.java)

    @ExperimentalCoroutinesApi
    fun play(untilNumber: CountToNumber): Flow<Answer> {
        return getAnswers((1..untilNumber.value).asFlow())
    }

    @ExperimentalCoroutinesApi
    fun getAnswers(numbers: Flow<Int>): Flow<Answer> {
        return numbers
            .map { getAnswer(it) }
            .onStart { log.debug("Answering") }
            .onCompletion { log.debug("Answered") }
            .flowOn(Dispatchers.Default)
    }

    fun getAnswer(number: Int): Answer {
        return player.giveAnswer(number)
    }
}
