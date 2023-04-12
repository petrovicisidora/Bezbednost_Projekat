package com.main.app.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserUtil {
    public static String encriptUserPassword(String password) {
        return new BCryptPasswordEncoder().encode(String.valueOf(password));
    }
}
