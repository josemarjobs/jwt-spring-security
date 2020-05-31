package com.example.jwtdemo.filter

import com.example.jwtdemo.services.MyUserDetailsService
import com.example.jwtdemo.util.JwtUtil
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter(
        private var userDetailsService: MyUserDetailsService
) : OncePerRequestFilter() {
    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {

        val authHeader = req.getHeader("Authorization")
        var username: String? = null
        var jwt: String? = null

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7)
            username = JwtUtil.extractUsername(jwt)
        }
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = this.userDetailsService.loadUserByUsername(username)

            if (JwtUtil.validateToken(jwt!!, userDetails)) {
                var authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authToken.details = WebAuthenticationDetailsSource().buildDetails(req)

                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        chain.doFilter(req, res)
    }
}