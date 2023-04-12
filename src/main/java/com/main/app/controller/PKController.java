package com.main.app.controller;

import com.main.app.domain.dto.CertDTO;
import com.main.app.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.cert.X509Certificate;

@RestController
@RequestMapping("/api/pk")
public class PKController {

    private CertificateService certificateService;

    @Autowired
    public PKController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping
    ResponseEntity<String> createPK(@RequestBody CertDTO certDTO) {

        X509Certificate cert = certificateService.generate(certDTO.getIssuerName(), certDTO.getName(),
                certDTO.getSerialNumber(), certDTO.getStartDate(), certDTO.getEndDate(), certDTO.getCertType());

        return new ResponseEntity<>(cert.toString(), HttpStatus.OK);
    }
}
