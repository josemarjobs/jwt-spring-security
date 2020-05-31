package com.example.jwtdemo

import com.example.jwtdemo.models.AuthenticationRequest
import com.example.jwtdemo.models.AuthenticationResponse
import com.example.jwtdemo.services.MyUserDetailsService
import com.example.jwtdemo.util.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/")
class HelloResource(
        private var authenticationManager: AuthenticationManager,
        private var userDetailsService: MyUserDetailsService
) {
    @GetMapping("hello")
    fun hello(): String {
        return "Hello World"
    }

    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.username, request.password))

            val user = userDetailsService.loadUserByUsername(request.username)

            return ResponseEntity.ok(AuthenticationResponse(jwt = JwtUtil.generateToken(user)))
        } catch (e: BadCredentialsException) {
            throw IllegalArgumentException("Incorrect username/password")
        }
    }
}