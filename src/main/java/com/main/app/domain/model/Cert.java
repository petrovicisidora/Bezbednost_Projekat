package com.main.app.domain.model;

import com.main.app.domain.dto.CertDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Cert extends AbstractEntity {

    private String issuerName;

    private String name;

    private String serialNumber;

    private String startDate;

    private String endDate;

    private String cert;

    private String certType;

    public Cert(String issuerName, String name, String serialNumber, String startDate, String endDate, String cert, String certType) {
        this.issuerName = issuerName;
        this.name = name;
        this.serialNumber = serialNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cert = cert;
        this.certType = certType;
    }

    public Cert(CertDTO cert) {
        this.issuerName = cert.getIssuerName();
        this.id = cert.getId();
        this.name = cert.getName();
        this.serialNumber = cert.getSerialNumber();
        this.startDate = cert.getStartDate();
        this.endDate = cert.getEndDate();
        this.cert = cert.getCert();
        this.certType = cert.getCertType();
    }
}
