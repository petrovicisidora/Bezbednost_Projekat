package com.main.app.service;

import com.main.app.domain.dto.RegistrationRequestDTO;
import com.main.app.domain.model.RegistrationRequest;
import com.main.app.enums.Status;
import com.main.app.repository.RequestRepository;
import org.graalvm.compiler.nodes.graphbuilderconf.InvocationPlugins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {
        @Autowired
        private RequestRepository requestRepository ;

        @Override
        public RegistrationRequest sendRequest(RegistrationRequestDTO registrationRequest){
            Optional<RegistrationRequest> existingRequest = requestRepository.findByEmail(registrationRequest.getEmail());

            RegistrationRequest registrationRequest1 = new RegistrationRequest();
            registrationRequest1.setEmail(registrationRequest.getEmail());
            registrationRequest1.setFirstName(registrationRequest.getFirstName());
            registrationRequest1.setLastName(registrationRequest.getLastName());
            registrationRequest1.setAddress(registrationRequest.getAddress());
            registrationRequest1.setCity(registrationRequest.getCity());
            registrationRequest1.setState(registrationRequest.getState());
            registrationRequest1.setPhoneNumber(registrationRequest.getPhoneNumber());
            registrationRequest1.setJobTitle(registrationRequest.getJobTitle());
            registrationRequest1.setStatus(Status.PENDING);

            return requestRepository.save(registrationRequest1);
        }

}
