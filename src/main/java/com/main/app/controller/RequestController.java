package com.main.app.controller;

import com.main.app.domain.dto.KorisnikDto;
import com.main.app.domain.dto.RegistrationRequestDTO;
import com.main.app.domain.model.Korisnik;
import com.main.app.domain.model.RegistrationRequest;
import com.main.app.service.KorisnikService;
import com.main.app.service.RequestService;
import org.elasticsearch.index.engine.Engine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request")
public class RequestController {
    @Autowired
    private RequestService requestService;

    @PostMapping(value = "/request", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendRequest(@RequestBody RegistrationRequestDTO registrationRequestDTO) {
        RegistrationRequest registrationRequest = requestService.sendRequest(registrationRequestDTO);
        return ResponseEntity.ok(registrationRequest);
    }
   /* @GetMapping
    public ResponseEntity<?> sendRequest(@RequestBody RegistrationRequestDTO registrationRequestDTO) {
        RegistrationRequest registrationRequest = requestService.sendRequest(registrationRequestDTO);
        return ResponseEntity.ok(registrationRequest);
    }
    */


}
