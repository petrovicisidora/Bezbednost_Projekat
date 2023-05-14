package com.main.app.service;

import com.main.app.domain.dto.RegistrationRequestDTO;
import com.main.app.domain.model.RegistrationRequest;

public interface RequestService {
    RegistrationRequest sendRequest(RegistrationRequestDTO registrationRequest);
}
