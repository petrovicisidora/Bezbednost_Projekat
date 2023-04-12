package com.main.app.service;

import com.main.app.config.SecurityUtils;
import com.main.app.domain.dto.UserDTO;
import com.main.app.domain.model.User;
import com.main.app.repository.UserRepository;
import com.main.app.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    UserRepository userRepository;

    @Autowired
    public CurrentUserServiceImpl(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public Optional<User> getCurrentUser() {
        Optional<String> username = SecurityUtils.getCurrentUserLogin();

        return this.userRepository.findOneByEmail(username.get());
    }

    public UserDTO getCurrentUserDTO() {
        Optional<User> user = getCurrentUser();

        return ObjectMapperUtils.map(user.get(), UserDTO.class);
    }

}
