package com.main.app.domain.dto;

import com.main.app.domain.model.Cert;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CertDTO {

    private Long id;
    private String issuerName;
    private String name;
    private String serialNumber;
    private String startDate;
    private String endDate;
    private String cert;
    private String certType;

    public CertDTO(Cert cert) {
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
