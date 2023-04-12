package com.main.app.controller;

import com.main.app.domain.dto.Entities;
import com.main.app.domain.model.Cert;
import com.main.app.domain.model.User;
import com.main.app.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cert")
public class CertController {

    private CertificateService certService;

    @Autowired
    public CertController(CertificateService certService) {
        this.certService = certService;
    }

    @GetMapping
    public ResponseEntity<Entities> getCerts(Pageable page) {
        Entities result = new Entities();

        Page<Cert> certs = certService.getCerts(page);

        result.setEntities(certs.getContent());
        result.setTotal(certs.getTotalElements());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        certService.delete(id);
    }
}
