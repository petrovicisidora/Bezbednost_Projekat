package com.main.app.domain.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnore;
import javax.persistence.*;

@Data
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "korisnik_skills")
public class KorisnikSkill {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "korisnik_id", referencedColumnName = "id", nullable = false)
        @JsonIgnore
        private Korisnik korisnik;

        @ManyToOne
        @JoinColumn(name = "skill_id", referencedColumnName = "id", nullable = false)
        @JsonIgnore
        private Skill skill;
}


