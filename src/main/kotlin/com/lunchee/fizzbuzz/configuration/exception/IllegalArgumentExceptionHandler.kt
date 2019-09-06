package com.lunchee.fizzbuzz.configuration.exception

import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

@ControllerAdvice
class IllegalArgumentExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleException(exception: IllegalArgumentException) {
        throw ResponseStatusException(UNPROCESSABLE_ENTITY, exception.localizedMessage)
    }
}