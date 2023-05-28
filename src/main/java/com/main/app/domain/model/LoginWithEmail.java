package com.main.app.domain.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Embeddable
public class LoginWithEmail implements Serializable {
    @Column(name = "token")
    private UUID token;
    @Column(name = "expiration_date")
    private Date expirationDate;
    @Column(name = "is_used")
    private boolean isUsed;
}
