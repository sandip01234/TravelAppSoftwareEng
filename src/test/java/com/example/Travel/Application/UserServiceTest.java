package com.example.Travel.Application;

/**
 * ███████╗ █████╗ ███╗   ██╗██████╗ ██╗██████╗
 * ██╔════╝██╔══██╗████╗  ██║██╔══██╗██║██╔══██╗
 * ███████╗███████║██╔██╗ ██║██║  ██║██║██████╔╝
 * ╚════██║██╔══██║██║╚██╗██║██║  ██║██║██╔═══╝
 * ███████║██║  ██║██║ ╚████║██████╔╝██║██║
 * ╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═════╝ ╚═╝╚═╝
 *
 * @author HP VICTUS on 9/14/2025
 */

import com.example.Travel.Application.Dto.RegisterRequest;
import com.example.Travel.Application.Entity.User;
import com.example.Travel.Application.Repository.UserRepository;
import com.example.Travel.Application.Services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService; // Injects mocks into UserService

    @Test
    void testRegister_NewUser_Success() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        request.setEmail("test@example.com");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");

        // Act
        userService.register(request);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_UsernameAlreadyExists_ThrowsException() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existinguser");
        request.setPassword("password123");
        request.setEmail("existing@example.com");

        when(userRepository.findByUsername("existinguser"))
                .thenReturn(Optional.of(new User())); // Pretend user exists

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.register(request));

        assertEquals("Username already exists", exception.getMessage());

        verify(userRepository, never()).save(any(User.class)); // ensure not saved
    }
}
