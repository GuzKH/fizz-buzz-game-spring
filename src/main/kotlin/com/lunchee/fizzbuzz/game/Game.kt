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
        return (1..untilNumber.value).asFlow()
            .map { player.giveAnswer(it) }
            .onStart { log.debug("Game started") }
            .onCompletion { log.debug("Game ended") }
            .flowOn(Dispatchers.Default)
    }
}
