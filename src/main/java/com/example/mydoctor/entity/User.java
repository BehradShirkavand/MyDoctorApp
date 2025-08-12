package com.example.mydoctor.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.mydoctor.enums.Status;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="username", unique = true, nullable = false)    
    private String username;

    private String password;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name="email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalVisit> medicalVisits;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public void addMedicalVisit(MedicalVisit tempMedicalVisit) {

        if (medicalVisits == null) {
            medicalVisits = new ArrayList<>();
        }

        medicalVisits.add(tempMedicalVisit);
        tempMedicalVisit.setUser(this);
    }
}
