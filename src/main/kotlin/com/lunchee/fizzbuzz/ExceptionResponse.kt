package com.lunchee.fizzbuzz

import org.springframework.http.HttpStatus

data class ExceptionResponse(val code: Int, val error: String, val message: String) {
    constructor(status: HttpStatus, message: String)
            : this(code = status.value(), error = status.name, message = message)
}