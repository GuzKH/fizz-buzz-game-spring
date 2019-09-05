package com.lunchee.fizzbuzz.configuration.controller

import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionAdvice {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleException(exception: IllegalArgumentException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity.unprocessableEntity()
            .body(
                ExceptionResponse(
                    status = UNPROCESSABLE_ENTITY,
                    message = exception.localizedMessage
                )
            )
    }
}