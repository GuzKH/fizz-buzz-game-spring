package com.lunchee.fizzbuzz.game

data class CountToNumber(val value: Int) {
    init {
        require(value >= 1) { "'Count to number' should be greater than zero." }
    }
}