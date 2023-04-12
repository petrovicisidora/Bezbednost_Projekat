package com.main.app.service;

import com.main.app.config.SecurityUtils;
import com.main.app.domain.dto.UserDTO;
import com.main.app.domain.model.User;
import com.main.app.enums.Role;
import com.main.app.repository.UserRepository;
import com.main.app.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * The implementation of the service used for management of the User data.
 */
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private CurrentUserService currentUserService;

    @Autowired
    public UserServiceImpl(
            UserRepository repository,
            CurrentUserService currentUserService
    ) {
        this.userRepository = repository;
        this.currentUserService = currentUserService;
    }

    public Page<User> getUsers(Pageable page) {
        return userRepository.findAllByDeleted(false, page);
    }

    public User register(UserDTO userDTO) {

        User user = new User(userDTO);
        user.setRole(Role.OPERATOR);
        user.setPassword(UserUtil.encriptUserPassword(user.getPassword()));

        User savedUser = userRepository.save(user);

        return savedUser;
    }

    public Optional<User> getCurrentUser() {
        Optional<String> username = SecurityUtils.getCurrentUserLogin();

        return this.userRepository.findOneByEmail(username.get());
    }
}
