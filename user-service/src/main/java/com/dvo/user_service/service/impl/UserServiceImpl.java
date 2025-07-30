package com.dvo.user_service.service.impl;

import com.dvo.user_service.entity.User;
import com.dvo.user_service.exception.EntityExistsException;
import com.dvo.user_service.exception.EntityNotFoundException;
import com.dvo.user_service.mapper.UserMapper;
import com.dvo.user_service.repository.UserRepository;
import com.dvo.user_service.service.UserService;
import com.dvo.user_service.web.model.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<User> findAll() {
        log.info("Call findAll in UserServiceImpl");

        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        log.info("Call findById in UserServiceImpl with ID: {}", id);

        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("User with ID: {0} not found", id)));
    }

    @Override
    @Transactional
    public User create(User user) {
        log.info("Call create in UserServiceImpl with user: {}", user);

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new EntityExistsException(MessageFormat.format("Username: {0} already exists", user.getUsername()));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EntityExistsException(MessageFormat.format("Email: {0} already exists", user.getEmail()));
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(UpdateUserRequest request, Long id) {
        log.info("Call update in UserServiceImpl for ID: {}, with request: {}", id, request);

        User existedUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("User with ID: {0} not found", id)));

        if (request.getEmail() != null && !request.getEmail().equals(existedUser.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new EntityExistsException(MessageFormat.format("Email: {0} already exists", request.getEmail()));
        }

        userMapper.updateRequestToUser(request, existedUser);

        return userRepository.save(existedUser);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Call deleteById in UserServiceImpl with ID: {}", id);

        userRepository.deleteById(id);
    }
}
