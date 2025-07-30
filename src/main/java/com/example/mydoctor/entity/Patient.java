package com.example.mydoctor.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.mydoctor.enums.Status;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="patient")
public class Patient implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="username", unique = true, nullable = false)    
    private String username;

    private String password;

    @Column(name="email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalVisit> medicalVisits;

    private Set<String> roles = new HashSet<>();

    public void addMedicalVisit(MedicalVisit tempMedicalVisit) {

        if (medicalVisits == null) {
            medicalVisits = new ArrayList<>();
        }

        medicalVisits.add(tempMedicalVisit);
        tempMedicalVisit.setPatient(this);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
    
    @Override public String getUsername() { return username;}
    
    @Override public String getPassword() { return password;}

    @Override public boolean isAccountNonExpired() { return true;}

    @Override public boolean isAccountNonLocked() { return true;}

    @Override public boolean isCredentialsNonExpired() { return true;}

    @Override
    public boolean isEnabled() {
        
        if (status == Status.ACTIVE) {
            return true;
        }
        return false;
    }

}
