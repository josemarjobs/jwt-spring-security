package com.example.jwtdemo.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

object JwtUtil {
    val SECRET_KEY = "secret"

    fun extractUsername(token: String): String = extractClaim(token, Claims::getSubject)

    fun extractExpiration(token: String): Date = extractClaim(token, Claims::getExpiration)

    fun generateToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = mutableMapOf()
        return createToken(claims, userDetails.username)
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        return extractUsername(token) == userDetails.username && !isTokenExpired(token);
    }

    private fun createToken(claims: Map<String, Any>, username: String?): String {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(Date())
                .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    private fun <T> extractClaim(token: String, claimsResolvers: (Claims) -> T): T {
        val claims = extractAllClaims(token);
        return claimsResolvers(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).body
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }


}