package com.dvo.user_service.service.impl;

import com.dvo.user_service.entity.User;
import com.dvo.user_service.exception.EntityExistsException;
import com.dvo.user_service.exception.EntityNotFoundException;
import com.dvo.user_service.mapper.UserMapper;
import com.dvo.user_service.repository.UserRepository;
import com.dvo.user_service.web.model.request.UpdateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("user")
                .email("email@mail.ru")
                .build();
    }

    @Test
    void testFindAll() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("user", result.getFirst().getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertEquals("user", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_whenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void testCreate() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.create(user);

        assertEquals(user, result);
        verify(userRepository).existsByUsername(anyString());
        verify(userRepository).existsByEmail(anyString());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreate_whenUsernameExists() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> userService.create(user));
    }

    @Test
    void testCreate_whenEmailExists() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> userService.create(user));
    }

    @Test
    void testUpdate() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .email("new@mail.ru")
                .build();
        User existedUser = user;

        when(userRepository.findById(1L)).thenReturn(Optional.of(existedUser));
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        doNothing().when(userMapper).updateRequestToUser(request, existedUser);
        when(userRepository.save(existedUser)).thenReturn(existedUser);

        User result = userService.update(request, 1L);

        assertEquals(existedUser, result);
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail(anyString());
        verify(userRepository).save(existedUser);
    }

    @Test
    void testUpdate_whenEmailExists() {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .email("new@mail.ru")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> userService.update(request, 1L));
    }

    @Test
    void testDeleteById() {
        userService.deleteById(1L);

        verify(userRepository).deleteById(1L);
    }

}
