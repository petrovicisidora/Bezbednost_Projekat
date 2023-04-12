package com.main.app.service;

import com.main.app.domain.internal.IssuerData;
import com.main.app.domain.internal.SubjectData;
import com.main.app.domain.model.Cert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;

@Service
public interface CertificateService {
    X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData);

    X509Certificate generate(String issuerName, String name, String serialNumber, String startDate, String endDate, String certType);
    Page<Cert> getCerts(Pageable page);
    void delete(Long id);
}
