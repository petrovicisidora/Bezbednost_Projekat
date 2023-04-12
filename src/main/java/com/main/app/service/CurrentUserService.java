package com.main.app.service;

import com.main.app.domain.dto.UserDTO;
import com.main.app.domain.model.User;

import java.util.Optional;

/**
 * The service used for getting current user.
 */
public interface CurrentUserService {

    Optional<User> getCurrentUser();

    UserDTO getCurrentUserDTO();
}
