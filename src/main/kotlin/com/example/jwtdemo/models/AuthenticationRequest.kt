package com.example.jwtdemo.models

data class AuthenticationRequest(
        var username: String? = null,
        var password: String? = null
)