package com.dvo.user_service.service;

import com.dvo.user_service.entity.User;
import com.dvo.user_service.web.model.request.UpdateUserRequest;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(Long id);

    User create(User user);

    User update(UpdateUserRequest request, Long id);

    void deleteById(Long id);
}
