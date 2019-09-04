package com.lunchee.fizzbuzz

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FizzbuzzBackendApplication

fun main(args: Array<String>) {
	runApplication<FizzbuzzBackendApplication>(*args)
}
