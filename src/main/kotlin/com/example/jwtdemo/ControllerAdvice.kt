package com.example.jwtdemo

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.IllegalArgumentException

@ControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(*[IllegalArgumentException::class])
    fun handleIncorrectUsernameAndPassword(e: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity.status(400).body(e.message)
    }
}