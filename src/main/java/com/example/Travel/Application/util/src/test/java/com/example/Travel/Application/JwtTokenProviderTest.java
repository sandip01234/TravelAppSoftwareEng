package com.example.Travel.Application;


import com.example.Travel.Application.Security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * ███████╗ █████╗ ███╗   ██╗██████╗ ██╗██████╗
 * ██╔════╝██╔══██╗████╗  ██║██╔══██╗██║██╔══██╗
 * ███████╗███████║██╔██╗ ██║██║  ██║██║██████╔╝
 * ╚════██║██╔══██║██║╚██╗██║██║  ██║██║██╔═══╝
 * ███████║██║  ██║██║ ╚████║██████╔╝██║██║
 * ╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═════╝ ╚═╝╚═╝
 *
 * @author HP VICTUS on 9/1/2025
 */
public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();

        // Inject test values (since @Value won’t work in unit tests without Spring context)
        // Java
        jwtTokenProvider.jwtSecret = "ZmFrZVNlY3JldEtleUZvclRlc3RpbmdBQUFBQUFBQUFBQUFBQUFBQQ=="; // 32 bytes Base64
        jwtTokenProvider.jwtExpirationMs = 3600000; // 1 hour
    }

    @Test
    void testGenerateAndValidateToken() {
        // given
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testuser", null, Collections.emptyList());

        // when
        String token = jwtTokenProvider.generateToken(authentication);

        // then
        assertThat(token).isNotNull();
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
        assertThat(jwtTokenProvider.getUsernameFromJWT(token)).isEqualTo("testuser");
    }

    @Test
    void testExpiredToken() throws InterruptedException {
        // given
        jwtTokenProvider.jwtExpirationMs = 1; // 1 millisecond
        Authentication authentication =
                new UsernamePasswordAuthenticationToken("testuser", null, Collections.emptyList());

        String token = jwtTokenProvider.generateToken(authentication);

        // wait so token expires
        Thread.sleep(5);

        // then
        assertThat(jwtTokenProvider.validateToken(token)).isFalse();
    }

    @Test
    void testInvalidToken() {
        // given
        String invalidToken = "abc.def.ghi";

        // then
        assertThat(jwtTokenProvider.validateToken(invalidToken)).isFalse();
    }
}
