package howru.howru.globalConfig.security

import howru.howru.jwt.filterLogic.JwtAuthenticationFilter
import howru.howru.jwt.filterLogic.JwtTokenProvider
import howru.howru.member.controller.constant.MemberUrl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig
    @Autowired
    constructor(
        private val jwtTokenProvider: JwtTokenProvider
    ) {
        @Bean
        fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

        @Bean
        fun filterChain(http: HttpSecurity): SecurityFilterChain? {
            http.csrf { obj -> obj.disable() }
            http.requestCache { cache -> cache.disable() }
            http.sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            http.authorizeHttpRequests { path ->
                path.requestMatchers(
                    MemberUrl.SIGNUP,
                    MemberUrl.LOGIN,
                    MemberUrl.JWT_TOKEN_REISSUE,
                    MemberUrl.RECOVERY_MEMBER
                ).permitAll().anyRequest().authenticated()
            }
            http.addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
            http.exceptionHandling { exception -> exception.accessDeniedPage(MemberUrl.PROHIBITION) }
            return http.build()
        }
    }
