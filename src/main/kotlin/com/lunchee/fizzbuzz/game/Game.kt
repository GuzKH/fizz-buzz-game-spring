package com.lunchee.fizzbuzz.game

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service

@Service
class Game(private val player: Player) {

    @ExperimentalCoroutinesApi
    fun play(untilNumber: CountToNumber): Flow<Answer> {
        return (1..untilNumber.value).asFlow()
            .map { player.giveAnswer(it) }
    }
}
